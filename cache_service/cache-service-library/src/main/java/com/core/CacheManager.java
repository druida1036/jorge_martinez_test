package com.core;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.core.cache.CacheProvider;
import com.core.cache.LRUCacheProvider;
import com.core.server.ClosestByDistanceServerFinder;
import com.core.server.ServerContentCacheDecorator;
import com.core.server.ServerFinder;
import com.core.server.ServerContent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CacheManager<K, V> {
    private static final Logger logger = LogManager.getLogger(CacheManager.class);
    private static final String SERVERS_EMPTY_ERROR_MESSAGE = "Servers can not be empty or null";

    private final Set<ServerContent<K, V>> servers;
    private final ServerFinder<K, V> serverFinder;

    public CacheManager(Set<ServerContent<K, V>> servers, ServerFinder<K, V> serverFinder,
        CacheProvider<K, V> cacheProvider) {
        this.serverFinder = serverFinder;
        validateServers(servers);

        this.servers = servers.stream()
            .map(server -> new ServerContentCacheDecorator<>(server, cacheProvider.getInstance()))
            .collect(Collectors.toSet());
    }

    public void write(K key, V value) {
        servers.parallelStream().forEach(s -> s.write(key, value));
    }

    public Optional<V> read(final K key, final Map<String, Object> requestAttributes) {
        logger.info(String.format(
            "Searching for the closest server available with requestAttributes [%s].", requestAttributes));
        final var server = serverFinder.find(servers, requestAttributes);
        return server.map(s -> s.read(key)).orElseThrow(() -> {
            throw new RuntimeException("There is not server available to resolve the request");
        });
    }

    private void validateServers(Set<ServerContent<K, V>> servers) {
        if (servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException(SERVERS_EMPTY_ERROR_MESSAGE);
        }
    }

    public static class Builder<K, V> {

        private Set<ServerContent<K, V>> serverContents;
        private ServerFinder<K, V> serverFinder;
        private CacheProvider<K, V> cacheProvider;

        public Builder() {
        }

        public Builder<K, V> servers(Set<ServerContent<K, V>> servers) {
            this.serverContents = servers;
            return this;
        }

        public Builder<K, V> serverFinder(ServerFinder<K, V> serverFinder) {
            this.serverFinder = serverFinder;
            return this;
        }

        public Builder<K, V> cacheProvider(CacheProvider<K, V> cacheProvider) {
            this.cacheProvider = cacheProvider;
            return this;
        }

        public CacheManager<K, V> build() {
            return new CacheManager<>(serverContents, serverFinder, cacheProvider);
        }

        public CacheManager<K, V> onDefaults(Set<ServerContent<K, V>> servers) {
            return new CacheManager<>(servers, new ClosestByDistanceServerFinder<>(), new LRUCacheProvider<>());
        }

    }
}
