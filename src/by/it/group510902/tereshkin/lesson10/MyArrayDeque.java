package by.it.group510902.tereshkin.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Collection<E> {
    static final int DEF_CAPACITY = 10;
    private Object[] elements = new Object[DEF_CAPACITY];
    private int head = 0;
    private int tail = 0;
    private int size = 0;
    private int capacity = DEF_CAPACITY;

    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % capacity]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public int size() { return size; }

    public boolean add(E element) {
        if (size < capacity) {
            elements[tail] = element;
            tail = (tail + 1) % capacity;
        } else {
            Object[] bigger = new Object[capacity * 2];
            for (int i = 0; i < size; i++) bigger[i] = elements[(head + i) % capacity];
            bigger[size] = element;
            head = 0;
            capacity *= 2;
            tail = size + 1;
            elements = bigger;
        }
        size++;
        return true;
    }

    public void addFirst(E element) {
        if (size < capacity) {
            head = (head - 1 + capacity) % capacity;
            elements[head] = element;
        } else {
            Object[] bigger = new Object[capacity * 2];
            for (int i = 0; i < size; i++) bigger[i + 1] = elements[(head + i) % capacity];
            head = 0;
            bigger[head] = element;
            capacity *= 2;
            tail = size + 1;
            elements = bigger;
        }
        size++;
    }

    public void addLast(E element) { add(element); }

    public E element() { return getFirst(); }

    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[head];
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[(tail - 1 + capacity) % capacity];
    }

    public E poll() { return pollFirst(); }

    public E pollFirst() {
        if (size == 0) return null;
        E value = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % capacity;
        size--;
        return value;
    }

    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + capacity) % capacity;
        E value = (E) elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    // ---------- методы Collection, которых не хватало ----------

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            Object v = elements[(head + i) % capacity];
            if (o == null ? v == null : o.equals(v)) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int idx = 0;
            @Override public boolean hasNext() { return idx < size; }
            @Override public E next() {
                if (idx >= size) throw new NoSuchElementException();
                return (E) elements[(head + idx++) % capacity];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[(head + i) % capacity];
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) a[i] = (T) elements[(head + i) % capacity];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            int pos = (head + i) % capacity;
            Object v = elements[pos];
            if (o == null ? v == null : o.equals(v)) {
                for (int j = i; j < size - 1; j++) {
                    elements[(head + j) % capacity] =
                            elements[(head + j + 1) % capacity];
                }
                tail = (tail - 1 + capacity) % capacity;
                elements[tail] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) { add(e); changed = true; }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            while (remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; ) {
            Object v = elements[(head + i) % capacity];
            if (!c.contains(v)) { remove(v); changed = true; }
            else i++;
        }
        return changed;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) elements[i] = null;
        head = 0;
        tail = 0;
        size = 0;
    }
}