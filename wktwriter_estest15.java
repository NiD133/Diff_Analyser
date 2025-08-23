package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link WKTWriter} class.
 * This refactored test focuses on clarity and maintainability.
 */
public class WKTWriterTest {

    /**
     * Tests that the WKTWriter correctly formats an empty Point shape
     * into the standard "POINT EMPTY" string representation.
     */
    @Test
    public void toString_withEmptyPoint_shouldReturnPointEmpty() {
        // Arrange
        WKTWriter wktWriter = new WKTWriter();
        SpatialContext spatialContext = SpatialContext.GEO;

        // In Spatial4j, an empty point is created by providing NaN for coordinates.
        Point emptyPoint = spatialContext.makePoint(Double.NaN, Double.NaN);
        String expectedWkt = "POINT EMPTY";

        // Act
        String actualWkt = wktWriter.toString(emptyPoint);

        // Assert
        assertEquals("The WKT for an empty point should be 'POINT EMPTY'", expectedWkt, actualWkt);
    }
}