package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    private final SpatialContext ctx = SpatialContext.GEO;

    // Coordinates for Amsterdam, Netherlands
    private static final double AMSTERDAM_LAT = 52.3738007;
    private static final double AMSTERDAM_LON = 4.8909347;

    // The expected geohash for Amsterdam with default (12-char) precision.
    private static final String AMSTERDAM_GEOHASH_PRECISION_12 = "u173zq37x014";

    // A shorter, 4-char precision geohash for the same area.
    private static final String AMSTERDAM_GEOHASH_PRECISION_4 = "u173";

    // The decoded center point of the 12-char geohash.
    // Note: Decoding a geohash gives the center of the bounding box, which may not be the exact original coordinates.
    private static final double DECODED_12_CHAR_GEOHASH_LAT = 52.37380061d;
    private static final double DECODED_12_CHAR_GEOHASH_LON = 4.8909343d;

    // A small tolerance for comparing floating-point coordinates.
    private static final double COORDINATE_ASSERTION_TOLERANCE = 0.000001d;

    @Test
    public void shouldEncodeLocationToCorrectGeohash() {
        // Arrange
        // Act
        String generatedGeohash = GeohashUtils.encodeLatLon(AMSTERDAM_LAT, AMSTERDAM_LON);

        // Assert
        assertEquals(AMSTERDAM_GEOHASH_PRECISION_12, generatedGeohash);
    }

    @Test
    public void shouldDecodeGeohashToCorrectLocation() {
        // Arrange
        // Act
        Point decodedPoint = GeohashUtils.decode(AMSTERDAM_GEOHASH_PRECISION_12, ctx);

        // Assert
        assertEquals("Latitude should match the center of the geohash cell",
                DECODED_12_CHAR_GEOHASH_LAT, decodedPoint.getY(), COORDINATE_ASSERTION_TOLERANCE);
        assertEquals("Longitude should match the center of the geohash cell",
                DECODED_12_CHAR_GEOHASH_LON, decodedPoint.getX(), COORDINATE_ASSERTION_TOLERANCE);
    }

    /**
     * This test verifies that a decode-then-encode round trip reproduces the original geohash.
     * This behavior was the subject of LUCENE-1815.
     * See: https://issues.apache.org/jira/browse/LUCENE-1815
     */
    @Test
    public void shouldPreserveGeohashAfterDecodeEncodeRoundtrip() {
        // Arrange
        String originalGeohash = AMSTERDAM_GEOHASH_PRECISION_12;

        // Act
        Point decodedPoint = GeohashUtils.decode(originalGeohash, ctx);
        String reEncodedGeohash = GeohashUtils.encodeLatLon(decodedPoint.getY(), decodedPoint.getX());

        // Assert
        assertEquals("Re-encoded geohash should match the original", originalGeohash, reEncodedGeohash);
    }

    /**
     * This test verifies a more complex round-trip scenario. When a short geohash is decoded,
     * its center point is found. When that center point is re-encoded (to a higher default precision)
     * and then decoded again, the resulting point should be the same as the first decoded point.
     */
    @Test
    public void shouldPreserveDecodedPointAfterRoundtripWithShorterGeohash() {
        // Arrange
        String shortGeohash = AMSTERDAM_GEOHASH_PRECISION_4;

        // Act
        // 1. Decode the short geohash to get its center point.
        Point initialDecodedPoint = GeohashUtils.decode(shortGeohash, ctx);

        // 2. Re-encode the center point. This will use default precision, creating a longer geohash.
        String reEncodedLongGeohash = GeohashUtils.encodeLatLon(initialDecodedPoint.getY(), initialDecodedPoint.getX());

        // 3. Decode the new, longer geohash.
        Point finalDecodedPoint = GeohashUtils.decode(reEncodedLongGeohash, ctx);

        // Assert
        // The point decoded from the long geohash should be the same as the center of the short geohash.
        assertEquals(initialDecodedPoint.getY(), finalDecodedPoint.getY(), COORDINATE_ASSERTION_TOLERANCE);
        assertEquals(initialDecodedPoint.getX(), finalDecodedPoint.getX(), COORDINATE_ASSERTION_TOLERANCE);
    }
}