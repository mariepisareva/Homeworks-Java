package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    static class ValidUser {
        @NotNull
        private String name = "Alex";

        @Size(min = 2, max = 10)
        private String username = "alex";

        @Range(min = 18, max = 100)
        private int age = 25;

        @Email
        private String email = "alex@example.com";
    }

    static class InvalidNotNull {
        @NotNull
        private String name = null;
    }

    static class InvalidSize {
        @Size(min = 2, max = 10)
        private String name = "A";
    }

    static class InvalidRange {
        @Range(min = 18, max = 100)
        private int age = 15;
    }

    static class InvalidEmail {
        @Email
        private String email = "wrong-email";
    }

    static class InvalidUser {
        @NotNull
        private String name = null;

        @Size(min = 2, max = 10)
        private String username = "A";

        @Range(min = 18, max = 100)
        private int age = 15;

        @Email
        private String email = "wrong-email";
    }

    static class SizeBoundaryUser {
        @Size(min = 2, max = 5)
        private String minValue = "ab";

        @Size(min = 2, max = 5)
        private String maxValue = "abcde";
    }

    static class RangeBoundaryUser {
        @Range(min = 18, max = 100)
        private int minValue = 18;

        @Range(min = 18, max = 100)
        private int maxValue = 100;
    }

    static class InvalidBoundaryUser {
        @Size(min = 2, max = 5)
        private String tooShort = "a";

        @Size(min = 2, max = 5)
        private String tooLong = "abcdef";

        @Range(min = 18, max = 100)
        private int tooSmall = 17;

        @Range(min = 18, max = 100)
        private int tooLarge = 101;
    }

    static class InvalidSizeConfiguration {
        @Size(min = 10, max = 2)
        private String name = "Alex";
    }

    static class InvalidRangeConfiguration {
        @Range(min = 100, max = 10)
        private int age = 20;
    }

    static class InvalidSizeType {
        @Size(min = 1, max = 5)
        private int age = 20;
    }

    static class InvalidRangeType {
        @Range(min = 1, max = 5)
        private String age = "20";
    }

    static class Parent {
        @NotNull
        private String parentName = null;
    }

    static class Child extends Parent {
        @NotNull
        private String childName = "Alex";
    }

    static class StaticFieldsUser {
        @NotNull
        private static String name = null;
    }

    @Test
    void validObjectShouldReturnEmptyErrors() {
        ValidationResult result = Validator.validate(new ValidUser());

        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void notNullShouldDetectViolation() {
        ValidationResult result = Validator.validate(new InvalidNotNull());

        assertFalse(result.isValid());
        assertEquals("NotNull", result.getErrors().get(0).annotationName());
    }

    @Test
    void sizeShouldDetectViolation() {
        ValidationResult result = Validator.validate(new InvalidSize());

        assertFalse(result.isValid());
        assertEquals("Size", result.getErrors().get(0).annotationName());
    }

    @Test
    void rangeShouldDetectViolation() {
        ValidationResult result = Validator.validate(new InvalidRange());

        assertFalse(result.isValid());
        assertEquals("Range", result.getErrors().get(0).annotationName());
    }

    @Test
    void emailShouldDetectViolation() {
        ValidationResult result = Validator.validate(new InvalidEmail());

        assertFalse(result.isValid());
        assertEquals("Email", result.getErrors().get(0).annotationName());
    }

    @Test
    void multipleErrorsShouldBeCollected() {
        ValidationResult result = Validator.validate(new InvalidUser());

        assertEquals(4, result.getErrors().size());
    }

    @Test
    void sizeShouldAcceptMinAndMaxValues() {
        ValidationResult result = Validator.validate(new SizeBoundaryUser());

        assertTrue(result.isValid());
    }

    @Test
    void rangeShouldAcceptMinAndMaxValues() {
        ValidationResult result = Validator.validate(new RangeBoundaryUser());

        assertTrue(result.isValid());
    }

    @Test
    void sizeAndRangeShouldRejectValuesOutsideBoundaries() {
        ValidationResult result = Validator.validate(new InvalidBoundaryUser());

        assertEquals(4, result.getErrors().size());
    }

    @Test
    void validateNullShouldThrowException() {
        assertThrows(
                NullPointerException.class,
                () -> Validator.validate(null)
        );
    }

    @Test
    void invalidSizeConfigurationShouldThrowException() {
        assertThrows(
                ValidationConfigurationException.class,
                () -> Validator.validate(new InvalidSizeConfiguration())
        );
    }

    @Test
    void invalidRangeConfigurationShouldThrowException() {
        assertThrows(
                ValidationConfigurationException.class,
                () -> Validator.validate(new InvalidRangeConfiguration())
        );
    }

    @Test
    void annotationOnUnsupportedTypeShouldThrowException() {
        assertThrows(
                ValidationConfigurationException.class,
                () -> Validator.validate(new InvalidSizeType())
        );

        assertThrows(
                ValidationConfigurationException.class,
                () -> Validator.validate(new InvalidRangeType())
        );
    }

    @Test
    void privateAndInheritedFieldsShouldBeValidated() {
        ValidationResult result = Validator.validate(new Child());

        assertFalse(result.isValid());
        assertEquals("parentName", result.getErrors().get(0).fieldName());
    }

    @Test
    void staticFieldsShouldBeIgnored() {
        ValidationResult result = Validator.validate(new StaticFieldsUser());

        assertTrue(result.isValid());
    }

    @Test
    void errorsListShouldBeImmutable() {
        ValidationResult result = Validator.validate(new InvalidNotNull());

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.getErrors().add(
                        new ValidationError("test", "NotNull", "test")
                )
        );
    }
}