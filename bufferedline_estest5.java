package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    // A non-geodetic (Euclidean) context is used because BufferedLine operates in a 2D plane.
    private final SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());

    /**
     * This test verifies the properties of the internal "primary line" when a BufferedLine
     * is created from a single point (i.e., its start and end points are the same).
     * This is a degenerate case, effectively representing a buffered point.
     */
    @Test
    public void getLinePrimary_whenLineIsDegenerate_returnsCorrectlyConfiguredLine() {
        // ARRANGE
        // Define test data with descriptive names to avoid magic numbers.
        final double coordinateX = -1697.31;
        final double coordinateY = -512.1;
        final double bufferDistance = 3478.01;

        // A BufferedLine created from two identical points is a degenerate case.
        Point singlePoint = new PointImpl(coordinateX, coordinateY, spatialContext);
        BufferedLine bufferedLine = new BufferedLine(singlePoint, singlePoint, bufferDistance, spatialContext);

        // ACT
        // Retrieve the internal primary line used for calculations.
        InfBufLine primaryLine = bufferedLine.getLinePrimary();

        // ASSERT
        // For a degenerate line, the implementation treats the primary line as horizontal (slope=0).
        // Therefore, its y-intercept should be the y-coordinate of the point.
        final double expectedIntercept = coordinateY;
        assertEquals("Intercept should be the Y-coordinate of the point",
                expectedIntercept, primaryLine.getIntercept(), 0.01);

        // Verify the buffer distance was stored correctly in the parent BufferedLine.
        assertEquals("Buffer of BufferedLine should match the input buffer",
                bufferDistance, bufferedLine.getBuf(), 0.01);

        // Verify the buffer distance was propagated correctly to the internal primary line.
        assertEquals("Buffer of internal InfBufLine should match the input buffer",
                bufferDistance, primaryLine.getBuf(), 0.01);
    }
}