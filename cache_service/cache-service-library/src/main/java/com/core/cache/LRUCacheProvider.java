package com.core.cache;

public class LRUCacheProvider<K, V> implements CacheProvider<K, V> {
    @Override
    public Cache<K, V> getInstance() {
        return new LRUCache<>(10);
    }
}
