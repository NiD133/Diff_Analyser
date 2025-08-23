package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the WKTWriter class.
 */
public class WKTWriterTest {

    /**
     * Tests that the WKTWriter correctly formats a Point with negative infinity coordinates.
     * The expected output should use the infinity symbol (∞) for the coordinates.
     */
    @Test
    public void toString_forPointWithNegativeInfinityCoordinates_writesInfinitySymbol() {
        // Arrange: Set up the test objects and expected values.
        SpatialContext geoContext = SpatialContext.GEO;
        Point pointWithNegativeInfinity = new PointImpl(
                Double.NEGATIVE_INFINITY,
                Double.NEGATIVE_INFINITY,
                geoContext
        );
        WKTWriter wktWriter = new WKTWriter();
        String expectedWkt = "POINT (-\u221E -\u221E)"; // WKT representation with infinity symbol

        // Act: Execute the method under test.
        String actualWkt = wktWriter.toString(pointWithNegativeInfinity);

        // Assert: Verify that the actual output matches the expected output.
        assertEquals(expectedWkt, actualWkt);
    }
}