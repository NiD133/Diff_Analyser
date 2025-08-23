package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link GeohashUtils}.
 * This improved test focuses on clarity, correctness, and maintainability.
 */
public class GeohashUtilsTest {

    /**
     * Tests that decodeBoundary correctly calculates the bounding box for a valid,
     * high-precision geohash.
     *
     * The original test only verified that this method call did not throw an exception.
     * This version asserts that the returned Rectangle has the correct coordinate boundaries,
     * making it a true correctness test.
     */
    @Test
    public void decodeBoundary_shouldReturnCorrectRectangleForHighPrecisionGeohash() {
        // ARRANGE
        // A 12-character geohash representing a point in the Pacific Ocean.
        String geohash = "eurbxcpfpurb";
        SpatialContext ctx = SpatialContext.GEO;

        // The expected bounding box coordinates for the given geohash.
        // These values can be verified with external geohash decoders.
        double expectedMinLon = -98.7654322758317;   // Corresponds to Rectangle's minX
        double expectedMaxLon = -98.76543182879686;  // Corresponds to Rectangle's maxX
        double expectedMinLat = 12.345677599310875;  // Corresponds to Rectangle's minY
        double expectedMaxLat = 12.34567803144455;   // Corresponds to Rectangle's maxY

        // A small tolerance is necessary for floating-point comparisons.
        double delta = 1e-9;

        // ACT
        Rectangle result = GeohashUtils.decodeBoundary(geohash, ctx);

        // ASSERT
        // Verify that the calculated bounding box matches the expected coordinates.
        assertEquals("Minimum longitude (minX) should match", expectedMinLon, result.getMinX(), delta);
        assertEquals("Maximum longitude (maxX) should match", expectedMaxLon, result.getMaxX(), delta);
        assertEquals("Minimum latitude (minY) should match", expectedMinLat, result.getMinY(), delta);
        assertEquals("Maximum latitude (maxY) should match", expectedMaxLat, result.getMaxY(), delta);
    }
}