package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static ValidationResult validate(Object object) {
        if (object == null) {
            throw new NullPointerException("Object must not be null");
        }

        List<ValidationError> errors = new ArrayList<>();
        Class<?> currentClass = object.getClass();

        while (currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }

                if (!field.trySetAccessible()) {
                    throw new ValidationConfigurationException("Cannot access field: " + currentClass.getName() + "." + field.getName());
                }

                Object value;

                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    throw new ValidationConfigurationException("Cannot access field: " + currentClass.getName() + "." + field.getName(), e);
                }

                NotNull notNull = field.getAnnotation(NotNull.class);
                if (notNull != null && value == null) {
                    errors.add(new ValidationError(field.getName(), "NotNull", notNull.message()));
                }

                Size size = field.getAnnotation(Size.class);
                if (size != null) {
                    if (!CharSequence.class.isAssignableFrom(field.getType())) {
                        throw new ValidationConfigurationException("@Size can only be used on CharSequence fields: " + field.getName());
                    }

                    if (size.min() < 0 || size.min() > size.max()) {
                        throw new ValidationConfigurationException("Invalid @Size configuration on field: " + field.getName());
                    }

                    if (value != null) {
                        CharSequence text = (CharSequence) value;

                        if (text.length() < size.min() || text.length() > size.max()) {
                            errors.add(new ValidationError(field.getName(), "Size", size.message()));
                        }
                    }
                }

                Range range = field.getAnnotation(Range.class);
                if (range != null) {
                    if (!isNumericType(field.getType())) {
                        throw new ValidationConfigurationException("@Range can only be used on numeric fields: " + field.getName());
                    }

                    if (Double.isNaN(range.min()) || Double.isNaN(range.max()) || Double.isInfinite(range.min())
                            || Double.isInfinite(range.max()) || range.min() > range.max()) {
                        throw new ValidationConfigurationException("Invalid @Range configuration on field: " + field.getName());
                    }

                    if (value != null) {
                        double number = ((Number) value).doubleValue();

                        if (Double.isNaN(number) || number < range.min() || number > range.max()) {
                            errors.add(new ValidationError(field.getName(), "Range", range.message()));
                        }
                    }
                }

                Email email = field.getAnnotation(Email.class);
                if (email != null) {
                    if (!CharSequence.class.isAssignableFrom(field.getType())) {
                        throw new ValidationConfigurationException("@Email can only be used on CharSequence fields: " + field.getName());
                    }

                    if (value != null) {
                        CharSequence text = (CharSequence) value;

                        if (!isValidEmail(text)) {
                            errors.add(new ValidationError(field.getName(), "Email", email.message()));
                        }
                    }
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return new ValidationResult(errors);
    }

    private static boolean isNumericType(Class<?> type) {
        if (type.isPrimitive()) {
            return type == byte.class || type == short.class || type == int.class
                    || type == long.class || type == float.class || type == double.class;
        }

        return Number.class.isAssignableFrom(type);
    }

    private static boolean isValidEmail(CharSequence value) {
        return value.toString().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
}