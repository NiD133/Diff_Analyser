package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext context = SpatialContext.GEO;

    /**
     * Tests that the BufferedLine constructor rejects invalid arguments.
     * Specifically, it should throw an AssertionError if the buffer distance is negative.
     */
    @Test(expected = AssertionError.class)
    public void constructorShouldThrowAssertionErrorForNegativeBuffer() {
        // Arrange: Define the line's endpoints and an invalid negative buffer.
        // The specific points are not critical; the focus is on the buffer value.
        Point pointA = context.makePoint(0, 0);
        Point pointB = context.makePoint(10, 10);
        double negativeBuffer = -1.0;

        // Act & Assert: Attempt to create a BufferedLine with the negative buffer.
        // The @Test(expected) annotation asserts that an AssertionError is thrown.
        new BufferedLine(pointA, pointB, negativeBuffer, context);
    }
}