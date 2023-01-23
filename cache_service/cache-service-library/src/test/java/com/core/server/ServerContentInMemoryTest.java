package com.core.server;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import static com.core.constants.AttributeConstants.LONGITUDE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ServerContentInMemoryTest {

    public static final String VALUE = "value";
    public static final String KEY = "key";
    private ServerContentInMemory<String, String> serverContentInMemory;

    @Before
    public void setUp() {
        serverContentInMemory = new ServerContentInMemory<>("test server", Map.of(LONGITUDE, 1));
    }

    @Test
    public void testServerContentInMemoryWhenNameIsNullShouldFail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new ServerContentInMemory<>(null, Map.of(LONGITUDE, 1)));

        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void testServerContentInMemoryWhenNameIsEmptyShouldFail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new ServerContentInMemory<>("", Map.of(LONGITUDE, 1)));

        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void testServerContentInMemoryWhenAttributesAreNullShouldFail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new ServerContentInMemory<>("test", null));

        assertEquals("Attributes can not be empty", exception.getMessage());
    }

    @Test
    public void testServerContentInMemoryWhenAttributesAreEmptyShouldFail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> new ServerContentInMemory<>("test", Collections.emptyMap()));

        assertEquals("Attributes can not be empty", exception.getMessage());
    }

    @Test
    public void testWriteShouldSuccess() {
        serverContentInMemory.write(KEY, VALUE);
        assertEquals(VALUE, serverContentInMemory.write(KEY, VALUE));
    }

    @Test
    public void testReadWithExistingKeyShouldSuccess() {
        serverContentInMemory.write(KEY, VALUE);
        assertEquals(Optional.of(VALUE), serverContentInMemory.read(KEY));
    }

    @Test
    public void testReadWithNewKeyShouldSuccess() {
        assertEquals(Optional.empty(), serverContentInMemory.read("newKey"));
    }

    @Test
    public void testIsHealthy() {
        assertTrue(serverContentInMemory.isHealthy());
    }

    @Test
    public void testGetAttributes() {
        assertEquals(1, serverContentInMemory.getAttributes().size());
    }

    @Test
    public void testToString() {
        assertEquals("ServerContentInMemory{name='test server', attributes={LONGITUDE=1}}",
            serverContentInMemory.toString());
    }

}