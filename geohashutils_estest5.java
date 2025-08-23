package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link GeohashUtils}.
 * This class focuses on verifying the correctness of geohash decoding operations.
 */
public class GeohashUtilsTest {

    private static final double FLOATING_POINT_DELTA = 1e-12;

    /**
     * Verifies that {@link GeohashUtils#decodeBoundary(String, SpatialContext)}
     * correctly calculates the bounding box for a valid, high-precision geohash.
     */
    @Test
    public void decodeBoundary_withValidGeohash_returnsCorrectBoundingBox() {
        // Arrange
        // A 12-character geohash representing a small area in Antarctica.
        String geohash = "8h2081040h20";
        SpatialContext ctx = SpatialContext.GEO;

        // The expected bounding box coordinates for the given geohash. These values
        // are derived from the geohash algorithm's defined precision for 12 characters.
        double expectedMinLon = -135.00000008940697;
        double expectedMaxLon = -134.99999991059303;
        double expectedMinLat = -81.3281250372529;
        double expectedMaxLat = -81.3281249627471;

        // Act
        Rectangle result = GeohashUtils.decodeBoundary(geohash, ctx);

        // Assert
        // Verify that the decoded rectangle's coordinates match the expected values.
        assertEquals("Minimum longitude (minX) should match", expectedMinLon, result.getMinX(), FLOATING_POINT_DELTA);
        assertEquals("Maximum longitude (maxX) should match", expectedMaxLon, result.getMaxX(), FLOATING_POINT_DELTA);
        assertEquals("Minimum latitude (minY) should match", expectedMinLat, result.getMinY(), FLOATING_POINT_DELTA);
        assertEquals("Maximum latitude (maxY) should match", expectedMaxLat, result.getMaxY(), FLOATING_POINT_DELTA);
    }
}