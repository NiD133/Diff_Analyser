package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Tests that the decode method correctly handles a geohash with a precision
     * far exceeding the typical maximum. The long tail of '0's in the geohash
     * refines the location to the southernmost edge of its bounding box, which
     * corresponds to the South Pole (-90 degrees latitude).
     */
    @Test
    public void decode_withOverlyPreciseGeohashForSouthPole_returnsCorrectPoint() {
        // Arrange
        // This geohash is intentionally much longer than the standard maximum precision.
        final String overlyPreciseGeohash = "h0pb421bn842p8h85bj0hbp000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        final SpatialContext spatialContext = SpatialContext.GEO;

        final double expectedLongitude = 11.0;
        final double expectedLatitude = -90.0; // South Pole
        final double delta = 0.01;

        // Act
        Point decodedPoint = GeohashUtils.decode(overlyPreciseGeohash, spatialContext);

        // Assert
        assertEquals("Longitude should be decoded correctly", expectedLongitude, decodedPoint.getX(), delta);
        assertEquals("Latitude should be at the South Pole", expectedLatitude, decodedPoint.getLat(), delta);
    }
}