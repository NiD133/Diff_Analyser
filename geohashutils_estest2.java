package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Tests that encoding a location with a precision of 0 results in an empty string.
     * A geohash's precision corresponds to its string length, so zero precision
     * should logically yield a zero-length string.
     */
    @Test
    public void encodeLatLon_withZeroPrecision_shouldReturnEmptyString() {
        // Arrange
        final double latitude = 13.0;
        final double longitude = 13.0;
        final int precision = 0;
        final String expectedGeohash = "";

        // Act
        String actualGeohash = GeohashUtils.encodeLatLon(latitude, longitude, precision);

        // Assert
        assertEquals("Encoding with zero precision should produce an empty geohash.",
                expectedGeohash, actualGeohash);
    }
}