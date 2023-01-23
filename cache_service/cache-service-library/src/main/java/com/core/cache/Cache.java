package com.core.cache;

import java.util.Optional;

public interface Cache<K, V> {
    V write(K key, V value);

    Optional<V> read(K key);
}
