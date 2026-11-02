package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Collections;
import java.util.HashMap;

/**
 * Операции выборки по реестру студентов.
 */
public final class StudentRegistry {

    private StudentRegistry() {
    }

    /**
     * Создаёт {@link HashMap}, где ключ равен идентификатору студента.
     *
     * @param students студенты для добавления
     * @return новая карта id -> студент
     */
    public static Map<Integer, Student> createHashMap(Iterable<Student> students) {
        Map<Integer, Student> map = new HashMap<>();
        for (Student student : students) {
            map.put(student.id(), student);
        }
        return map;
    }

    /**
     * Создаёт {@link TreeMap}, где ключи отсортированы по убыванию.
     *
     * @param students студенты для добавления
     * @return новая карта id -> студент с убывающим порядком ключей
     */
    public static NavigableMap<Integer, Student> createDescendingTreeMap(Iterable<Student> students) {
        NavigableMap<Integer, Student> map = new TreeMap<>(Collections.reverseOrder());
        for (Student student : students) {
            map.put(student.id(), student);
        }
        return map;
    }

    /**
     * Возвращает студентов с оценкой из замкнутого диапазона.
     *
     * @param students карта студентов, не изменяется
     * @param minGrade нижняя граница включительно
     * @param maxGrade верхняя граница включительно
     * @return новый изменяемый список подходящих студентов
     * @throws IllegalArgumentException если minGrade больше maxGrade
     * или границы вне диапазона [0.0, 10.0]
     */
    public static List<Student> findStudentsByGradeRange(
            Map<Integer, Student> students,
            double minGrade,
            double maxGrade) {
        if (Double.isNaN(minGrade) || Double.isNaN(maxGrade)
                || minGrade < 0.0 || maxGrade > 10.0
                || minGrade > maxGrade) {
            throw new IllegalArgumentException("неверные значения границ: они должны лежать в 0.0 <= minGrade <= maxGrade <= 10.0");
        }
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.grade() >= minGrade && student.grade() <= maxGrade) {
                result.add(student);
            }
        }
        return result;
    }

    /**
     * Возвращает не более n студентов с наибольшими идентификаторами
     * в порядке убывания id. Карта должна быть создана с убывающим порядком ключей.
     *
     * @param students карта с убывающей сортировкой ключей, не изменяется
     * @param n сколько студентов вернуть
     * @return новый изменяемый список
     * @throws IllegalArgumentException если n отрицательно
     */
    public static List<Student> getTopNStudentsById(
            NavigableMap<Integer, Student> students,
            int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n не может быть отрицательным");
        }
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (result.size() >= n) {
                break;
            }
            result.add(student);
        }
        return result;
    }
}