package by.it.group510902.tereshkin.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    // единый сентинел: NIL — это Node, всегда чёрный
    private static final Node NIL;
    static {
        NIL = new Node(null, null, BLACK);
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
    }

    private Node root = NIL;
    private int size;

    private String oldValue;
    private boolean added;
    private boolean removed;


    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key");
        oldValue = null;
        added = false;

        Node z = new Node(key, value, RED);
        z.left = NIL;
        z.right = NIL;
        z.parent = NIL;

        Node y = NIL;
        Node x = root;
        while (x != NIL) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }

        z.parent = y;
        if (y == NIL) root = z;
        else if (key.compareTo(y.key) < 0) y.left = z;
        else y.right = z;

        size++;
        added = true;
        fixAfterAdding(z);
        return oldValue;
    }

    private void fixAfterAdding(Node z) {
        while (z.parent.color == RED) {
            Node p = z.parent;
            Node g = p.parent;

            if (p == g.left) {
                Node u = g.right;
                if (u.color == RED) {
                    p.color = BLACK;
                    u.color = BLACK;
                    g.color = RED;
                    z = g;
                } else {
                    if (z == p.right) {
                        z = p;
                        rotateLeft(z);
                        p = z.parent;
                        g = p.parent;
                    }
                    p.color = BLACK;
                    g.color = RED;
                    rotateRight(g);
                }
            } else {
                Node u = g.left;
                if (u.color == RED) {
                    p.color = BLACK;
                    u.color = BLACK;
                    g.color = RED;
                    z = g;
                } else {
                    if (z == p.left) {
                        z = p;
                        rotateRight(z);
                        p = z.parent;
                        g = p.parent;
                    }
                    p.color = BLACK;
                    g.color = RED;
                    rotateLeft(g);
                }
            }
        }
        root.color = BLACK;
    }


    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;

        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != NIL) y.right.parent = x;

        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;

        y.right = x;
        x.parent = y;
    }


    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        oldValue = null;
        removed = false;

        Node z = findNode((Integer) key);
        if (z == null) return null;

        oldValue = z.value;
        removed = true;
        deleteNode(z);
        size--;
        return oldValue;
    }

    private Node findNode(Integer key) {
        Node x = root;
        while (x != NIL) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x;
        }
        return null;
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;

        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == BLACK) {
            fixAfterDeletion(x);
        }
    }

    private void transplant(Node u, Node v) {
        if (u.parent == NIL) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    private Node minimum(Node x) {
        while (x.left != NIL) x = x.left;
        return x;
    }

    private Node maximum(Node x) {
        while (x.right != NIL) x = x.right;
        return x;
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    // ====================== MAP ======================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == NIL) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    // ====================== SORTED MAP ======================

    @Override
    public Integer firstKey() {
        if (root == NIL) throw new NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        return maximum(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap map = new MyRbMap();
        headMap(root, toKey, map);
        return map;
    }

    private void headMap(Node node, Integer toKey, MyRbMap map) {
        if (node == NIL) return;
        if (node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
            headMap(node.left, toKey, map);
            headMap(node.right, toKey, map);
        } else {
            headMap(node.left, toKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap map = new MyRbMap();
        tailMap(root, fromKey, map);
        return map;
    }

    private void tailMap(Node node, Integer fromKey, MyRbMap map) {
        if (node == NIL) return;
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
            tailMap(node.left, fromKey, map);
            tailMap(node.right, fromKey, map);
        } else {
            tailMap(node.right, fromKey, map);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap map = new MyRbMap();
        subMap(root, fromKey, toKey, map);
        return map;
    }

    private void subMap(Node node, Integer fromKey, Integer toKey, MyRbMap map) {
        if (node == NIL) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
        }
        if (node.key.compareTo(fromKey) > 0) subMap(node.left, fromKey, toKey, map);
        if (node.key.compareTo(toKey) < 0) subMap(node.right, fromKey, toKey, map);
    }

    @Override
    public java.util.Comparator<? super Integer> comparator() {
        return null;
    }

    // ====================== VIEWS ======================

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean[] first = {true};
        inOrder(root, sb, first);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb, boolean[] first) {
        if (node == NIL) return;
        inOrder(node.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        first[0] = false;
        inOrder(node.right, sb, first);
    }
}