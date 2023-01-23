package com.client;

import com.core.CacheManager;
import com.core.CacheManager.Builder;

public class DemoContentManager {

    public static void main(String[] args) {

        CacheManager<String, String> cacheManager = new Builder<String, String>().onDefaults(
            ServerHelper.getServerContents());
        // use case
        cacheManager.write("key1", "value");
        cacheManager.read("key1", ServerHelper.MONTREAL_LOCATION);
    }


}