package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.InfBufLine;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Tests the quadrant logic of the internal {@link org.locationtech.spatial4j.shape.impl.InfBufLine},
 * which is a component of {@link BufferedLine}.
 */
public class BufferedLineQuadrantTest extends RandomizedTest {

    private static final double DISTANCE_COMPARISON_TOLERANCE = 1e-6;

    private final SpatialContext ctx = new SpatialContextFactory() {
        {
            geo = false;
            worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
        }
    }.newSpatialContext();

    @Rule
    public TestLog testLog = TestLog.instance;

    /**
     * This test verifies a geometric property of the {@code InfBufLine.quadrant()} method.
     * The property is that for a given rectangle, the quadrant containing the rectangle's center
     * is the same as the quadrant corresponding to the corner of the rectangle that is farthest
     * from the line. This provides a way to test the correctness of the quadrant calculation
     * by comparing it against a brute-force distance check to all corners.
     */
    @Test
    @Repeat(iterations = 15)
    public void quadrantOfCenter_shouldCorrespondTo_farthestCorner() {
        // ARRANGE
        // Create a random line and use its bounding box for the test.
        BufferedLine line = createRandomBufferedLine();
        Rectangle rect = line.getBoundingBox();
        Point center = rect.getCenter();

        // The method under test is on an internal component of BufferedLine.
        // We are testing its behavior in relation to the rectangle's geometry.
        InfBufLine primaryLine = line.getLinePrimary();

        // ACT
        // 1. Calculate the quadrant of the rectangle's center using the method under test.
        int actualQuadrantOfCenter = primaryLine.quadrant(center);

        // 2. Find the expected quadrant(s) by finding which corner is farthest from the line.
        Collection<Integer> expectedFarthestQuadrants = findFarthestCornerQuadrants(primaryLine, rect);

        // ASSERT
        String assertionMessage = String.format(
                "The quadrant of the center (%d) should be one of the farthest quadrants %s. " +
                "Line: %s, Rect: %s",
                actualQuadrantOfCenter, expectedFarthestQuadrants, line, rect);
        assertTrue(assertionMessage, expectedFarthestQuadrants.contains(actualQuadrantOfCenter));
    }

    /**
     * Creates a {@link BufferedLine} with random points and a random buffer.
     */
    private BufferedLine createRandomBufferedLine() {
        Point pA = ctx.makePoint(randomIntBetween(-50, 50), randomIntBetween(-25, 25));
        Point pB = ctx.makePoint(randomIntBetween(-50, 50), randomIntBetween(-25, 25));
        int buf = randomInt(5);

        // Randomly swap points to test both construction orders
        if (randomBoolean()) {
            return new BufferedLine(pB, pA, buf, ctx);
        } else {
            return new BufferedLine(pA, pB, buf, ctx);
        }
    }

    /**
     * Finds the quadrant(s) corresponding to the corner(s) of the rectangle
     * that are farthest from the given line.
     *
     * @param line The line to measure distance from.
     * @param rect The rectangle whose corners are to be checked.
     * @return A collection of quadrant numbers (1-4). The collection may contain
     *         more than one quadrant in case of a tie in distance.
     */
    private Collection<Integer> findFarthestCornerQuadrants(InfBufLine line, Rectangle rect) {
        List<Point> corners = getRectangleCorners(rect);
        Collection<Integer> farthestQuadrants = new LinkedList<>();
        double maxDistance = -1.0;

        // Quadrants are numbered 1-4, corresponding to the order in getRectangleCorners.
        for (int quadrant = 1; quadrant <= corners.size(); quadrant++) {
            Point corner = corners.get(quadrant - 1);
            double distance = line.distanceUnbuffered(corner);

            if (distance > maxDistance + DISTANCE_COMPARISON_TOLERANCE) {
                // Found a new farthest corner
                maxDistance = distance;
                farthestQuadrants.clear();
                farthestQuadrants.add(quadrant);
            } else if (Math.abs(distance - maxDistance) < DISTANCE_COMPARISON_TOLERANCE) {
                // Found a corner with roughly the same distance (a tie)
                farthestQuadrants.add(quadrant);
            }
        }
        return farthestQuadrants;
    }

    /**
     * Returns the four corners of a rectangle in a specific order corresponding
     * to quadrants 1 through 4 relative to the rectangle's center.
     * Quadrant 1: Top-Right
     * Quadrant 2: Top-Left
     * Quadrant 3: Bottom-Left
     * Quadrant 4: Bottom-Right
     *
     * @param rect The rectangle.
     * @return A list of the four corner points.
     */
    private List<Point> getRectangleCorners(Rectangle rect) {
        List<Point> corners = new ArrayList<>(4);
        // The order here defines the quadrant numbering used in the test.
        corners.add(ctx.makePoint(rect.getMaxX(), rect.getMaxY())); // Q1
        corners.add(ctx.makePoint(rect.getMinX(), rect.getMaxY())); // Q2
        corners.add(ctx.makePoint(rect.getMinX(), rect.getMinY())); // Q3
        corners.add(ctx.makePoint(rect.getMaxX(), rect.getMinY())); // Q4
        return corners;
    }
}