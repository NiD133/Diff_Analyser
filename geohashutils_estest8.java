package org.locationtech.spatial4j.io;

import org.junit.Test;

/**
 * Unit tests for the {@link GeohashUtils} class.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that encodeLatLon() throws an IllegalArgumentException when the
     * specified precision is greater than the maximum allowed value. The valid
     * precision range is [1, GeohashUtils.MAX_PRECISION].
     */
    @Test(expected = IllegalArgumentException.class)
    public void encodeLatLon_withPrecisionAboveMax_shouldThrowIllegalArgumentException() {
        // Arrange: Define valid coordinates but a precision that is just outside the
        // upper bound of the valid range. GeohashUtils.MAX_PRECISION is 24.
        final double validLatitude = 40.7128;  // New York City
        final double validLongitude = -74.0060;
        final int invalidPrecision = GeohashUtils.MAX_PRECISION + 1;

        // Act & Assert: This call is expected to throw an IllegalArgumentException
        // because the precision is out of the allowed range. The invalid latitude
        // and longitude from the original test are not relevant, as the precision
        // is validated first.
        GeohashUtils.encodeLatLon(validLatitude, validLongitude, invalidPrecision);
    }
}