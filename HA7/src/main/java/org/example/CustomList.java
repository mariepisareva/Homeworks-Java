package org.example;

/**
 * Интерфейс списка, определяющий основные операции
 * для работы с элементами
 *
 * @param <E> тип элементов списка
 */
public interface CustomList<E> extends Iterable<E> {
    /**
     * Добавляет элемент в конец списка
     *
     * @param element добавляемый элемент
     * @throws NullPointerException если элемент равен null
     */
    void add(E element);

    /**
     * Возвращает элемент по указанному индексу
     *
     * @param index индекс элемента
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    E get(int index);

    /**
     * Удаляет элемент по указанному индексу
     *
     * @param index индекс удаляемого элемента
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    E remove(int index);

    /**
     * Возвращает количество элементов списка
     *
     * @return количество элементов
     */
    int size();

    /**
     * Проверяет, пуст ли список
     *
     * @return true, если список пуст, иначе false
     */
    boolean isEmpty();
}