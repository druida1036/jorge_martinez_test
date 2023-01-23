package com.core.cache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class LRUCacheTest {

    @Test
    public void testWriteNewKeysShouldSuccess() {
        LRUCache<String, String> lruCache = new LRUCache<>(2);
        lruCache.write("1", "test1");
        lruCache.write("2", "test2");

        assertEquals("test1", lruCache.read("1").get());
        assertEquals("test2", lruCache.read("2").get());
    }

    @Test
    public void testWriteSameKeyTwiceShouldAddedOnce() {
        LRUCache<String, String> lruCache = new LRUCache<>(2);
        lruCache.write("1", "oldest");
        lruCache.write("1", "latest");

        assertEquals("latest", lruCache.read("1").get());
        assertFalse(lruCache.read("2").isPresent());

    }

    @Test
    public void testWriteAddNewKeyWhenCacheIsFullShouldRemoveLeastRecentKey() {
        LRUCache<String, String> lruCache = new LRUCache<>(2);
        lruCache.write("1", "test1");
        lruCache.write("2", "test2");
        lruCache.write("3", "test3");
        assertFalse(lruCache.read("1").isPresent());
    }

    @Test
    public void testWriteAddKeysMultiThreadShouldNoTLostData() throws Exception {
        final int size = 50;

        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        Cache<Integer, String> cache = new LRUCache<>(size);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        try {
            IntStream.range(0, size).<Runnable>mapToObj(key -> () -> {
                cache.write(key, "value" + key);
                countDownLatch.countDown();
            }).forEach(executorService::submit);
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }
        IntStream.range(0, size).forEach(i -> assertEquals("value" + i, cache.read(i).get()));
    }

    @Test
    public void testCreationCacheWithCapacityLowerThatOneShouldFail() {

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new LRUCache<>(0));

        assertEquals("Capacity cache can not be lower that 1", exception.getMessage());

    }
}