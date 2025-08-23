package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    @Test
    public void decodeBoundary_withHighPrecisionGeohashNearOrigin_returnsCorrectRectangle() {
        // ARRANGE
        // A 12-character geohash represents a very small area. This specific geohash
        // is located adjacent to the prime meridian and equator.
        final String highPrecisionGeohash = "kpbpbpbpbpbp";
        final SpatialContext geoContext = SpatialContext.GEO;

        // The expected boundaries for the geohash "kpbpbpbpbpbp".
        // A 12-character geohash cell has a width of (360 / 2^30) degrees and a
        // height of (180 / 2^30) degrees.
        final double expectedMinX = 0.0;
        final double expectedMaxX = 3.3527612686157227E-7;
        final double expectedMinY = -1.6763806343078613E-7;
        final double expectedMaxY = 0.0;

        // Use a small delta for assertions involving high-precision floating-point numbers.
        final double delta = 1e-9;

        // ACT
        Rectangle decodedBounds = GeohashUtils.decodeBoundary(highPrecisionGeohash, geoContext);

        // ASSERT
        // Verify that all four coordinates of the decoded bounding box match the expected values.
        assertEquals("Min X (longitude)", expectedMinX, decodedBounds.getMinX(), delta);
        assertEquals("Max X (longitude)", expectedMaxX, decodedBounds.getMaxX(), delta);
        assertEquals("Min Y (latitude)", expectedMinY, decodedBounds.getMinY(), delta);
        assertEquals("Max Y (latitude)", expectedMaxY, decodedBounds.getMaxY(), delta);
    }
}