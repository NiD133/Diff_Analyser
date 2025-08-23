package org.locationtech.spatial4j.shape;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BufferedLine} that verify the distance calculation
 * used by the {@link BufferedLine#contains(Point)} method.
 */
public class BufferedLineTest {

    /**
     * The SpatialContext is non-geodetic (geo=false), meaning it operates on a
     * flat 2D plane. This is important because BufferedLine is implemented
     * using Euclidean geometry.
     */
    private final SpatialContext ctx = new SpatialContextFactory() {
        {
            geo = false;
            worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
        }
    }.newSpatialContext();

    /**
     * Asserts that the distance calculation for containment is correct.
     * <p>
     * It verifies that a point is not contained when the buffer is slightly
     * smaller than the expected distance, and that it is contained when the
     * buffer is slightly larger. This effectively tests the distance threshold.
     *
     * @param lineStart        The starting point of the line segment.
     * @param lineEnd          The ending point of the line segment.
     * @param pointToTest      The point to check for containment.
     * @param expectedDistance The known distance from the point to the line segment.
     */
    private void assertContainmentAtDistanceThreshold(Point lineStart, Point lineEnd, Point pointToTest, double expectedDistance) {
        // A buffer slightly smaller than the actual distance should NOT contain the point.
        if (expectedDistance > 0) {
            BufferedLine smallerBuffer = new BufferedLine(lineStart, lineEnd, expectedDistance * 0.999, ctx);
            assertFalse("Point should not be contained with a buffer smaller than the distance",
                    smallerBuffer.contains(pointToTest));
        } else {
            // Special case: If distance is 0, the point is on the line segment.
            // A zero-buffer line should contain it.
            assert expectedDistance == 0;
            BufferedLine zeroBuffer = new BufferedLine(lineStart, lineEnd, 0, ctx);
            assertTrue("Point on the line should be contained with a zero buffer",
                    zeroBuffer.contains(pointToTest));
        }

        // A buffer slightly larger than the actual distance SHOULD contain the point.
        BufferedLine largerBuffer = new BufferedLine(lineStart, lineEnd, expectedDistance * 1.001, ctx);
        assertTrue("Point should be contained with a buffer larger than the distance",
                largerBuffer.contains(pointToTest));
    }

    @Test
    public void containmentThreshold_forNegativeSlopeLine() {
        Point lineStart = ctx.makePoint(7, -4);
        Point lineEnd = ctx.makePoint(3, 2);
        Point pointToTest = ctx.makePoint(5, 6);
        double expectedDistance = 3.88290;

        assertContainmentAtDistanceThreshold(lineStart, lineEnd, pointToTest, expectedDistance);
    }

    @Test
    public void containmentThreshold_forPositiveSlopeLine() {
        Point lineStart = ctx.makePoint(3, 2);
        Point lineEnd = ctx.makePoint(7, 5);
        Point pointToTest = ctx.makePoint(5, 6);
        double expectedDistance = 2.0;

        assertContainmentAtDistanceThreshold(lineStart, lineEnd, pointToTest, expectedDistance);
    }

    @Test
    public void containmentThreshold_forVerticalLine() {
        Point lineStart = ctx.makePoint(3, 2);
        Point lineEnd = ctx.makePoint(3, 8);
        Point pointToTest = ctx.makePoint(4, 3);
        double expectedDistance = 1.0;

        assertContainmentAtDistanceThreshold(lineStart, lineEnd, pointToTest, expectedDistance);
    }

    @Test
    public void containmentThreshold_forHorizontalLine() {
        Point lineStart = ctx.makePoint(3, 2);
        Point lineEnd = ctx.makePoint(6, 2);
        Point pointToTest = ctx.makePoint(4, 3);
        double expectedDistance = 1.0;

        assertContainmentAtDistanceThreshold(lineStart, lineEnd, pointToTest, expectedDistance);
    }
}