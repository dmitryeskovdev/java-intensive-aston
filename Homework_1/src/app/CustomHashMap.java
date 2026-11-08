package app;

import java.util.Objects;

public class CustomHashMap<K, V> {

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;

    public CustomHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        h ^= (h >>> 16);
        return Math.abs(h % table.length);
    }

    public V put(K key, V value) {
        int index = hash(key);
        Node<K, V> head = table[index];

        Node<K, V> current = head;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, head);
        table[index] = newNode;
        size++;

        if (size >= threshold) {
            resize();
        }

        return null;
    }

    public V get(Object key) {
        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(Object key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        Node<K, V> previous = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                V removedValue = current.value;

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;
                return removedValue;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);
        size = 0;

        for (Node<K, V> head : oldTable) {
            Node<K, V> current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    public boolean containsKey(Object key) {
        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public boolean containsValue(Object value) {
        for (Node<K, V> head : table) {
            Node<K, V> current = head;

            while (current != null) {
                if (Objects.equals(current.value, value)) {
                    return true;
                }
                current = current.next;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Node<K, V> head : table) {
            Node<K, V> current = head;

            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }

                sb.append(current.key).append("=").append(current.value);

                first = false;

                current = current.next;
            }
        }

        sb.append("}");
        return sb.toString();
    }
}
