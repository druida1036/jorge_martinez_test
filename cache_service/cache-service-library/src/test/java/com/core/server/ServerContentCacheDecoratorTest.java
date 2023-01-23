package com.core.server;

import java.util.Optional;

import com.core.cache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ServerContentCacheDecoratorTest {

    @Mock
    private ServerContent<String, String> serverContent;

    @Mock
    private Cache<String, String> cache;


    @Test
    public void testServerContentCacheDecoratorWhenServerContentIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new ServerContentCacheDecorator<>(null, cache));

        assertEquals("Server can not be empty or null", exception.getMessage());
    }

    @Test
    public void testServerContentCacheDecoratorWhenCacheIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new ServerContentCacheDecorator<>(serverContent, null));

        assertEquals("Cache can not be empty or null", exception.getMessage());
    }

    @Test
    public void testWriteShouldCallInOrder() {
        InOrder inOrder = Mockito.inOrder(serverContent, cache);
        ServerContentCacheDecorator<String, String> serverContentCacheDecorator =
            new ServerContentCacheDecorator<>(serverContent, cache);
        String key = "key";
        String value = "value";

        serverContentCacheDecorator.write(key, value);

        inOrder.verify(serverContent).write(key, value);
        inOrder.verify(cache).write(key, value);
    }

    @Test
    public void testReadShouldCallInOrder() {
        InOrder inOrder = Mockito.inOrder(serverContent, cache);
        ServerContentCacheDecorator<String, String> serverContentCacheDecorator =
            new ServerContentCacheDecorator<>(serverContent, cache);
        String key = "key";

        serverContentCacheDecorator.read(key);

        inOrder.verify(cache).read(key);
        inOrder.verify(serverContent).read(key);
    }

    @Test
    public void testReadWhenValueInCashShouldNotCallServerContent() {
        ServerContentCacheDecorator<String, String> serverContentCacheDecorator =
            new ServerContentCacheDecorator<>(serverContent, cache);
        String key = "key";

        Mockito.when(cache.read(key)).thenReturn(Optional.of("value"));

        serverContentCacheDecorator.read(key);

        verify(cache).read(key);
        verifyNoInteractions(serverContent);
    }

    @Test
    public void testIsHealthyShouldSuccess() {
        ServerContentCacheDecorator<String, String> serverContentCacheDecorator =
            new ServerContentCacheDecorator<>(serverContent, cache);

        serverContentCacheDecorator.isHealthy();

        verify(serverContent).isHealthy();
    }

    @Test
    public void testGetAttributesShouldSuccess() {
        ServerContentCacheDecorator<String, String> serverContentCacheDecorator =
            new ServerContentCacheDecorator<>(serverContent, cache);

        serverContentCacheDecorator.getAttributes();

        verify(serverContent).getAttributes();
    }
}