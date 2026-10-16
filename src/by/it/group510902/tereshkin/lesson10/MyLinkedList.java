package by.it.group510902.tereshkin.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Collection<E> {

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public void addFirst(E element) {
        Node<E> h = head;
        Node<E> newNode = new Node<>(null, element, h);
        head = newNode;
        if (h == null) tail = newNode;
        else h.prev = newNode;
        size++;
    }

    public void addLast(E element) {
        Node<E> t = tail;
        Node<E> newNode = new Node<>(t, element, null);
        tail = newNode;
        if (t == null) head = newNode;
        else t.next = newNode;
        size++;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (head == null) return null;
        E val = head.item;
        Node<E> next = head.next;
        head.item = null;
        head.next = null;
        head = next;
        if (next == null) tail = null;
        else next.prev = null;
        size--;
        return val;
    }

    public E pollLast() {
        if (tail == null) return null;
        E val = tail.item;
        Node<E> prev = tail.prev;
        tail.item = null;
        tail.prev = null;
        tail = prev;
        if (prev == null) head = null;
        else prev.next = null;
        size--;
        return val;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> cur;
        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        return unlink(cur);
    }

    @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    private E unlink(Node<E> node) {
        E val = node.item;
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev == null) head = next;
        else prev.next = next;
        if (next == null) tail = prev;
        else next.prev = prev;
        node.item = null;
        node.prev = null;
        node.next = null;
        size--;
        return val;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.item == null : o.equals(cur.item)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> next = head;

            @Override public boolean hasNext() { return next != null; }

            @Override public E next() {
                if (next == null) throw new NoSuchElementException();
                E v = next.item;
                next = next.next;
                return v;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> c = head; c != null; c = c.next) arr[i++] = c.item;
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> c = head; c != null; c = c.next) a[i++] = (T) c.item;
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
        for (E e : c) { addLast(e); changed = true; }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.item)) { unlink(cur); changed = true; }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.item)) { unlink(cur); changed = true; }
            cur = next;
        }
        return changed;
    }

    @Override
    public void clear() {
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            cur.item = null;
            cur.prev = null;
            cur.next = null;
            cur = next;
        }
        head = null;
        tail = null;
        size = 0;
    }
}