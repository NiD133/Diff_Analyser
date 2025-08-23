package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests the initialization of a {@link BufferedLine} for the degenerate case
     * where the start and end points are the same, creating a zero-length line.
     * This test verifies the properties of the internal perpendicular helper line, which
     * is expected to be a vertical line in this scenario.
     */
    @Test
    public void perpendicularLineProperties_forZeroLengthLine_areCorrectlyInitialized() {
        // ARRANGE: Create a zero-length, zero-buffer line at the origin.
        // This is a degenerate case, effectively representing a point.
        Point startAndEndPoint = new PointImpl(0.0, 0.0, spatialContext);
        double buffer = 0.0;
        BufferedLine zeroLengthLine = new BufferedLine(startAndEndPoint, startAndEndPoint, buffer, spatialContext);

        // ACT: Retrieve the internal perpendicular line.
        // For a zero-length line, the primary line is treated as horizontal (slope=0),
        // making the perpendicular line vertical (slope=infinity).
        InfBufLine perpendicularLine = zeroLengthLine.getLinePerp();

        // ASSERT: Verify the properties of the original line and the perpendicular line.
        assertEquals("Buffer of the original line should be the provided value.",
                0.0, zeroLengthLine.getBuf(), 0.01);

        // The perpendicular line is vertical, passes through the origin, and has no buffer.
        assertEquals("Buffer of the perpendicular line should also be zero.",
                0.0, perpendicularLine.getBuf(), 0.01);
        assertEquals("Intercept of a vertical line through the origin is defined as 0.",
                0.0, perpendicularLine.getIntercept(), 0.01);

        // The distance denominator (used in distance calculations) is expected to be NaN
        // for a line with an infinite slope. This is a known and tested outcome for this edge case.
        assertEquals("Distance denominator for a vertical line should be NaN.",
                Double.NaN, perpendicularLine.getDistDenomInv(), 0.01);
    }
}