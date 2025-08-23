package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    @Test
    public void getArea_withZeroLengthAndZeroBuffer_shouldReturnZero() {
        // Arrange: Create a degenerate line with zero length and a zero buffer.
        // This is effectively a point with no area.
        Point point = new PointImpl(0.0, 0.0, spatialContext);
        double zeroBuffer = 0.0;
        BufferedLine degenerateLine = new BufferedLine(point, point, zeroBuffer, spatialContext);

        // Act: Calculate the area of the shape.
        double area = degenerateLine.getArea(spatialContext);

        // Assert: Verify that the area is zero.
        double delta = 0.0; // Use a delta of 0.0 for exact floating-point comparisons to zero.
        assertEquals("A line with zero length and zero buffer should have an area of 0.0.", 0.0, area, delta);
        assertEquals("The buffer distance should be correctly stored as 0.0.", 0.0, degenerateLine.getBuf(), delta);
    }
}