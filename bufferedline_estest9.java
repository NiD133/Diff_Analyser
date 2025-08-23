package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// The test class name is auto-generated, but kept for consistency with the original.
// In a real-world scenario, this would be renamed to "BufferedLineTest".
public class BufferedLine_ESTestTest9 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that the perpendicular line for a vertical BufferedLine is calculated correctly.
     * For a vertical line segment, the perpendicular line is horizontal and should pass
     * through the vertical midpoint of the original segment.
     */
    @Test
    public void getLinePerp_forVerticalLine_calculatesCorrectIntercept() {
        // Arrange: Create a vertical line segment along the Y-axis.
        SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());

        double yStart = -3402.5;
        double yEnd = 0.0;
        Point pointA = new PointImpl(0.0, yStart, spatialContext);
        Point pointB = new PointImpl(0.0, yEnd, spatialContext);
        
        // The buffer value does not affect the position of the perpendicular line's intercept.
        double buffer = 1945.73;

        BufferedLine bufferedLine = new BufferedLine(pointA, pointB, buffer, spatialContext);

        // The expected intercept of the perpendicular line is the Y-coordinate of the center point.
        double expectedIntercept = (yStart + yEnd) / 2.0; // -1701.25

        // Act: Get the perpendicular line from the buffered line.
        InfBufLine perpendicularLine = bufferedLine.getLinePerp();

        // Assert: Verify the intercept and that the line has area.
        assertEquals("The perpendicular line's intercept should be the midpoint of the original line.",
                expectedIntercept, perpendicularLine.getIntercept(), 0.01);

        assertTrue("A buffered line with a positive buffer must have an area.",
                bufferedLine.hasArea());
    }
}