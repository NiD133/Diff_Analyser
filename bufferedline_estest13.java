package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BufferedLine}.
 * This class contains the refactored test from the original BufferedLine_ESTestTest13.
 */
public class BufferedLineTest {

    private final SpatialContext CONTEXT = SpatialContext.GEO;

    /**
     * Tests that the bounding box for a "degenerate" line (where start and end points
     * are the same) is calculated correctly. This is effectively a buffered point,
     * so the bounding box should be a square centered on the point.
     */
    @Test
    public void getBoundingBoxForDegenerateLineShouldReturnCorrectlySizedBox() {
        // --- Arrange ---
        // Use simple, whole numbers for coordinates and buffer to make expected results obvious.
        double pointX = 10.0;
        double pointY = 20.0;
        double buffer = 5.0;

        // Use a descriptive name for the point.
        Point centerPoint = CONTEXT.makePoint(pointX, pointY);

        // Create the buffered line with the same start and end point, making it a degenerate case.
        BufferedLine bufferedLine = new BufferedLine(centerPoint, centerPoint, buffer, CONTEXT);

        // --- Act ---
        // Call the method under test and store its result.
        Rectangle boundingBox = bufferedLine.getBoundingBox();

        // --- Assert ---
        // For a buffered point (x, y) with buffer 'b', the expected bounding box is:
        // minX = x - b, maxX = x + b, minY = y - b, maxY = y + b.
        double expectedMinX = pointX - buffer; // 10.0 - 5.0 = 5.0
        double expectedMaxX = pointX + buffer; // 10.0 + 5.0 = 15.0
        double expectedMinY = pointY - buffer; // 20.0 - 5.0 = 15.0
        double expectedMaxY = pointY + buffer; // 20.0 + 5.0 = 25.0

        assertEquals("Min X should be the point's X-coordinate minus the buffer", expectedMinX, boundingBox.getMinX(), 0.0);
        assertEquals("Max X should be the point's X-coordinate plus the buffer", expectedMaxX, boundingBox.getMaxX(), 0.0);
        assertEquals("Min Y should be the point's Y-coordinate minus the buffer", expectedMinY, boundingBox.getMinY(), 0.0);
        assertEquals("Max Y should be the point's Y-coordinate plus the buffer", expectedMaxY, boundingBox.getMaxY(), 0.0);

        // Also confirm the buffer property itself was stored correctly.
        assertEquals("The buffer value should be retrievable", buffer, bufferedLine.getBuf(), 0.0);
    }
}