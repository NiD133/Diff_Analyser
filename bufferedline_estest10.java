package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    // A standard delta for floating-point comparisons.
    private static final double DELTA = 0.01;

    /**
     * Tests the properties of the perpendicular line when a BufferedLine is
     * constructed with identical start and end points. This creates a degenerate
     * line that is effectively a buffered point.
     *
     * The implementation treats this case by creating a primary line that is horizontal
     * and a perpendicular line that is vertical, both passing through the point.
     */
    @Test
    public void getLinePerp_whenLineIsDegeneratePoint_returnsCorrectlyConfiguredLine() {
        // Arrange
        final double bufferDistance = 1945.7336009;
        SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());

        // A line with zero length is essentially a point.
        Point point = new PointImpl(0.0, 0.0, spatialContext);
        BufferedLine bufferedPointLine = new BufferedLine(point, point, bufferDistance, spatialContext);

        // Act
        // Get the line that is perpendicular to the (zero-length) primary line.
        InfBufLine perpendicularLine = bufferedPointLine.getLinePerp();

        // Assert
        // First, a sanity check on the original buffered line's buffer.
        assertEquals("The buffer of the BufferedLine should be correctly set.",
                bufferDistance, bufferedPointLine.getBuf(), DELTA);

        // For a degenerate point-line, the constructor defines the perpendicular line
        // as having an infinite slope (i.e., it's vertical).
        // The 'distDenomInv' (related to 1/sqrt(1+slope^2)) becomes NaN due to floating-point
        // arithmetic with infinity, which is the expected behavior in this implementation.
        assertEquals("distDenomInv should be NaN for a vertical line with infinite slope.",
                Double.NaN, perpendicularLine.getDistDenomInv(), DELTA);

        // The buffer of the perpendicular line should match the original buffer.
        assertEquals("The buffer of the perpendicular line should match the original.",
                bufferDistance, perpendicularLine.getBuf(), DELTA);
        
        // The perpendicular line passes through the center (0,0) and is vertical.
        // The implementation calculates its y-intercept as 0 in this special case.
        assertEquals("The intercept of the vertical perpendicular line should be 0.",
                0.0, perpendicularLine.getIntercept(), DELTA);
    }
}