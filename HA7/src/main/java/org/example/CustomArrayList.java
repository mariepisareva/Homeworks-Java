package org.example;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация динамического массива
 * Хранит элементы во внутреннем массиве Object[]
 * и автоматически увеличивает его ёмкость
 * при заполнении.
 *
 * @param <E> тип элементов списка
 */
public class CustomArrayList<E> implements CustomList<E> {
    private Object[] elementData;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Создаёт пустой список с начальной ёмкостью 10.
     */
    public CustomArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Создаёт пустой список с указанной начальной ёмкостью.
     *
     * @param initCapacity начальная ёмкость массива
     * @throws IllegalArgumentException если ёмкость отрицательная
     */
    public CustomArrayList(int initCapacity) {
        if (initCapacity < 0) {
            throw new IllegalArgumentException("Недопустимое значение размера");
        }
        elementData = new Object[initCapacity];
    }

    /**
     * Добавляет элемент в конец списка, если список заполнен, то увеличивает его в 1.5 раза.
     *
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException("Элемент не может быть null");
        }
        int newCapacity = 0;
        if (elementData.length == 0) {
            newCapacity = 1;
            elementData = Arrays.copyOf(elementData, newCapacity);
        }else if (size == elementData.length) {
            newCapacity = elementData.length + elementData.length / 2;
            if (newCapacity <= elementData.length) {
                newCapacity++;
            }
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
        elementData[size] = element;
        size++;
    }

    /**
     * Возвращает элемент  по индексу.
     *
     * @param index индекс элемента
     * @throws IndexOutOfBoundsException если индекс находится вне диапазона
     * @return элемент по индексу
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона значений массива");
        }
        return (E) elementData[index];
    }

    /**
     * Удаляет элемент из списка по индексу
     * Сдвигает следующие после удаленного элементы влево
     * очищает осовободившуюся ячейку.
     *
     * @param index индекс удаляемого элемента
     * @return удаленный элемент
     * @throws IndexOutOfBoundsException если индекс находится вне диапазона
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона значений массива");
        }
        E removeElement = (E) elementData[index];
        System.arraycopy(elementData, index + 1, elementData, index, size - index - 1);
        size--;
        elementData[size]=null;

        return removeElement;
    }

    /**
     * Возвращает количество элементов в списке.
     *
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пустой ли список.
     *
     * @return true - если список пуст, иначе - false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *  Возвращает итератор для последовательного обхода элементов.
     *
     * @return итератор списка
     */
    @Override
    public Iterator<E> iterator() {
        return new CustomIterator();
    }

    /**
     * Итератор для обхода элементов динамического массива.
     */
    private class CustomIterator implements Iterator<E> {
        private int cursor = 0;

        /**
         * Проверяет, есть ли следующий элемент в списке.
         *
         * @return true - если элемент есть, иначе - false
         */
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        /**
         * Возвращает следующий элемент списка.
         *
         * @return следующий элемент
         * @throws NoSuchElementException если следующего элемента нет
         */
        @Override
        public E next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
             E element = (E) elementData[cursor];
            cursor++;
            return element;
        }
    }
}
