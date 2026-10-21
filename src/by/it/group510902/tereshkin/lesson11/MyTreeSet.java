package by.it.group510902.tereshkin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyTreeSet<E extends Comparable<E>> implements Collection<E> {

    private int size = 0;
    private static final int DEF_CAPACITY = 8;

    private E[] elements = (E[]) new Comparable[DEF_CAPACITY];

    private int cmp(Object a, Object b) {
        return ((Comparable<Object>) a).compareTo(b);
    }

    private int indexOf(Object o) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int c = cmp(o, elements[mid]);
            if (c == 0) return mid;
            if (c < 0)  hi = mid - 1;
            else        lo = mid + 1;
        }
        return -(lo) - 1;
    }

    private void ensureCapacity(int needed) {
        if (needed <= elements.length) return;
        int newCap = elements.length * 2;
        while (newCap < needed) newCap *= 2;
        E[] bigger = (E[]) new Comparable[newCap];
        for (int i = 0; i < size; i++) bigger[i] = elements[i];
        elements = bigger;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    public boolean add(E o) {
        int i = indexOf(o);
        if (i >= 0) return false;

        int pos = -i - 1;
        ensureCapacity(size + 1);

        for (int k = size; k > pos; k--) {
            elements[k] = elements[k - 1];
        }
        elements[pos] = o;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) return false;

        for (int k = i; k < size - 1; k++) {
            elements[k] = elements[k + 1];
        }
        elements[size - 1] = null;
        size--;
        return true;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int idx = 0;
            @Override public boolean hasNext() { return idx < size; }
            @Override public E next() {
                if (idx >= size) throw new NoSuchElementException();
                return elements[idx++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] ans = new Object[size];
        for (int i = 0; i < size; i++) ans[i] = elements[i];
        return ans;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) a[i] = (T) elements[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) changed |= add(e);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) changed |= remove(o);
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        int i = 0;
        while (i < size) {
            if (!c.contains(elements[i])) {
                for (int k = i; k < size - 1; k++) elements[k] = elements[k + 1];
                elements[size - 1] = null;
                size--;
                changed = true;
            } else {
                i++;
            }
        }
        return changed;
    }
}