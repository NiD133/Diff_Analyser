package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BufferedLineTest extends RandomizedTest {

    private final SpatialContext context = new SpatialContextFactory() {{
        geo = false;
        worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
    }}.newSpatialContext();

    @Rule
    public TestLog testLog = TestLog.instance;

    @Test
    public void testDistanceToPoint() {
        // Test lines with different slopes and orientations
        verifyDistanceToPoint(context.makePoint(7, -4), context.makePoint(3, 2), context.makePoint(5, 6), 3.88290);
        verifyDistanceToPoint(context.makePoint(3, 2), context.makePoint(7, 5), context.makePoint(5, 6), 2.0);
        verifyDistanceToPoint(context.makePoint(3, 2), context.makePoint(3, 8), context.makePoint(4, 3), 1.0);
        verifyDistanceToPoint(context.makePoint(3, 2), context.makePoint(6, 2), context.makePoint(4, 3), 1.0);
    }

    private void verifyDistanceToPoint(Point start, Point end, Point testPoint, double expectedDistance) {
        if (expectedDistance > 0) {
            assertFalse(new BufferedLine(start, end, expectedDistance * 0.999, context).contains(testPoint));
        } else {
            assertTrue(new BufferedLine(start, end, 0, context).contains(testPoint));
        }
        assertTrue(new BufferedLine(start, end, expectedDistance * 1.001, context).contains(testPoint));
    }

    @Test
    public void testPointOnBufferedLine() {
        Point point = context.makePoint(10, 1);
        BufferedLine line = new BufferedLine(point, point, 3, context);
        assertTrue(line.contains(context.makePoint(10, 1 + 3 - 0.1)));
        assertFalse(line.contains(context.makePoint(10, 1 + 3 + 0.1)));
    }

    @Test
    @Repeat(iterations = 15)
    public void testQuadrantCalculation() {
        BufferedLine line = createRandomBufferedLine();
        Rectangle boundingBox = createRandomBufferedLine().getBoundingBox();
        ArrayList<Point> corners = getRectangleCorners(boundingBox);

        Collection<Integer> farthestQuadrants = new LinkedList<>();
        double maxDistance = -1;
        int quadrant = 1;

        for (Point corner : corners) {
            double distance = line.getLinePrimary().distanceUnbuffered(corner);
            if (Math.abs(distance - maxDistance) < 0.000001) {
                farthestQuadrants.add(quadrant);
            } else if (distance > maxDistance) {
                farthestQuadrants.clear();
                farthestQuadrants.add(quadrant);
                maxDistance = distance;
            }
            quadrant++;
        }

        int calculatedQuadrant = line.getLinePrimary().quadrant(boundingBox.getCenter());
        assertTrue(farthestQuadrants.contains(calculatedQuadrant));
    }

    private BufferedLine createRandomBufferedLine() {
        Point start = new PointImpl(randomInt(9), randomInt(9), context);
        Point end = new PointImpl(randomInt(9), randomInt(9), context);
        int buffer = randomInt(5);
        return new BufferedLine(start, end, buffer, context);
    }

    private ArrayList<Point> getRectangleCorners(Rectangle rectangle) {
        ArrayList<Point> corners = new ArrayList<>(4);
        corners.add(context.makePoint(rectangle.getMaxX(), rectangle.getMaxY()));
        corners.add(context.makePoint(rectangle.getMinX(), rectangle.getMaxY()));
        corners.add(context.makePoint(rectangle.getMinX(), rectangle.getMinY()));
        corners.add(context.makePoint(rectangle.getMaxX(), rectangle.getMinY()));
        return corners;
    }

    @Test
    public void testRectangleIntersection() {
        new RectIntersectionTestHelper<BufferedLine>(context) {

            @Override
            protected BufferedLine generateRandomShape(Point nearPoint) {
                Rectangle nearRectangle = randomRectangle(nearPoint);
                ArrayList<Point> corners = getRectangleCorners(nearRectangle);
                int randomCornerIndex = randomInt(3);
                Point start = corners.get(randomCornerIndex);
                Point end = corners.get((randomCornerIndex + 2) % 4);
                double maxBuffer = Math.max(nearRectangle.getWidth(), nearRectangle.getHeight());
                double buffer = Math.abs(randomGaussian()) * maxBuffer / 4;
                buffer = randomInt((int) buffer);
                return new BufferedLine(start, end, buffer, context);
            }

            @Override
            protected Point randomPointInEmptyShape(BufferedLine shape) {
                return randomInt(1) == 0 ? shape.getA() : shape.getB();
            }
        }.testRelateWithRectangle();
    }

    private BufferedLine createBufferedLine(int x1, int y1, int x2, int y2, int buffer) {
        Point start = context.makePoint(x1, y1);
        Point end = context.makePoint(x2, y2);
        return randomBoolean() ? new BufferedLine(end, start, buffer, context) : new BufferedLine(start, end, buffer, context);
    }
}