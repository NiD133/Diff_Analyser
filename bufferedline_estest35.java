package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    private final SpatialContext ctx = SpatialContext.GEO;

    /**
     * Tests that two BufferedLine objects are not equal if their buffer sizes differ,
     * even when their underlying line segments are identical.
     */
    @Test
    public void testEqualsReturnsFalseForDifferentBufferSizes() {
        // ARRANGE: Create two BufferedLine objects that are identical except for their buffer size.
        // Using the same start and end point effectively creates a buffered point (a circle).
        Point commonPoint = ctx.makePoint(10, 20);
        double bufferA = 5.0;
        double bufferB = 10.0;

        BufferedLine lineA = new BufferedLine(commonPoint, commonPoint, bufferA, ctx);
        BufferedLine lineB = new BufferedLine(commonPoint, commonPoint, bufferB, ctx);

        // ACT & ASSERT: The two lines should not be equal because their buffers differ.
        assertNotEquals(lineA, lineB);

        // Sanity check: Both lines should report having an area due to the non-zero buffer.
        assertTrue("A line with a positive buffer should have an area.", lineA.hasArea());
        assertTrue("A line with a positive buffer should have an area.", lineB.hasArea());
    }
}