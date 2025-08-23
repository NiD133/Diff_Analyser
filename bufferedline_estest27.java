package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    // Using a non-geodetic (Euclidean) context is appropriate
    // since BufferedLine operates in a Cartesian space.
    private final SpatialContext ctx = SpatialContext.SYSTEM;

    /**
     * Tests a special case where the line has zero length because its start and end points are identical.
     * This configuration should behave like a buffered point (i.e., a circle) and must contain
     * the point from which it was created.
     */
    @Test
    public void zeroLengthLineShouldContainItsCenterPoint() {
        // Arrange: Define the geometry for the test.
        // A single point will be used for both the start and end of the line.
        Point centerPoint = ctx.makePoint(0.0, 1774.45);
        double bufferDistance = 1774.45;

        // Create a zero-length line by providing the same point for start and end.
        BufferedLine zeroLengthLine = new BufferedLine(centerPoint, centerPoint, bufferDistance, ctx);

        // Act: Perform the action under test.
        boolean isContained = zeroLengthLine.contains(centerPoint);

        // Assert: Verify the outcome.
        assertTrue("A buffered line, even with zero length, must have an area.", zeroLengthLine.hasArea());
        assertTrue("A zero-length buffered line should contain its defining point.", isContained);
    }
}