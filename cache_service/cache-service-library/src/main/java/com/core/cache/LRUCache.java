package com.core.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final HashMap<K, V> data;
    private final LinkedList<K> keys;
    private final ReentrantReadWriteLock lock;

    public LRUCache(final int capacity) {
        validateCapacity(capacity);
        this.capacity = capacity;
        this.data = new HashMap<>();
        this.keys = new LinkedList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public V write(final K key, final V value) {
        lock.writeLock().lock();
        try {
            if (data.containsKey(key)) {
                data.put(key, value);
                // remove current
                keys.remove(key);
                // move it to head
                keys.addFirst(key);
                return value;
            }

            if (data.size() == capacity) {
                // remove the least recently used element
                data.remove(keys.removeLast());
            }

            data.put(key, value);
            // move it to head
            keys.addFirst(key);
            return value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<V> read(final K key) {
        lock.readLock().lock();
        try {
            if (!data.containsKey(key)) {
                return Optional.empty();
            }
            keys.remove(key);
            keys.addFirst(key);
            return Optional.of(data.get(key));
        } finally {
            lock.readLock().unlock();
        }
    }

    private void validateCapacity(final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity cache can not be lower that 1");
        }
    }
}