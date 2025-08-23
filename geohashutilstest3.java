package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    private final SpatialContext ctx = SpatialContext.GEO;

    /**
     * Tests the round-trip encoding and decoding of coordinates.
     * <p>
     * This test verifies that after encoding a latitude and longitude into a geohash
     * and then decoding it back, the resulting coordinates are approximately equal
     * to the original values. Geohash conversion is inherently lossy, so we check
     * that the difference is within an acceptable margin of error.
     */
    @Test
    public void encodeThenDecode_shouldResultInSimilarCoordinates() {
        // Arrange
        double originalLatitude = 84.6;
        double originalLongitude = 10.5;
        double acceptableError = 0.00001D;

        // Act
        String geohash = GeohashUtils.encodeLatLon(originalLatitude, originalLongitude);
        Point decodedPoint = GeohashUtils.decode(geohash, ctx);

        // Assert
        assertEquals(originalLatitude, decodedPoint.getY(), acceptableError);
        assertEquals(originalLongitude, decodedPoint.getX(), acceptableError);
    }
}