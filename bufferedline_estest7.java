package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BufferedLine}.
 * This improved version replaces an auto-generated test case with a clear,
 * maintainable, and human-readable one.
 */
public class BufferedLineTest {

    /**
     * Tests that the perpendicular line is calculated correctly for a standard sloped line.
     * The perpendicular line should pass through the center of the original line segment
     * and have a slope that is the negative reciprocal of the original line's slope.
     */
    @Test
    public void getLinePerp_calculatesCorrectInterceptForSlopedLine() {
        // ARRANGE
        // Use a non-geodetic (flat-earth) context, as BufferedLine operates in Euclidean space.
        SpatialContext ctx = new SpatialContext(new SpatialContextFactory());

        // Define a simple line segment from (10, 10) to (20, 30) with an arbitrary buffer.
        // The buffer value does not affect the perpendicular line's intercept.
        Point startPoint = new PointImpl(10, 10, ctx);
        Point endPoint = new PointImpl(20, 30, ctx);
        double buffer = 5.0;

        BufferedLine bufferedLine = new BufferedLine(startPoint, endPoint, buffer, ctx);

        // ACT
        // Get the perpendicular line, which is the focus of this test.
        InfBufLine perpendicularLine = bufferedLine.getLinePerp();

        // ASSERT
        // The center of the line segment is ((10+20)/2, (10+30)/2) = (15, 20).
        // The slope of the primary line is (30-10)/(20-10) = 20/10 = 2.
        // The slope of the perpendicular line is the negative reciprocal: -1/2 = -0.5.
        // The intercept of the perpendicular line is calculated from y = mx + c => c = y - mx.
        // Using the center point (15, 20) and slope -0.5:
        // intercept = 20 - (-0.5 * 15) = 20 - (-7.5) = 27.5.
        double expectedIntercept = 27.5;
        double delta = 1e-9; // Use a small delta for floating-point comparison.

        assertEquals("The perpendicular line's intercept should be calculated correctly.",
                expectedIntercept, perpendicularLine.getIntercept(), delta);
    }
}