package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BufferedLine}, focusing on specific edge cases and behaviors.
 */
public class BufferedLineTest extends RandomizedTest {

    // A non-geodetic (flat) context is used because BufferedLine operates in Euclidean space.
    private final SpatialContext ctx = new SpatialContextFactory() {{
        geo = false;
        worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
    }}.newSpatialContext();

    /**
     * Tests the contains() method for a zero-length line where the start and end points are the same.
     * This configuration should behave identically to a circle.
     */
    @Test
    public void testContainsForZeroLengthLine() {
        // GIVEN a zero-length line (a point) with a buffer, which should act like a circle.
        double buffer = 3.0;
        Point center = ctx.makePoint(10, 1);
        BufferedLine lineAsCircle = new BufferedLine(center, center, buffer, ctx);

        // AND a small value to test points just inside or outside the buffer boundary.
        double epsilon = 0.1;

        // WHEN checking a point just inside the buffer
        Point pointInside = ctx.makePoint(center.getX(), center.getY() + buffer - epsilon);
        // THEN it should be contained
        assertTrue("A point just inside the buffer radius should be contained.",
                lineAsCircle.contains(pointInside));

        // WHEN checking a point just outside the buffer
        Point pointOutside = ctx.makePoint(center.getX(), center.getY() + buffer + epsilon);
        // THEN it should not be contained
        assertFalse("A point just outside the buffer radius should not be contained.",
                lineAsCircle.contains(pointOutside));
    }
}