package by.it.group510902.tereshkin.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedHashSet<E> implements Collection<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> before;
        Node<E> after;     // order list

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
            this.before = null;
            this.after = null;
        }
    }

    private int size = 0;
    private static final int DEF_CAPACITY = 8;

    private Node<E>[] elements = new Node[DEF_CAPACITY];

    private Node<E> head;   // first in insertion order
    private Node<E> tail;   // last in insertion order

    private static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    private int hash(Object o) {
        return (o.hashCode() & 0x7fffffff) % elements.length;
    }


    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < elements.length; i++) elements[i] = null;
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(E o) {
        int h = hash(o);

        // 1. dedup: scan bucket chain
        for (Node<E> c = elements[h]; c != null; c = c.next) {
            if (eq(c.item, o)) return false;
        }

        // 2. create node
        Node<E> newNode = new Node<>(o, elements[h]);

        // 3. prepend to bucket chain
        elements[h] = newNode;

        // 4. append to order list
        if (head == null) {          // first element
            head = newNode;
            tail = newNode;
        } else {
            tail.after = newNode;
            newNode.before = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    public boolean remove(Object o) {
        int h = hash(o);

        // 1. find in bucket chain, carrying predecessor
        Node<E> cur = elements[h];
        Node<E> prev = null;
        while (cur != null && !eq(cur.item, o)) {
            prev = cur;
            cur = cur.next;
        }
        if (cur == null) return false;

        // 2. unlink from bucket chain
        if (prev == null) elements[h] = cur.next;
        else              prev.next = cur.next;

        // 3. unlink from order list
        if (cur.before != null) cur.before.after = cur.after;
        else                    head = cur.after;
        if (cur.after != null)  cur.after.before = cur.before;
        else                    tail = cur.before;

        size--;
        return true;
    }

    public boolean contains(Object o) {
        for (Node<E> c = elements[hash(o)]; c != null; c = c.next) {
            if (eq(c.item, o)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> c = head; c != null; c = c.after) {
            if (!first) sb.append(", ");
            sb.append(c.item);
            first = false;
        }
        return sb.append("]").toString();
    }

    // ---------------------------------------------------------- Collection

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> nextNode = head;
            @Override public boolean hasNext() { return nextNode != null; }
            @Override public E next() {
                if (nextNode == null) throw new NoSuchElementException();
                E value = nextNode.item;
                nextNode = nextNode.after;
                return value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] ans = new Object[size];
        int i = 0;
        for (Node<E> c = head; c != null; c = c.after) ans[i++] = c.item;
        return ans;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> c = head; c != null; c = c.after) a[i++] = (T) c.item;
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
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.after;         // save before unlinking
            if (!c.contains(cur.item)) {
                remove(cur.item);             // unlinks from bucket + order
                changed = true;
            }
            cur = next;
        }
        return changed;
    }
}