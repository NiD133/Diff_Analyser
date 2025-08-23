package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BufferedLine} class, focusing on its internal line representations.
 */
public class BufferedLine_ESTestTest4 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that for a vertically-aligned BufferedLine, the internal primary line
     * is correctly configured with an infinite slope. This is a special case in the
     * line's geometric calculations.
     */
    @Test
    public void getLinePrimary_whenLineIsVertical_shouldHaveInfiniteSlope() {
        // Arrange: Create a vertical line with a zero buffer.
        // A vertical line is defined by two points having the same X-coordinate.
        SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());
        Point startPoint = new PointImpl(0.0, -10.0, spatialContext);
        Point endPoint = new PointImpl(0.0, 10.0, spatialContext);
        double buffer = 0.0;

        BufferedLine verticalLine = new BufferedLine(startPoint, endPoint, buffer, spatialContext);

        // Act: Retrieve the internal primary line representation.
        InfBufLine primaryLine = verticalLine.getLinePrimary();

        // Assert: Verify the properties of the primary line.
        // The buffer should be correctly propagated to the internal line.
        assertEquals("Buffer of the main line should be 0.", 0.0, verticalLine.getBuf(), 0.01);
        assertEquals("Buffer of the primary internal line should be 0.", 0.0, primaryLine.getBuf(), 0.01);

        // For a vertical line, the slope is mathematically infinite.
        assertEquals("Slope of a vertical line should be infinite.", Double.POSITIVE_INFINITY, primaryLine.getSlope(), 0.01);
        
        // The implementation uses an intercept of 0 for this vertical line case.
        assertEquals("Intercept for a vertical line at X=0 should be 0.", 0.0, primaryLine.getIntercept(), 0.01);
    }
}