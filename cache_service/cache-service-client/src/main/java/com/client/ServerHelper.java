package com.client;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.core.server.ServerContent;
import com.core.server.ServerContentInMemory;

public final class ServerHelper {

    public static final Map<String, Object> TORONTO_LOCATION = Map.of("LATITUDE", 43.65, "LONGITUDE", 30);
    public static final Map<String, Object> MONTREAL_LOCATION = Map.of("LATITUDE", 45.50, "LONGITUDE", 73.56);
    public static final Map<String, Object> NEW_YORK_LOCATION = Map.of("LATITUDE", 40.71, "LONGITUDE", 74);

    public static Set<ServerContent<String, String>> getServerContents() {
        Set<ServerContent<String, String>> servers = new HashSet<>();
        servers.add(new ServerContentInMemory<>("server toronto", TORONTO_LOCATION));
        servers.add(new ServerContentInMemory<>("server montreal", MONTREAL_LOCATION));
        servers.add(new ServerContentInMemory<>("server new york", NEW_YORK_LOCATION));
        return servers;
    }
}
