import org.example.CustomArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {
    @Test
    void newListShouldBeEmpty() {
        CustomArrayList<String> list = new CustomArrayList<>();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void addShouldPreserveElementOrder() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void addShouldExpandArrayWhenCapacityIsExceeded() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 0; i < 11; i++) {
            list.add(i);
        }

        assertEquals(11, list.size());

        for (int i = 0; i < 11; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    void getShouldReturnElementsAtDifferentIndexes() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        assertEquals("A", list.get(0));
        assertEquals("C", list.get(2));
        assertEquals("E", list.get(4));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1})
    void getShouldThrowExceptionForInvalidIndex(int index) {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1})
    void removeShouldThrowExceptionForInvalidIndex(int index) {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
    }

    @Test
    void removeShouldDeleteElementsAtDifferentIndexes() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        assertEquals("A", list.remove(0));
        assertEquals("C", list.remove(1));
        assertEquals("E", list.remove(2));

        assertEquals(2, list.size());
        assertEquals("B", list.get(0));
        assertEquals("D", list.get(1));
    }

    @Test
    void addShouldThrowExceptionForNull() {
        CustomArrayList<String> list = new CustomArrayList<>();

        assertThrows(NullPointerException.class, () -> list.add(null));

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void iteratorShouldReturnElementsInOrder() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> iterator = list.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratorNextShouldThrowExceptionWhenNoElementsLeft() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("A");

        Iterator<String> iterator = list.iterator();

        assertEquals("A", iterator.next());

        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }
}