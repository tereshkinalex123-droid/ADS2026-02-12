package by.it.group510902.tereshkin.lesson11;

import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Collection<E> {

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private int size = 0;
    private static final int DEF_CAPACITY = 8;

    private Node<E>[] elements = new Node[DEF_CAPACITY];

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
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(E o) {
        int h = hash(o);
        for (Node<E> c = elements[h]; c != null; c = c.next) {
            if (eq(c.item, o)) return false;
        }
        elements[h] = new Node<>(o, elements[h]);
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int h = hash(o);
        Node<E> cur = elements[h];
        Node<E> prev = null;
        while (cur != null && !eq(cur.item, o)) {
            prev = cur;
            cur = cur.next;
        }
        if (cur == null) return false;
        if (prev == null) {
            elements[h] = cur.next;
        } else {
            prev.next = cur.next;
        }
        size--;
        return true;
    }

    public boolean contains(Object o) {
        for (Node<E> c = elements[hash(o)]; c != null; c = c.next) {
            if (eq(c.item, o)) return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : elements) {
            for (Node<E> c = head; c != null; c = c.next) {
                if (!first) sb.append(", ");
                sb.append(c.item);
                first = false;
            }
        }
        return sb.append("]").toString();
    }

    public Iterator<E> iterator(){
        return null;
    }

    public Object[] toArray() { /* walk buckets */ return null;}
    public <T> T[] toArray(T[] a) { /* walk buckets */return null; }
    public boolean containsAll(Collection<?> c) { for (Object o : c) if (!contains(o)) return false; return true; }
    public boolean addAll(Collection<? extends E> c) { boolean ch = false; for (E e : c) ch |= add(e); return ch; }
    public boolean removeAll(Collection<?> c) { boolean ch = false; for (Object o : c) ch |= remove(o); return ch; }
    public boolean retainAll(Collection<?> c) { /* walk and remove elements not in c */return true; }
}