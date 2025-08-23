package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the BufferedLine class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class BufferedLine_ESTestTest11 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that calling getBuffered() with a zero distance on a line that
     * already has a zero buffer results in a new line that also has a zero buffer.
     * This verifies the base case for the buffering operation.
     */
    @Test
    public void getBufferedWithZeroDistanceOnZeroBufferLineReturnsZeroBufferLine() {
        // Arrange: Create a zero-length line (effectively a point) with a zero buffer.
        SpatialContext spatialContext = SpatialContext.GEO;
        Point point = new PointImpl(0.0, 0.0, spatialContext);
        
        // A BufferedLine from a point to itself represents a buffered point.
        BufferedLine initialLine = new BufferedLine(point, point, 0.0, spatialContext);
        assertEquals("Precondition failed: Initial line's buffer should be zero.", 0.0, initialLine.getBuf(), 0.0);

        // Act: Buffer the existing line with a distance of 0.0.
        // The getBuffered() method should return a new shape with an added buffer distance.
        BufferedLine resultLine = (BufferedLine) initialLine.getBuffered(0.0, spatialContext);

        // Assert: The resulting line's buffer should still be zero.
        assertEquals("Buffering by zero should not change the buffer size.", 0.0, resultLine.getBuf(), 0.0);
    }
}