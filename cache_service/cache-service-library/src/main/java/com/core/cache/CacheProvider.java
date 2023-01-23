package com.core.cache;

public interface CacheProvider<K, V> {
    Cache<K, V> getInstance();
}
