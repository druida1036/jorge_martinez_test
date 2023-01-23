package com.core.server;

import java.util.Map;

public abstract class ServerContentDecorator<K, V> implements ServerContent<K, V> {
    private static final String SERVER_EMPTY_ERROR_MESSAGE = "Server can not be empty or null";
    protected ServerContent<K, V> serverContent;

    public ServerContentDecorator(ServerContent<K, V> serverContent) {
        this.serverContent = serverContent;
        if (serverContent == null) {
            throw new IllegalArgumentException(SERVER_EMPTY_ERROR_MESSAGE);
        }
    }

    public boolean isHealthy() {
        return serverContent.isHealthy();
    }

    public Map<String, Object> getAttributes() {
        return serverContent.getAttributes();
    }


}
