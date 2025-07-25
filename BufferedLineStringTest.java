package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Tests the intersection of {@link BufferedLineString} with {@link Rectangle}.
 * The tests generate random BufferedLineStrings within a defined spatial context
 * and check their spatial relationship with randomly generated Rectangles.
 */
public class BufferedLineStringTest extends RandomizedTest {

    // Define a spatial context without geography and a limited world bounds for testing.
    private final SpatialContext ctx = new SpatialContextFactory() {{
        geo = false;
        worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
    }}.newSpatialContext();

    /**
     * Tests the intersection of BufferedLineString with a Rectangle.
     * It generates random BufferedLineStrings near a randomly generated Rectangle,
     * and then uses the RectIntersectionTestHelper to check if the spatial
     * relationship between them is correctly determined.
     */
    @Test
    public void testRectIntersect() {
        new RectIntersectionTestHelper<BufferedLineString>(ctx) {

            /**
             * Generates a random BufferedLineString near a given Point.
             * The BufferedLineString consists of a random number of points (2-5),
             * a random buffer width, and a SpatialContext.
             *
             * @param nearP The point near which the BufferedLineString is generated.
             * @return A randomly generated BufferedLineString.
             */
            @Override
            protected BufferedLineString generateRandomShape(Point nearP) {
                Rectangle nearR = randomRectangle(nearP);
                int numPoints = 2 + randomInt(3); // 2-5 points

                ArrayList<Point> points = new ArrayList<>(numPoints);
                while (points.size() < numPoints) {
                    points.add(randomPointIn(nearR));
                }

                double maxBuf = Math.max(nearR.getWidth(), nearR.getHeight());
                double buf = Math.abs(randomGaussian()) * maxBuf / 4;
                buf = randomInt((int) divisible(buf)); // Ensure buffer is an integer

                return new BufferedLineString(points, buf, ctx);
            }

            /**
             * Selects a random point from the list of points that make up the given BufferedLineString.
             * Used for testing containment when the shape is effectively empty (zero width, zero buffer).
             * @param shape The BufferedLineString to select a point from.
             * @return A random Point from the BufferedLineString's point list.
             */
            @Override
            protected Point randomPointInEmptyShape(BufferedLineString shape) {
                List<Point> points = shape.getPoints();
                assertTrue(!points.isEmpty()); // Assert that there are points to choose from.
                return points.get(randomInt(points.size() - 1));
            }
        }.testRelateWithRectangle();
    }
}