package com.core.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.core.constants.AttributeConstants.LATITUDE;
import static com.core.constants.AttributeConstants.LONGITUDE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClosestByDistanceServerFinderTest {

    public static final Map<String, Object> TORONTO_LOCATION = Map.of("LATITUDE", 43.65, "LONGITUDE", 30);
    public static final Map<String, Object> MONTREAL_LOCATION = Map.of("LATITUDE", 90, "LONGITUDE", 91);
    public static final Map<String, Object> NEW_YORK_LOCATION = Map.of("LATITUDE", 90, "LONGITUDE", 91);
    @Mock
    private ServerContent<String, String> serverToronto;
    @Mock
    private ServerContent<String, String> serverMontreal;

    @Test
    public void testFindShouldReturnServer() {
        ClosestByDistanceServerFinder<String, String> serverFinder = new ClosestByDistanceServerFinder<>();

        when(serverToronto.isHealthy()).thenReturn(true);
        when(serverToronto.getAttributes()).thenReturn(TORONTO_LOCATION);

        when(serverMontreal.isHealthy()).thenReturn(true);
        when(serverMontreal.getAttributes()).thenReturn(MONTREAL_LOCATION);

        Optional<ServerContent<String, String>> serverFound = serverFinder.find(Set.of(serverToronto,
            serverMontreal), NEW_YORK_LOCATION);

        assertEquals(serverMontreal, serverFound.get());
    }

    @Test
    public void testFindWithUnhealthyServerShouldReturnEmpty() {
        ClosestByDistanceServerFinder<String, String> serverFinder = new ClosestByDistanceServerFinder<>();
        Map<String, Object> attributes = Map.of(LONGITUDE, 1, LATITUDE, 1);

        when(serverToronto.isHealthy()).thenReturn(false);

        Optional<ServerContent<String, String>> serverFound = serverFinder.find(Set.of(serverToronto), attributes);

        assertEquals(Optional.empty(), serverFound);
    }

    @Test
    public void testFindWhenAttributeIsInvalidShouldReturnEmpty() {
        ClosestByDistanceServerFinder<String, String> serverFinder = new ClosestByDistanceServerFinder<>();
        Map<String, Object> attributes = Map.of(LONGITUDE, 1, LATITUDE, "invalid");

        when(serverToronto.isHealthy()).thenReturn(true);
        when(serverToronto.getAttributes()).thenReturn(attributes);

        NumberFormatException exception = assertThrows(
            NumberFormatException.class, () -> serverFinder.find(Set.of(serverToronto), attributes));

        assertEquals("For input string: \"invalid\"", exception.getMessage());
    }

    @Test
    public void testFindWhenAttributeIsNullShouldReturnEmpty() {
        ClosestByDistanceServerFinder<String, String> serverFinder = new ClosestByDistanceServerFinder<>();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(LONGITUDE, 1);
        attributes.put(LATITUDE, null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> serverFinder.find(Set.of(serverToronto), attributes));

        assertEquals("Longitude and longitude are required to continue", exception.getMessage());
    }

    @Test
    public void testFindWhenSourceEmptyParametersShouldReturnEmpty() {
        ClosestByDistanceServerFinder<String, String> serverFinder = new ClosestByDistanceServerFinder<>();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> serverFinder.find(Set.of(serverToronto), Collections.emptyMap()));

        assertEquals("Longitude and longitude are required to continue", exception.getMessage());
    }

    @Test
    public void testFindWhenSourceNullParametersShouldReturnEmpty() {
        ClosestByDistanceServerFinder<String, String> serverFinder = new ClosestByDistanceServerFinder<>();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> serverFinder.find(Set.of(serverToronto), null));

        assertEquals("Longitude and longitude are required to continue", exception.getMessage());
    }

    @Test
    public void testFindWhenServerAttributesEmptyParametersShouldReturnEmpty() {
        ClosestByDistanceServerFinder<String, String> serverFinder = new ClosestByDistanceServerFinder<>();
        Map<String, Object> attributes = Map.of(LONGITUDE, 1, LATITUDE, 1);

        when(serverToronto.isHealthy()).thenReturn(true);
        when(serverToronto.getAttributes()).thenReturn(Map.of(LONGITUDE, 1));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> serverFinder.find(Set.of(serverToronto), attributes));

        assertEquals("Longitude and longitude are required to continue", exception.getMessage());
    }
}