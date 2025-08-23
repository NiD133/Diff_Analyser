package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    // Use a standard spatial context for creating shapes.
    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * A degenerate BufferedLine, where the start and end points are the same,
     * effectively behaves like a circle. This test verifies that a point
     * located outside this circle's buffer is correctly identified as not
     * being contained.
     */
    @Test
    public void contains_forDegenerateLine_whenPointIsOutsideBuffer_returnsFalse() {
        // Arrange
        // A degenerate line (start and end points are the same) with a buffer
        // is effectively a circle with the point as its center and the buffer as its radius.
        Point center = spatialContext.makePoint(10, 10);
        double buffer = 5.0;
        BufferedLine lineAsCircle = new BufferedLine(center, center, buffer, spatialContext);

        // Create a point whose distance from the center (6 units) is greater than the buffer (5 units).
        Point outsidePoint = spatialContext.makePoint(16, 10);

        // Act
        boolean isContained = lineAsCircle.contains(outsidePoint);

        // Assert
        assertFalse("Point should not be contained as it lies outside the buffer radius.", isContained);
        // Verify the buffer was set correctly, confirming the test setup.
        assertEquals("The buffer distance should be correctly stored.", buffer, lineAsCircle.getBuf(), 0.0);
    }
}