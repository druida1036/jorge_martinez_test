package com.core.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ServerContentInMemory<K, V> implements ServerContent<K, V> {

    private static final Logger logger = LogManager.getLogger(ServerContentInMemory.class);
    private static final String NAME_EMPTY_ERROR_MESSAGE = "Name can not be empty";
    private static final String ATTRIBUTES_EMPTY_ERROR_MESSAGE = "Attributes can not be empty";
    private final String name;
    private final Map<String, Object> attributes;

    private final Map<K, V> data = new HashMap<>();

    public ServerContentInMemory(String name, Map<String, Object> attributes) {
        validateParameters(name, attributes);
        this.name = name;
        this.attributes = attributes;
    }

    @Override
    public V write(K key, V value) {
        logger.info(String.format("Server [%s] receive a request to write with key: [%s] and value: [%s]", name,
            key, value));
        return data.put(key, value);
    }

    @Override
    public Optional<V> read(K key) {
        logger.info(String.format("Reading from Server [%s] with key: [%s].", name, key));
        return Optional.ofNullable(data.get(key));
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "ServerContentInMemory{" + "name='" + name + '\'' + ", attributes=" + attributes + '}';
    }

    private void validateParameters(String name, Map<String, Object> attributes) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(NAME_EMPTY_ERROR_MESSAGE);
        }
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException(ATTRIBUTES_EMPTY_ERROR_MESSAGE);
        }
    }
}
