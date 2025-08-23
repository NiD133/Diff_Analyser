package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    @Test
    public void encodeLatLon_shouldGenerateCorrectGeohashForWesternHemisphere() {
        // Arrange
        double latitude = 42.6;    // A point in Spain
        double longitude = -5.6;
        String expectedGeohash = "ezs42e44yx96";

        // Act
        String actualGeohash = GeohashUtils.encodeLatLon(latitude, longitude);

        // Assert
        assertEquals(expectedGeohash, actualGeohash);
    }

    @Test
    public void encodeLatLon_shouldGenerateCorrectGeohashForEasternHemisphere() {
        // Arrange
        double latitude = 57.64911; // A point in Denmark
        double longitude = 10.40744;
        String expectedGeohash = "u4pruydqqvj8";

        // Act
        String actualGeohash = GeohashUtils.encodeLatLon(latitude, longitude);

        // Assert
        assertEquals(expectedGeohash, actualGeohash);
    }
}