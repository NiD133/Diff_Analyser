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

/**
 * Test suite for BufferedLine functionality including distance calculations,
 * containment checks, and spatial relationship operations.
 */
public class BufferedLineTest extends RandomizedTest {

    // Non-geographic spatial context with custom world bounds for testing
    private final SpatialContext spatialContext = createTestSpatialContext();

    @Rule
    public TestLog testLog = TestLog.instance;

    /**
     * Creates a non-geographic spatial context with bounds from -100 to 100 (x) and -50 to 50 (y).
     */
    private SpatialContext createTestSpatialContext() {
        return new SpatialContextFactory() {{
            geo = false;
            worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
        }}.newSpatialContext();
    }

    /**
     * Tests distance calculations from points to buffered lines with various orientations.
     */
    @Test
    public void testDistanceCalculations() {
        // Test line with negative slope
        testPointDistanceToLine(
            spatialContext.makePoint(7, -4),  // line start
            spatialContext.makePoint(3, 2),   // line end
            spatialContext.makePoint(5, 6),   // test point
            3.88290                           // expected distance
        );

        // Test line with positive slope
        testPointDistanceToLine(
            spatialContext.makePoint(3, 2),   // line start
            spatialContext.makePoint(7, 5),   // line end
            spatialContext.makePoint(5, 6),   // test point
            2.0                               // expected distance
        );

        // Test vertical line
        testPointDistanceToLine(
            spatialContext.makePoint(3, 2),   // line start
            spatialContext.makePoint(3, 8),   // line end
            spatialContext.makePoint(4, 3),   // test point
            1.0                               // expected distance
        );

        // Test horizontal line
        testPointDistanceToLine(
            spatialContext.makePoint(3, 2),   // line start
            spatialContext.makePoint(6, 2),   // line end
            spatialContext.makePoint(4, 3),   // test point
            1.0                               // expected distance
        );
    }

    /**
     * Verifies that a buffered line correctly contains or excludes a point based on distance.
     */
    private void testPointDistanceToLine(Point lineStart, Point lineEnd, Point testPoint, double expectedDistance) {
        if (expectedDistance > 0) {
            // Point should NOT be contained when buffer is slightly smaller than distance
            double smallerBuffer = expectedDistance * 0.999;
            BufferedLine lineWithSmallerBuffer = new BufferedLine(lineStart, lineEnd, smallerBuffer, spatialContext);
            assertFalse("Point should not be contained with buffer smaller than distance", 
                       lineWithSmallerBuffer.contains(testPoint));
        } else {
            // When distance is exactly 0, point should be contained with zero buffer
            BufferedLine lineWithZeroBuffer = new BufferedLine(lineStart, lineEnd, 0, spatialContext);
            assertTrue("Point should be contained when distance is zero", 
                      lineWithZeroBuffer.contains(testPoint));
        }

        // Point should be contained when buffer is slightly larger than distance
        double largerBuffer = expectedDistance * 1.001;
        BufferedLine lineWithLargerBuffer = new BufferedLine(lineStart, lineEnd, largerBuffer, spatialContext);
        assertTrue("Point should be contained with buffer larger than distance", 
                  lineWithLargerBuffer.contains(testPoint));
    }

    /**
     * Tests edge cases and miscellaneous scenarios.
     */
    @Test
    public void testEdgeCases() {
        testZeroLengthLine();
    }

    /**
     * Tests behavior when line start and end points are identical (zero-length line).
     */
    private void testZeroLengthLine() {
        Point singlePoint = spatialContext.makePoint(10, 1);
        double bufferDistance = 3.0;
        BufferedLine zeroLengthLine = new BufferedLine(singlePoint, singlePoint, bufferDistance, spatialContext);

        // Point just inside the buffer should be contained
        Point pointInsideBuffer = spatialContext.makePoint(10, 1 + bufferDistance - 0.1);
        assertTrue("Point inside buffer of zero-length line should be contained", 
                  zeroLengthLine.contains(pointInsideBuffer));

        // Point just outside the buffer should not be contained
        Point pointOutsideBuffer = spatialContext.makePoint(10, 1 + bufferDistance + 0.1);
        assertFalse("Point outside buffer of zero-length line should not be contained", 
                   zeroLengthLine.contains(pointOutsideBuffer));
    }

    /**
     * Tests quadrant calculations for buffered lines with random geometries.
     * Verifies that the calculated closest quadrant matches the brute-force result.
     */
    @Test
    @Repeat(iterations = 15)
    public void testQuadrantCalculations() {
        BufferedLine testLine = createRandomBufferedLine();
        Rectangle testRectangle = createRandomBufferedLine().getBoundingBox();

        Collection<Integer> expectedFarthestQuadrants = findFarthestDistanceQuadrantsBruteForce(testLine, testRectangle);
        int calculatedClosestQuadrant = testLine.getLinePrimary().quadrant(testRectangle.getCenter());

        assertTrue("Calculated quadrant should match brute-force result", 
                  expectedFarthestQuadrants.contains(calculatedClosestQuadrant));
    }

