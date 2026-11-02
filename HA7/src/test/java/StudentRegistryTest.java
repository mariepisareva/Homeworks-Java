import org.example.Student;
import org.example.StudentRegistry;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentRegistryTest {

    private static final Student ANNA = new Student(1, "Anna", 4.0);
    private static final Student BORIS = new Student(2, "Boris", 7.5);
    private static final Student CARL = new Student(3, "Carl", 10.0);
    private static final Student DINA = new Student(4, "Dina", 0.0);
    private static final Student EVA = new Student(5, "Eva", 6.0);

    private static final List<Student> ALL = List.of(ANNA, BORIS, CARL, DINA, EVA);

    private static Map<Integer, Student> hashMap() {
        return StudentRegistry.createHashMap(ALL);
    }

    private static NavigableMap<Integer, Student> descendingMap() {
        return StudentRegistry.createDescendingTreeMap(ALL);
    }

    private static void assertSameStudents(Set<Student> expected, List<Student> actual) {
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, new HashSet<>(actual));
    }

    @Test
    void rangeReturnsStudentsInsideRange() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap(), 5.0, 8.0);
        assertSameStudents(Set.of(BORIS, EVA), result);
    }

    @Test
    void rangeIncludesLowerBound() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap(), 4.0, 5.0);
        assertSameStudents(Set.of(ANNA), result);
    }

    @Test
    void rangeIncludesUpperBound() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap(), 9.0, 10.0);
        assertSameStudents(Set.of(CARL), result);
    }

    @Test
    void rangeWithBothBoundsEqualToGrades() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap(), 4.0, 7.5);
        assertSameStudents(Set.of(ANNA, BORIS, EVA), result);
    }

    @Test
    void fullScaleRangeReturnsAllStudents() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap(), 0.0, 10.0);
        assertSameStudents(Set.copyOf(ALL), result);
    }

    @Test
    void rangeOnEmptyMapReturnsEmptyList() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(new HashMap<>(), 0.0, 10.0);
        assertTrue(result.isEmpty());
    }

    @Test
    void rangeThrowsWhenMinGreaterThanMax() {
        Map<Integer, Student> map = hashMap();
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(map, 8.0, 5.0));
    }

    @Test
    void rangeThrowsWhenMinBelowAllowedScale() {
        Map<Integer, Student> map = hashMap();
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(map, -0.1, 5.0));
    }

    @Test
    void rangeThrowsWhenMaxAboveAllowedScale() {
        Map<Integer, Student> map = hashMap();
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(map, 5.0, 10.1));
    }

    @Test
    void topNReturnsLargestIdsInDescendingOrder() {
        List<Student> result = StudentRegistry.getTopNStudentsById(descendingMap(), 3);
        assertEquals(List.of(EVA, DINA, CARL), result);
    }

    @Test
    void topNEqualToSizeReturnsAllInDescendingOrder() {
        List<Student> result = StudentRegistry.getTopNStudentsById(descendingMap(), 5);
        assertEquals(List.of(EVA, DINA, CARL, BORIS, ANNA), result);
    }

    @Test
    void topNLargerThanSizeReturnsAllInDescendingOrder() {
        List<Student> result = StudentRegistry.getTopNStudentsById(descendingMap(), 100);
        assertEquals(List.of(EVA, DINA, CARL, BORIS, ANNA), result);
    }

    @Test
    void topZeroReturnsEmptyList() {
        List<Student> result = StudentRegistry.getTopNStudentsById(descendingMap(), 0);
        assertTrue(result.isEmpty());
    }

    @Test
    void topNOnEmptyMapReturnsEmptyList() {
        NavigableMap<Integer, Student> empty = StudentRegistry.createDescendingTreeMap(List.of());
        List<Student> result = StudentRegistry.getTopNStudentsById(empty, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void topNThrowsWhenNIsNegative() {
        NavigableMap<Integer, Student> map = descendingMap();
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.getTopNStudentsById(map, -1));
    }
}