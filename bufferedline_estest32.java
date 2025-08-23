package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link BufferedLine} class, focusing on edge cases.
 */
public class BufferedLineTest {

    // Using a standard, readily available SpatialContext is cleaner than building one from scratch.
    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests that hashCode() can be called on a degenerate BufferedLine
     * (a zero-length line with a zero buffer) without throwing an exception.
     * This edge case is equivalent to a point.
     */
    @Test
    public void hashCodeShouldNotFailForDegenerateLine() {
        // Arrange: Create a degenerate line from a point to itself with a zero buffer.
        // This is an important edge case where the line is effectively just a point.
        Point point = spatialContext.makePoint(0.0, 0.0);
        double buffer = 0.0;
        BufferedLine degenerateLine = new BufferedLine(point, point, buffer, spatialContext);

        // Act: Call hashCode(). The primary goal is to ensure this does not throw an exception.
        // No assertion is needed on the return value itself, as it's implementation-dependent.
        degenerateLine.hashCode();

        // Assert: Verify the state of the created object to confirm it was constructed as expected.
        assertEquals("The buffer distance should be correctly initialized.",
                buffer, degenerateLine.getBuf(), 0.0);
    }
}