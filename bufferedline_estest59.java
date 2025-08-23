package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * This test class contains tests for the {@link BufferedLine} class.
 */
public class BufferedLine_ESTestTest59 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that calling getBuffered() with a negative distance, which would result
     * in an overall negative buffer, throws an AssertionError. The constructor of
     * BufferedLine asserts that the buffer distance must be non-negative.
     */
    @Test(expected = AssertionError.class)
    public void getBufferedWithNegativeDistanceShouldThrowAssertionError() {
        // Arrange: Create a zero-length line (a point) with a zero buffer.
        // The spatial context is required for shape creation.
        SpatialContext geoContext = SpatialContext.GEO;
        Point point = new PointImpl(0.0, 0.0, geoContext);
        BufferedLine line = new BufferedLine(point, point, 0.0, geoContext);

        // A negative distance that will cause the resulting buffer to be negative.
        double negativeDistance = -10.0;

        // Act: Attempt to create a new buffered line where the new buffer size
        // (original buffer + distance) is negative (0.0 + -10.0 = -10.0).
        line.getBuffered(negativeDistance, geoContext);

        // Assert: The test expects an AssertionError, which is declared in the
        // @Test annotation. This is because the BufferedLine constructor does not
        // allow negative buffer values.
    }
}