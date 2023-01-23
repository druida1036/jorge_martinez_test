package com.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.core.CacheManager.Builder;
import com.core.cache.Cache;
import com.core.cache.CacheProvider;
import com.core.server.ServerContentCacheDecorator;
import com.core.server.ServerFinder;
import com.core.server.ServerContent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CacheManagerTest {

    public static final String KEY = "1";
    public static final String VALUE = "test";
    @Mock
    private Cache<String, String> cache;
    @Mock
    private ServerContent<String, String> serverContent1;
    @Mock
    private ServerContent<String, String> serverContent2;

    @Mock
    private ServerFinder<String, String> serverFinder;

    @Mock
    private CacheProvider<String, String> cacheProvider;

    @Captor
    private ArgumentCaptor<Set<ServerContent<String, String>>> argumentCaptorServers;

    private CacheManager<String, String> cacheManager;

    @Before
    public void setUp() {
        Mockito.when(cacheProvider.getInstance()).thenReturn(cache);
        Set<ServerContent<String, String>> servers = Set.of(serverContent1, serverContent2);

        cacheManager = new Builder<String, String>()
            .servers(servers)
            .serverFinder(serverFinder)
            .cacheProvider(cacheProvider)
            .build();
    }

    @Test
    public void testWriteShouldSuccess() {
        cacheManager.write(KEY, VALUE);

        verify(cacheProvider, times(2)).getInstance();
        verify(serverContent1).write("1", "test");
        verify(serverContent2).write("1", "test");
        verify(cache, times(2)).write("1", "test");
    }

    @Test
    public void testReadShouldReadFromServer() {
        HashMap<String, Object> attributes = new HashMap<>();

        Mockito.doReturn(Optional.of(serverContent1)).when(serverFinder).find(anySet(), anyMap());

        cacheManager.read(KEY, attributes);

        verify(serverFinder).find(argumentCaptorServers.capture(), anyMap());
        argumentCaptorServers.getValue()
            .forEach(serverContents -> assertTrue(serverContents instanceof ServerContentCacheDecorator));
        verify(serverContent1).read(KEY);
    }

    @Test
    public void testReadWhenNoServersAvailableShouldFail() {
        HashMap<String, Object> attributes = new HashMap<>();

        Mockito.doReturn(Optional.empty()).when(serverFinder).find(anySet(), anyMap());

        RuntimeException exception = assertThrows(
            RuntimeException.class, () -> cacheManager.read(KEY, attributes));

        assertEquals("There is not server available to resolve the request", exception.getMessage());

    }

    @Test
    public void testReadWhenServerNullShouldFail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new Builder<>().onDefaults(null));

        assertEquals("Servers can not be empty or null", exception.getMessage());
    }

    @Test
    public void testReadWhenServerEmptyShouldFail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new Builder<>().onDefaults(new HashSet<>()));

        assertEquals("Servers can not be empty or null", exception.getMessage());
    }

    @Test
    public void testOnDefaultsShouldCreate() {
        assertNotNull(new Builder<String, String>().onDefaults(Set.of(serverContent1)));
    }
}