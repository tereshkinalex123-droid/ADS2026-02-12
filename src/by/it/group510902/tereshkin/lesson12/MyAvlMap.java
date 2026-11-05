package by.it.group510902.tereshkin.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    private String oldValue;
    private boolean added;
    private boolean removed;

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
        return find(root, (Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (eq(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find(root, (Integer) key);
        return node == null ? null : node.value;
    }

    private Node find(Node node, Integer key) {
        if (node == null){
            return node;
        }

        if (key > node.key){
            return find(node.right, key);
        } else if (key < node.key){
            return find(node.left, key);
        } else {
            return node;
        }
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key");
        oldValue = null;
        added = false;
        root = put(root, key, value);
        if (added) size++;
        return oldValue;
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null){
            added = true;
            node = new Node(key, value);
            return node;
        }

        if (key > node.key){
            node.right = put(node.right, key, value);
        } else if (key < node.key){
            node.left = put(node.left, key, value);
        } else {
            oldValue = node.value;
            node.value = value;
        }

        return rebalance(node);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        oldValue = null;
        removed = false;
        root = remove(root, (Integer) key);
        if (removed) size--;
        return oldValue;
    }

    private Node remove(Node node, Integer key) {
        if (node == null){
            return node;
        }

        if (key > node.key){
            node.right = remove(node.right, key);
        } else if (key < node.key){
            node.left = remove(node.left, key);
        } else {
            oldValue = node.value;
            removed = true;

            if (node.left == null){ return rebalance(node.right);}
            if (node.right == null){ return rebalance(node.left);}

            Node min = min(node.right);
            node.value = min.value;
            node.key = min.key;
            node.right = removeMin(node.right);
            return rebalance(node);
        }

        return rebalance(node);
    }

    private Node min(Node node) {
        while (node.left != null){
            node = node.left;
        }
        return node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        } else {
            node.left = removeMin(node.left);
        }
        return rebalance(node);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        appendInOrder(root, sb, new boolean[]{true});
        sb.append("}");
        return sb.toString();
    }

    private void appendInOrder(Node node, StringBuilder sb, boolean[] first) {
        if (node == null) return;

        appendInOrder(node.left, sb, first);

        if (!first[0]) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        first[0] = false;

        appendInOrder(node.right, sb, first);
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + max(height(node.left), height(node.right));
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private int balance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rebalance(Node node) {
        if (node == null) return null;
        updateHeight(node);
        int b = balance(node);

        if (b > 1) {
            if (balance(node.left) < 0){
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        } else if (b < -1) {
            if (balance(node.right) > 0){
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node rotateRight(Node y) {
        Node node = y.left;
        y.left = node.right;
        node.right = y;
        updateHeight(node.right);
        updateHeight(node);
        return node;
    }

    private Node rotateLeft(Node x) {
        Node node = x.right;
        x.right = node.left;
        node.left = x;
        updateHeight(node.left);
        updateHeight(node);
        return node;
    }
}