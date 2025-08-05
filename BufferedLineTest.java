/*******************************************************************************
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BufferedLineTest extends RandomizedTest {

    private final SpatialContext ctx = new SpatialContextFactory()
        {{ geo = false; worldBounds = new RectangleImpl(-100, 100, -50, 50, null); }}.newSpatialContext();

    @Rule
    public TestLog testLog = TestLog.instance;

    // Test parameters for distance tests
    private final Point start;
    private final Point end;
    private final Point testPoint;
    private final double expectedDistance;

    public BufferedLineTest(Point start, Point end, Point testPoint, double expectedDistance) {
        this.start = start;
        this.end = end;
        this.testPoint = testPoint;
        this.expectedDistance = expectedDistance;
    }

    @Parameters(name = "Line from ({0}) to ({1}) - Point {2} should have distance {3}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
            // Negative slope
            { makePoint(7, -4), makePoint(3, 2), makePoint(5, 6), 3.88290 },
            // Positive slope
            { makePoint(3, 2), makePoint(7, 5), makePoint(5, 6), 2.0 },
            // Vertical line
            { makePoint(3, 2), makePoint(3, 8), makePoint(4, 3), 1.0 },
            // Horizontal line
            { makePoint(3, 2), makePoint(6, 2), makePoint(4, 3), 1.0 }
        });
    }

    private static Point makePoint(double x, double y) {
        return SpatialContext.GEO.getShapeFactory().pointXY(x, y);
    }

    @Test
    public void testDistanceToPointWithVariousLineSlopes() {
        // Test that a buffered line with a buffer slightly smaller doesn't contain the point
        if (expectedDistance > 0) {
            assertLineContainsPoint(start, end, expectedDistance * 0.999, testPoint, false);
        } else {
            assertLineContainsPoint(start, end, 0, testPoint, true);
        }
        // Test that a buffered line with a buffer slightly larger contains the point
        assertLineContainsPoint(start, end, expectedDistance * 1.001, testPoint, true);
    }

    private void assertLineContainsPoint(Point start, Point end, double buffer, Point testPoint, boolean shouldContain) {
        BufferedLine line = new BufferedLine(start, end, buffer, ctx);
        if (shouldContain) {
            assertTrue("Point should be within buffered line", line.contains(testPoint));
        } else {
            assertFalse("Point should not be within buffered line", line.contains(testPoint));
        }
    }

    @Test
    public void testLineWithIdenticalPoints() {
        // Tests behavior when line start and end are the same point
        Point point = ctx.makePoint(10, 1);
        double buffer = 3;
        BufferedLine line = new BufferedLine(point, point, buffer, ctx);

        // Point within buffer distance
        assertTrue(line.contains(ctx.makePoint(10, 1 + buffer - 0.1)));
        // Point outside buffer distance
        assertFalse(line.contains(ctx.makePoint(10, 1 + buffer + 0.1)));
    }

    @Test
    @Repeat(iterations = 15)
    public void testFarthestQuadrantFromLineCenter() {
        // Generate random line and rectangle
        BufferedLine line = generateRandomLine();
        Rectangle rect = generateRandomLine().getBoundingBox();

        // Calculate farthest corners from the line
        List<Point> corners = getRectangleCorners(rect);
        double maxDistance = -1;
        Collection<Integer> farthestQuadrants = new LinkedList<>();

        // Find corners with maximum distance to the line
        for (int i = 0; i < corners.size(); i++) {
            Point corner = corners.get(i);
            double distance = line.getLinePrimary().distanceUnbuffered(corner);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestQuadrants.clear();
                farthestQuadrants.add(i + 1); // Quadrants are 1-indexed
            } else if (Math.abs(distance - maxDistance) < 0.000001) {
                farthestQuadrants.add(i + 1); // Handle ties
            }
        }

        // Verify calculated quadrant matches expected farthest quadrant
        int calculatedQuadrant = line.getLinePrimary().quadrant(rect.getCenter());
        assertTrue(
            "Calculated quadrant should be one of the farthest",
            farthestQuadrants.contains(calculatedQuadrant)
        );
    }

    private BufferedLine generateRandomLine() {
        Point start = new PointImpl(randomInt(9), randomInt(9), ctx);
        Point end = new PointImpl(randomInt(9), randomInt(9), ctx);
        double buffer = randomInt(5);
        return new BufferedLine(start, end, buffer, ctx);
    }

    private List<Point> getRectangleCorners(Rectangle rect) {
        return Arrays.asList(
            ctx.makePoint(rect.getMaxX(), rect.getMaxY()), // NE (quadrant 1)
            ctx.makePoint(rect.getMinX(), rect.getMaxY()), // NW (quadrant 2)
            ctx.makePoint(rect.getMinX(), rect.getMinY()), // SW (quadrant 3)
            ctx.makePoint(rect.getMaxX(), rect.getMinY())  // SE (quadrant 4)
        );
    }

    @Test
    public void testRandomRectangleIntersection() {
        new RectIntersectionTestHelper<BufferedLine>(ctx) {

            @Override
            protected BufferedLine generateRandomShape(Point nearPoint) {
                Rectangle nearRect = randomRectangle(nearPoint);
                List<Point> corners = getRectangleCorners(nearRect);
                int cornerIndex = randomInt(3);
                Point start = corners.get(cornerIndex);
                Point end = corners.get((cornerIndex + 2) % 4); // Opposite corner
                double maxBuffer = Math.max(nearRect.getWidth(), nearRect.getHeight());
                double buffer = Math.abs(randomGaussian()) * maxBuffer / 4;
                buffer = randomInt((int) divisible(buffer));
                return new BufferedLine(start, end, buffer, ctx);
            }

            @Override
            protected Point randomPointInEmptyShape(BufferedLine shape) {
                // Return one of the endpoints (simple representation of "inside" the line)
                return randomBoolean() ? shape.getA() : shape.getB();
            }
        }.testRelateWithRectangle();
    }

    private BufferedLine createBufferedLine(int x1, int y1, int x2, int y2, int buffer) {
        Point start = ctx.makePoint(x1, y1);
        Point end = ctx.makePoint(x2, y2);
        // Randomize point order to test both directions
        if (randomBoolean()) {
            return new BufferedLine(end, start, buffer, ctx);
        }
        return new BufferedLine(start, end, buffer, ctx);
    }
}