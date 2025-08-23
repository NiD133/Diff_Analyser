package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    private final SpatialContext ctx = SpatialContext.GEO;

    @Test
    public void decodeBoundary_withSingleCharGeohash_returnsCorrectBoundingBox() {
        // GIVEN a single-character geohash "d".
        String geohash = "d";

        // According to the geohash algorithm, "d" (binary 01100) decodes to the
        // longitude range [-90°, -45°] and the latitude range [0°, 45°].
        Rectangle expectedBoundingBox = ctx.makeRectangle(-90.0, -45.0, 0.0, 45.0);

        // WHEN decoding the geohash to its boundary rectangle
        Rectangle actualBoundingBox = GeohashUtils.decodeBoundary(geohash, ctx);

        // THEN the resulting bounding box should match the expected coordinates
        assertEquals(expectedBoundingBox, actualBoundingBox);
    }
}