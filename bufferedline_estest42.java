package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for edge cases in {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext cartesianContext = SpatialContext.GEO_FALSE;

    /**
     * This test verifies the behavior of BufferedLine when constructed with non-finite values.
     * When a point has an infinite coordinate and the buffer is also infinite, geometric
     * calculations can lead to counter-intuitive results (e.g., NaN - Not a Number).
     *
     * The expected outcome is that the shape is considered DISJOINT from its own defining point,
     * as the internal distance checks fail due to the non-finite arithmetic.
     */
    @Test
    public void relateWithInfiniteCoordinateAndBufferShouldReturnDisjoint() {
        // Arrange: Create a point with an infinite Y-coordinate and a BufferedLine
        // from this point to itself with an infinite buffer.
        Point pointWithInfiniteY = new PointImpl(10.0, Double.POSITIVE_INFINITY, cartesianContext);
        BufferedLine lineWithInfiniteBuffer = new BufferedLine(
                pointWithInfiniteY, pointWithInfiniteY, Double.POSITIVE_INFINITY, cartesianContext);

        // Act: Check the spatial relationship of the line to its own defining point.
        SpatialRelation relation = lineWithInfiniteBuffer.relate(pointWithInfiniteY);

        // Assert: The relation should be DISJOINT, not CONTAINS.
        // This occurs because internal calculations with Double.POSITIVE_INFINITY likely
        // produce NaN, causing containment checks to fail.
        assertEquals("A shape with infinite values should be disjoint from its defining point",
                SpatialRelation.DISJOINT, relation);

        // A line with an infinite buffer logically has an area.
        assertTrue("A line with an infinite buffer should have an area",
                lineWithInfiniteBuffer.hasArea());
    }
}