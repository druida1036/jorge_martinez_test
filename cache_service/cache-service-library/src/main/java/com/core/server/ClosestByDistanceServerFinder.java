package com.core.server;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static com.core.constants.AttributeConstants.LATITUDE;
import static com.core.constants.AttributeConstants.LONGITUDE;

public class ClosestByDistanceServerFinder<K, V> implements ServerFinder<K, V> {

    private static final Logger logger = LogManager.getLogger(ClosestByDistanceServerFinder.class);
    private static final String LONGITUDE_AND_LONGITUDE_REQUIRED = "Longitude and longitude are "
        + "required to continue";

    @Override
    public Optional<ServerContent<K, V>> find(
        final Set<ServerContent<K, V>> serverContents,
        final Map<String, Object> source
    ) {
        verifyMandatoryAttributes(source);
        double minimumDistance = Double.MAX_VALUE;
        ServerContent<K, V> neaerstServer = null;
        for (ServerContent<K, V> server : serverContents) {
            if (!server.isHealthy()) {
                break;
            }
            verifyMandatoryAttributes(server.getAttributes());
            double distanceCalculated = computeDistance(
                convertToDouble(source, LATITUDE),
                convertToDouble(source, LONGITUDE),
                convertToDouble(server.getAttributes(), LATITUDE),
                convertToDouble(server.getAttributes(), LONGITUDE));
            if (distanceCalculated < minimumDistance) {
                minimumDistance = distanceCalculated;
                neaerstServer = server;
            }
        }
        logger.info(String.format("Closest server found [%s]", neaerstServer));
        return Optional.ofNullable(neaerstServer);
    }

    private void verifyMandatoryAttributes(final Map<String, Object> source) {
        if (source == null) {
            throw new IllegalArgumentException(LONGITUDE_AND_LONGITUDE_REQUIRED);
        }

        verifyAttribute(source, LONGITUDE);
        verifyAttribute(source, LATITUDE);

    }

    private void verifyAttribute(final Map<String, Object> source, final String key) {
        if (!source.containsKey(key)) {
            throw new IllegalArgumentException(LONGITUDE_AND_LONGITUDE_REQUIRED);
        }

        if (source.get(key) == null) {
            throw new IllegalArgumentException(LONGITUDE_AND_LONGITUDE_REQUIRED);
        }
    }

    private double convertToDouble(
        final Map<String, Object> attributes,
        final String attribute
    ) {
        return Double.parseDouble(attributes.get(attribute).toString());
    }

    private double computeDistance(
        final double sourceLatitude,
        final double sourceLongitude,
        final double targetLatitude,
        final double targetLongitude
    ) {
        final int earthRadius = 6371;
        final double latDistance = Math.toRadians(targetLatitude - sourceLatitude);
        final double lonDistance = Math.toRadians(targetLongitude - sourceLongitude);
        final double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
            Math.cos(Math.toRadians(sourceLatitude)) * Math.cos(Math.toRadians(targetLatitude)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
