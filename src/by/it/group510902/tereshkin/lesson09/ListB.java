package by.it.group510902.tereshkin.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private Object[] elements = new Object[10];
    private int size;
    private int capacity;

    private static final int DEFAULT_CAPACITY = 10;

    public ListB() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public ListB(int capacity) {
        this.elements = new Object[capacity];
        this.size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            Object[] bigger = new Object[elements.length * 2];
            for (int i = 0; i < elements.length; i++) {
                bigger[i] = elements[i];
            }
            elements = bigger;
        }
        elements[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        Object ans = elements[index];
        for (int i = index; i < this.size-1; i++){
            elements[i] = elements[i+1];
        }
        elements[this.size-1] = null;
        this.size --;
        return (E) ans;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > this.size) {
            return;
        }
        if (size == elements.length) {
            Object[] bigger = new Object[elements.length * 2];
            for (int i = 0; i < index; i++) {
                bigger[i] = elements[i];
            }

            for (int i = size; i > index; i--){
                bigger[i] = elements[i-1];
            }
            bigger[index] = element;
            elements = bigger;
            size++;
        }
        else {
            for (int i = size; i > index; i--) {
                elements[i] = elements[i - 1];
            }
            elements[index] = element;
            size++;
        }
    }

    @Override
    public boolean remove(Object o) {
        int i = 0;
        while (i < size) {
            if (o.equals(elements[i])){
                break;
            };
            i++;
        }
        if (i == size) {
            return false;
        }
        int j;
        for (j = i; j < size - 1; j++) {
            elements[j] = elements[j + 1];
        }
        elements[j] = null;
        size--;
        return true;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        Object ans = elements[index];
        elements[index] = element;
        return (E) ans;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }



    @Override
    public void clear() {
        elements = new Object[elements.length];
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])){
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(elements[i])){
                return i;
            }
        }
        return -1;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
