package com.core.cache;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LRUCacheProviderTest {

    @Test
    public void testGetInstanceShouldReturnLRUCache() {
        assertTrue(new LRUCacheProvider<>().getInstance() instanceof LRUCache);
    }

}