    /**
     * Finds quadrants with the farthest distance using brute-force calculation.
     */
    private Collection<Integer> findFarthestDistanceQuadrantsBruteForce(BufferedLine line, Rectangle rectangle) {
        ArrayList<Point> corners = getRectangleCorners(rectangle);
        Collection<Integer> farthestQuadrants = new LinkedList<>();
        double farthestDistance = -1;
        final double DISTANCE_TOLERANCE = 0.000001;

        for (int quadrantIndex = 0; quadrantIndex < corners.size(); quadrantIndex++) {
            Point corner = corners.get(quadrantIndex);
            double distance = line.getLinePrimary().distanceUnbuffered(corner);

            if (Math.abs(distance - farthestDistance) < DISTANCE_TOLERANCE) {
                // Distances are approximately equal
                farthestQuadrants.add(quadrantIndex + 1); // quadrants are 1-indexed
            } else if (distance > farthestDistance) {
                // Found a new farthest distance
                farthestQuadrants.clear();
                farthestQuadrants.add(quadrantIndex + 1);
                farthestDistance = distance;
            }
        }

        return farthestQuadrants;
    }

    /**
     * Creates a buffered line with random coordinates and buffer size.
     */
    private BufferedLine createRandomBufferedLine() {
        Point startPoint = new PointImpl(randomInt(9), randomInt(9), spatialContext);
        Point endPoint = new PointImpl(randomInt(9), randomInt(9), spatialContext);
        int bufferSize = randomInt(5);
        return new BufferedLine(startPoint, endPoint, bufferSize, spatialContext);
    }

    /**
     * Returns the four corners of a rectangle in clockwise order starting from top-right.
     */
    private ArrayList<Point> getRectangleCorners(Rectangle rectangle) {
        ArrayList<Point> corners = new ArrayList<>(4);
        corners.add(spatialContext.makePoint(rectangle.getMaxX(), rectangle.getMaxY())); // top-right
        corners.add(spatialContext.makePoint(rectangle.getMinX(), rectangle.getMaxY())); // top-left
        corners.add(spatialContext.makePoint(rectangle.getMinX(), rectangle.getMinY())); // bottom-left
        corners.add(spatialContext.makePoint(rectangle.getMaxX(), rectangle.getMinY())); // bottom-right
        return corners;
    }

    /**
     * Tests spatial relationship calculations between buffered lines and rectangles.
     */
    @Test
    public void testRectangleIntersections() {
        new RectIntersectionTestHelper<BufferedLine>(spatialContext) {

            @Override
            protected BufferedLine generateRandomShape(Point nearPoint) {
                return createRandomBufferedLineNear(nearPoint);
            }

            @Override
            protected Point randomPointInEmptyShape(BufferedLine shape) {
                // Return one of the line endpoints randomly
                return randomBoolean() ? shape.getA() : shape.getB();
            }
        }.testRelateWithRectangle();
    }

    /**
     * Creates a random buffered line near the specified point.
     */
    private BufferedLine createRandomBufferedLineNear(Point nearPoint) {
        Rectangle nearRectangle = randomRectangle(nearPoint);
        ArrayList<Point> corners = getRectangleCorners(nearRectangle);
        
        // Select two opposite corners
        int cornerIndex = randomInt(3); // 0..3
        Point startPoint = corners.get(cornerIndex);
        Point endPoint = corners.get((cornerIndex + 2) % 4); // opposite corner
        
        // Calculate buffer size based on rectangle dimensions
        double maxDimension = Math.max(nearRectangle.getWidth(), nearRectangle.getHeight());
        double bufferSize = Math.abs(randomGaussian()) * maxDimension / 4;
        bufferSize = randomInt((int) divisible(bufferSize));
        
        return new BufferedLine(startPoint, endPoint, bufferSize, spatialContext);
    }

    /**
     * Creates a buffered line with specified coordinates and buffer, with random point order.
     */
    private BufferedLine createBufferedLine(int x1, int y1, int x2, int y2, int bufferSize) {
        Point pointA = spatialContext.makePoint(x1, y1);
        Point pointB = spatialContext.makePoint(x2, y2);
        
        // Randomly swap point order to test order independence
        if (randomBoolean()) {
            return new BufferedLine(pointB, pointA, bufferSize, spatialContext);
        } else {
            return new BufferedLine(pointA, pointB, bufferSize, spatialContext);
        }
    }

    // Utility methods for debugging and visualization

    /**
     * Logs shapes in WKT format for debugging purposes.
     */
    public static void logShapesForDebugging(final BufferedLine line, final Rectangle rect) {
        String lineWKT = createLineWKT(line);
        String boundingBoxWKT = convertRectangleToWKT(line.getBoundingBox());
        System.out.println("GEOMETRYCOLLECTION(" + lineWKT + "," + boundingBoxWKT + ")");
        
        String rectWKT = convertRectangleToWKT(rect);
        System.out.println(rectWKT);
    }

    /**
     * Creates WKT representation of a line.
     */
    private static String createLineWKT(BufferedLine line) {
        return "LINESTRING(" + 
               line.getA().getX() + " " + line.getA().getY() + "," +
               line.getB().getX() + " " + line.getB().getY() + ")";
    }

    /**
     * Converts a rectangle to WKT polygon format.
     */
    private static String convertRectangleToWKT(Rectangle rect) {
        return "POLYGON((" + 
               rect.getMinX() + " " + rect.getMinY() + "," +
               rect.getMaxX() + " " + rect.getMinY() + "," +
               rect.getMaxX() + " " + rect.getMaxY() + "," +
               rect.getMinX() + " " + rect.getMaxY() + "," +
               rect.getMinX() + " " + rect.getMinY() + "))";
    }
}