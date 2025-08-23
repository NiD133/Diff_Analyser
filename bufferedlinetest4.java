package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A randomized test that focuses on the spatial relationship between a
 * {@link org.locationtech.spatial4j.shape.impl.BufferedLine} and a
 * {@link Rectangle}. It uses a helper framework to test INTERSECTS, CONTAINS, etc.
 */
public class BufferedLineRectIntersectionTest extends RandomizedTest {

    private final SpatialContext ctx = new SpatialContextFactory() {
        {
            geo = false;
            worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
        }
    }.newSpatialContext();

    @Rule
    public TestLog testLog = TestLog.instance;

    /**
     * This test uses a helper framework, {@link RectIntersectionTestHelper}, to perform
     * numerous randomized checks of the `relate()` method between a {@link BufferedLine}
     * and a {@link Rectangle}.
     */
    @Test
    public void testRelateWithRandomRectangles() {
        new RectIntersectionTestHelper<BufferedLine>(ctx) {

            @Override
            protected BufferedLine generateRandomShape(Point nearP) {
                return createRandomDiagonalLine(nearP);
            }

            @Override
            protected Point randomPointInEmptyShape(BufferedLine shape) {
                // The name of this method is defined by the abstract base class. It's
                // meant to find a point within a shape that might otherwise have no area.
                // For a BufferedLine, the endpoints of the original line segment (A and B)
                // are guaranteed to be contained within the buffered shape, so we return one.
                return randomBoolean() ? shape.getA() : shape.getB();
            }
        }.testRelateWithRectangle();
    }

    /**
     * Creates a {@link BufferedLine} that runs diagonally across a randomly generated
     * rectangle. This provides a good variety of angles and positions for testing.
     *
     * @param nearPoint A point to generate the rectangle near.
     * @return A new BufferedLine for testing.
     */
    private BufferedLine createRandomDiagonalLine(Point nearPoint) {
        // Generate a random rectangle to base the line on.
        Rectangle rect = randomRectangle(nearPoint);
        List<Point> corners = getRectangleCorners(rect);

        // Create a line segment between two diagonally opposite corners of the rectangle.
        int startCornerIdx = randomInt(3);
        Point pA = corners.get(startCornerIdx);
        Point pB = corners.get((startCornerIdx + 2) % 4); // The opposite corner

        // The buffer is a random fraction of the rectangle's largest dimension.
        double maxDim = Math.max(rect.getWidth(), rect.getHeight());
        // Use a gaussian distribution for a more realistic spread of buffer sizes.
        double buf = Math.abs(randomGaussian()) * maxDim / 4.0;
        // The divisible() method from RandomizedTest can help produce rounder numbers,
        // which might be useful for debugging or hitting specific edge cases.
        int intBuf = randomInt((int) divisible(buf));

        return new BufferedLine(pA, pB, intBuf, ctx);
    }

    /**
     * Returns the four corners of a rectangle in a consistent order.
     */
    private List<Point> getRectangleCorners(Rectangle rect) {
        List<Point> corners = new ArrayList<>(4);
        corners.add(ctx.makePoint(rect.getMaxX(), rect.getMaxY())); // Top-right
        corners.add(ctx.makePoint(rect.getMinX(), rect.getMaxY())); // Top-left
        corners.add(ctx.makePoint(rect.getMinX(), rect.getMinY())); // Bottom-left
        corners.add(ctx.makePoint(rect.getMaxX(), rect.getMinY())); // Bottom-right
        return corners;
    }
}