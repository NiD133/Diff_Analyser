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

    // Using a well-known location (Amsterdam Centraal) makes the test data less abstract.
    private static final double TEST_LATITUDE = 52.3738007;
    private static final double TEST_LONGITUDE = 4.8909347;

    // Geohash encoding is lossy, so we define an acceptable margin of error for the decoded value.
    private static final double DECODING_PRECISION_DELTA = 0.00001D;

    /**
     * Verifies that encoding coordinates to a geohash and decoding it back
     * results in a point that is very close to the original. This is a
     * fundamental "round-trip" test for the utility.
     */
    @Test
    public void encodeAndDecode_shouldReturnPointCloseToOriginal() {
        // Arrange: Define the original coordinates.
        double originalLatitude = TEST_LATITUDE;
        double originalLongitude = TEST_LONGITUDE;

        // Act: Encode the coordinates to a geohash, then decode it back to a Point.
        String geohash = GeohashUtils.encodeLatLon(originalLatitude, originalLongitude);
        Point decodedPoint = GeohashUtils.decode(geohash, ctx);

        // Assert: The decoded coordinates should match the originals within the allowed precision delta.
        // We check Y for latitude and X for longitude, as per Spatial4j convention.
        assertEquals(
                "Decoded latitude should be close to the original",
                originalLatitude,
                decodedPoint.getY(),
                DECODING_PRECISION_DELTA
        );
        assertEquals(
                "Decoded longitude should be close to the original",
                originalLongitude,
                decodedPoint.getX(),
                DECODING_PRECISION_DELTA
        );
    }
}