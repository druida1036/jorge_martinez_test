package com.core.server;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ServerFinder<K, V> {

    Optional<ServerContent<K, V>> find(Set<ServerContent<K, V>> serverContents, Map<String, Object> source);
}
