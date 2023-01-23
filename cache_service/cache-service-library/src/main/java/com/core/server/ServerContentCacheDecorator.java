package com.core.server;

import java.util.Optional;

import com.core.cache.Cache;
import com.core.retry.RetryUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ServerContentCacheDecorator<K, V> extends ServerContentDecorator<K, V> {
    private static final Logger logger = LogManager.getLogger(ServerContentCacheDecorator.class);
    private static final String CACHE_EMPTY_ERROR_MESSAGE = "Cache can not be empty or null";
    private final Cache<K, V> cache;

    public ServerContentCacheDecorator(final ServerContent<K, V> serverContent, final Cache<K, V> cache) {
        super(serverContent);
        if (cache == null) {
            throw new IllegalArgumentException(CACHE_EMPTY_ERROR_MESSAGE);
        }
        this.cache = cache;
    }

    @Override
    public V write(final K key, final V value) {
        writeOrRetry(key, value);
        return cache.write(key, value);
    }

    @Override
    public Optional<V> read(final K key) {
        final Optional<V> value = cache.read(key);
        if (value.isPresent()) {
            logger.info(String.format("Reading value cached from the server [%s].", serverContent));
            return value;
        }
        return readOrRetry(key);
    }

    @Override
    public String toString() {
        return "ServerContentCacheDecorator{" + ", serverContent=" + serverContent + '}';
    }

    private void writeOrRetry(K key, V value) {
        RetryUtils.getInstance().executeRunnable(() -> {
            logger.info(String.format("Writing value into the cache for server [%s] ", serverContent));
            serverContent.write(key, value);
        });
    }

    private Optional<V> readOrRetry(K key) {
        return RetryUtils.getInstance().executeSupplier(() -> serverContent.read(key));
    }

}
