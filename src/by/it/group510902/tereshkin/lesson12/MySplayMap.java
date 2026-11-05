package by.it.group510902.tereshkin.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    // ====================== SPLAY ======================

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else if ((x == p.left) == (p == g.left)) {
                // zig-zig: сначала дед, потом родитель
                if (x == p.left) {
                    rotateRight(g);
                    rotateRight(p);
                } else {
                    rotateLeft(g);
                    rotateLeft(p);
                }
            } else {
                // zig-zag: сначала родитель, потом дед
                if (x == p.left) {
                    rotateRight(p);
                    rotateLeft(g);
                } else {
                    rotateLeft(p);
                    rotateRight(g);
                }
            }
        }
        root = x;
    }

    private Node findNode(Integer key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                splay(cur);
                return cur;
            } else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        if (last != null) splay(last);
        return null;
    }

    // ====================== PUT ======================

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key");
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node cur = root;
        Node parent = null;
        while (cur != null) {
            parent = cur;
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            } else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }

        Node node = new Node(key, value);
        node.parent = parent;
        if (key.compareTo(parent.key) < 0) parent.left = node;
        else parent.right = node;
        size++;
        splay(node);
        return null;
    }

    // ====================== GET / CONTAINS ======================

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
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
        if (node == null) return false;
        if (value == null ? node.value == null : value.equals(node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    // ====================== REMOVE ======================

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);   // splay уже сделан
        if (node == null) return null;

        String old = node.value;

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            root.parent = null;
        } else {
            Node leftTree = node.left;
            Node rightTree = node.right;
            leftTree.parent = null;
            rightTree.parent = null;

            root = leftTree;
            Node maxLeft = leftTree;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft);          // maxLeft в корне левого поддерева

            root.right = rightTree;
            rightTree.parent = root;
        }

        size--;
        return old;
    }

    // ====================== SIZE / CLEAR / ISEMPTY ======================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    // ====================== FIRST / LAST ======================

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        splay(cur);
        return cur.key;
    }

    // ====================== HEAD / TAIL / SUB ======================

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap map = new MySplayMap();
        headMap(root, toKey, map);
        return map;
    }

    private void headMap(Node node, Integer toKey, MySplayMap map) {
        if (node == null) return;
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
        MySplayMap map = new MySplayMap();
        tailMap(root, fromKey, map);
        return map;
    }

    private void tailMap(Node node, Integer fromKey, MySplayMap map) {
        if (node == null) return;
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
        MySplayMap map = new MySplayMap();
        subMap(root, fromKey, toKey, map);
        return map;
    }

    private void subMap(Node node, Integer fromKey, Integer toKey, MySplayMap map) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0 && node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
        }
        if (node.key.compareTo(fromKey) > 0) subMap(node.left, fromKey, toKey, map);
        if (node.key.compareTo(toKey) < 0) subMap(node.right, fromKey, toKey, map);
    }

    // ====================== NAVIGABLE KEYS ======================

    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) < 0) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) <= 0) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) >= 0) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (cur.key.compareTo(key) > 0) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
    }

    // ====================== TO STRING ======================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean[] first = {true};
        inOrder(root, sb, first);
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;
        inOrder(node.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        first[0] = false;
        inOrder(node.right, sb, first);
    }

    // ====================== NAVIGABLE MAP (заглушки) ======================

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

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
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }
}