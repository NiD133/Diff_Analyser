package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Unit tests for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that the decode method rejects geohash strings containing characters
     * that are not part of the base-32 alphabet used by Geohash.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void decode_withInvalidCharacters_shouldThrowException() {
        // Arrange: Use a standard, readily available SpatialContext.
        final SpatialContext ctx = SpatialContext.GEO;

        // An invalid geohash string containing a hyphen ('-'), which is not a valid
        // geohash character. This makes the reason for the expected failure clear.
        final String invalidGeohash = "invalid-geohash";

        // Act & Assert:
        // The @Test(expected=...) annotation asserts that an
        // ArrayIndexOutOfBoundsException is thrown when the decode method
        // encounters an invalid character. This is cleaner than a try-catch block.
        GeohashUtils.decode(invalidGeohash, ctx);
    }
}