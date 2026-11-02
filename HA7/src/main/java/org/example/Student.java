package org.example;

/**
 * Неизменяемая модель студента.
 *
 * @param id    положительный уникальный идентификатор
 * @param name  непустое имя
 * @param grade оценка от 0.0 до 10.0 включительно
 */
public record Student(int id, String name, double grade) {

    /**
     * Создаёт студента и проверяет значения.
     *
     * @throws IllegalArgumentException если id не положителен, имя пустое
     * или оценка вне диапазона [0.0, 10.0]
     */
    public Student {
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name не может быть пустым");
        }
        if (Double.isNaN(grade) || grade < 0.0 || grade > 10.0) {
            throw new IllegalArgumentException("grade должна лежать в диапазоне [0.0, 10.0]");
        }
    }
}