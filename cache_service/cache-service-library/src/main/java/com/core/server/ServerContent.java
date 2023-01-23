package com.core.server;

import java.util.Map;
import java.util.Optional;

public interface ServerContent<K, V> {
    V write(K key, V value);

    Optional<V> read(K key);

    boolean isHealthy();

    Map<String, Object> getAttributes();
}
