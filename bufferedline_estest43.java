package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    // Use a single, shared SpatialContext for all tests in this class.
    // GEO context means units are in degrees.
    private final SpatialContext ctx = SpatialContext.GEO;

    /**
     * Tests that a BufferedLine correctly identifies an intersection with a rectangle.
     *
     * <p>This test case covers a specific scenario where the BufferedLine is defined
     * by the same start and end points, effectively creating a buffered point (a circle).
     * The test verifies that this circle correctly reports an INTERSECTS relationship
     * with a rectangle whose edge touches the circle's center.
     */
    @Test
    public void bufferedPointShouldIntersectWithTouchingRectangle() {
        // Arrange
        // A BufferedLine with identical start and end points behaves like a circle.
        Point centerPoint = ctx.makePoint(10, 20);
        double bufferDistance = 5.0;
        BufferedLine bufferedPoint = new BufferedLine(centerPoint, centerPoint, bufferDistance, ctx);

        // Create a rectangle whose right edge (at x=10) passes through the circle's center (10, 20).
        Rectangle intersectingRectangle = ctx.makeRectangle(0, 10, 15, 25);

        // Act
        SpatialRelation relation = bufferedPoint.relate(intersectingRectangle);

        // Assert
        // The rectangle's boundary touches the center of the buffered point, so they must intersect.
        assertEquals(SpatialRelation.INTERSECTS, relation);

        // Also, verify a fundamental property: a buffered line must have an area.
        assertTrue("A buffered line should always have an area", bufferedPoint.hasArea());
    }
}