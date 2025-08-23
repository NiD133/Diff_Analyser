package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    @Test
    public void getArea_forZeroLengthLine_returnsAreaOfSquareBoundingBox() {
        // ARRANGE
        // A zero-length BufferedLine is created by using the same start and end point.
        // This shape is geometrically equivalent to a circle (a buffered point).
        final SpatialContext context = SpatialContext.GEO;
        final double buffer = 2115.863165;
        final Point centerPoint = new PointImpl(0, 0, context);

        BufferedLine zeroLengthLine = new BufferedLine(centerPoint, centerPoint, buffer, context);

        // The area calculation for a zero-length buffered line is based on its square
        // bounding box, not the area of a circle (π * r^2).
        // The side of the square is 2 * buffer, so the area is (2 * buffer)^2.
        double expectedArea = 4 * buffer * buffer;
        final double DELTA = 0.01;

        // ACT
        double actualArea = zeroLengthLine.getArea(context);

        // ASSERT
        assertTrue("A buffered line with a non-zero buffer should have an area.", zeroLengthLine.hasArea());
        assertEquals("The area of a zero-length line should be the area of its square bounding box.",
                expectedArea, actualArea, DELTA);
    }
}