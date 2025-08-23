package org.locationtech.spatial4j.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * Tests for the {@link WKTWriter} to ensure it correctly formats shapes into
 * Well-Known Text strings.
 */
public class WKTWriterTest {

    // Initialize context and writer once for all tests in this class.
    // SpatialContext.GEO is a standard, reusable geographic context.
    private final SpatialContext ctx = SpatialContext.GEO;
    private final ShapeWriter writer = ctx.getFormats().getWktWriter();

    /**
     * Verifies that an empty Point is formatted to the WKT string "POINT EMPTY".
     * In Spatial4j, an empty point is represented by a Point with NaN coordinates.
     */
    @Test
    public void toString_givenEmptyPoint_returnsPointEmpty() {
        // Arrange: Create an empty point.
        Point emptyPoint = ctx.makePoint(Double.NaN, Double.NaN);
        String expectedWkt = "POINT EMPTY";

        // Act: Convert the empty point to its WKT string representation.
        String actualWkt = writer.toString(emptyPoint);

        // Assert: The actual WKT string should match the expected format.
        assertEquals(expectedWkt, actualWkt);
    }
}