package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BufferedLine}, focusing on the impact of the buffer size
 * on object equality and the {@code hasArea()} property.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Verifies that two BufferedLine instances with different buffer sizes are not equal,
     * and that their area property correctly reflects whether the buffer is positive.
     */
    @Test
    public void testEqualityAndAreaForDifferentBuffers() {
        // Arrange: Create two buffered lines that are identical except for their buffer size.
        // Using a zero-length line (from a point to itself) isolates the buffer's effect.
        Point point = new PointImpl(0.0, 0.0, spatialContext);

        BufferedLine lineWithZeroBuffer = new BufferedLine(point, point, 0.0, spatialContext);
        BufferedLine lineWithPositiveBuffer = new BufferedLine(point, point, 90.0, spatialContext);

        // Assert: Check for inequality based on buffer size.
        assertNotEquals("Lines with different buffer sizes should not be equal.",
                lineWithZeroBuffer, lineWithPositiveBuffer);

        // Assert: Check the hasArea() property for both lines.
        assertTrue("A line with a positive buffer should have an area.",
                lineWithPositiveBuffer.hasArea());

        assertFalse("A line with a zero buffer should not have an area.",
                lineWithZeroBuffer.hasArea());
    }
}