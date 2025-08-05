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
import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BufferedLineTest extends RandomizedTest {

  private final SpatialContext ctx = new SpatialContextFactory()
      {{ geo = false; worldBounds = new RectangleImpl(-100, 100, -50, 50, null); }}.newSpatialContext();

  @Rule
  public TestLog testLog = TestLog.instance;

  private static final double EPSILON = 1e-9;

  @Test
  public void testContains_withPointOnLine() {
    Point p1 = ctx.makePoint(0, 0);
    Point p2 = ctx.makePoint(10, 10);
    Point pointOnLine = ctx.makePoint(5, 5);
    BufferedLine line = new BufferedLine(p1, p2, 0, ctx);
    assertTrue("A point on a zero-buffered line should be contained", line.contains(pointOnLine));
  }

  @Test
  public void testContains_forNegativeSlopeLine() {
    Point p1 = ctx.makePoint(7, -4);
    Point p2 = ctx.makePoint(3, 2);
    Point testPoint = ctx.makePoint(5, 6);
    // The expected distance from testPoint to the line segment (p1, p2)
    double expectedDistance = 3.88290;
    assertPointContainmentAtDistanceBoundary(p1, p2, testPoint, expectedDistance);
  }

  @Test
  public void testContains_forPositiveSlopeLine() {
    Point p1 = ctx.makePoint(3, 2);
    Point p2 = ctx.makePoint(7, 5);
    Point testPoint = ctx.makePoint(5, 6);
    double expectedDistance = 2.0;
    assertPointContainmentAtDistanceBoundary(p1, p2, testPoint, expectedDistance);
  }

  @Test
  public void testContains_forVerticalLine() {
    Point p1 = ctx.makePoint(3, 2);
    Point p2 = ctx.makePoint(3, 8);
    Point testPoint = ctx.makePoint(4, 3);
    double expectedDistance = 1.0;
    assertPointContainmentAtDistanceBoundary(p1, p2, testPoint, expectedDistance);
  }

  @Test
  public void testContains_forHorizontalLine() {
    Point p1 = ctx.makePoint(3, 2);
    Point p2 = ctx.makePoint(6, 2);
    Point testPoint = ctx.makePoint(4, 3);
    double expectedDistance = 1.0;
    assertPointContainmentAtDistanceBoundary(p1, p2, testPoint, expectedDistance);
  }

  /**
   * Asserts that a point is not contained in a BufferedLine when the buffer is
   * slightly less than the point's distance to the line, and is contained when
   * the buffer is slightly more. This effectively tests the boundary condition.
   *
   * @param lineP1 Start point of the line.
   * @param lineP2 End point of the line.
   * @param externalPoint The point to test for containment.
   * @param distance The exact distance from the line to the externalPoint.
   */
  private void assertPointContainmentAtDistanceBoundary(Point lineP1, Point lineP2, Point externalPoint, double distance) {
    // A buffer just under the required distance should not contain the point.
    BufferedLine narrowLine = new BufferedLine(lineP1, lineP2, distance - EPSILON, ctx);
    assertFalse("Point should be outside the narrow buffer", narrowLine.contains(externalPoint));

    // A buffer just over the required distance should contain the point.
    BufferedLine wideLine = new BufferedLine(lineP1, lineP2, distance + EPSILON, ctx);
    assertTrue("Point should be inside the wide buffer", wideLine.contains(externalPoint));
  }

  /**
   * Tests that a BufferedLine with identical start and end points (a zero-length line)
   * behaves like a circle, where the buffer is the radius.
   */
  @Test
  public void testContains_whenLineIsPoint_behavesAsCircle() {
    Point center = ctx.makePoint(10, 1);
    double buffer = 3.0;
    BufferedLine lineAsCircle = new BufferedLine(center, center, buffer, ctx);

    // Test a point just inside the buffer radius
    Point pointInside = ctx.makePoint(10, 1 + buffer - 0.1);
    assertTrue("Point just inside buffer should be contained", lineAsCircle.contains(pointInside));

    // Test a point just outside the buffer radius
    Point pointOutside = ctx.makePoint(10, 1 + buffer + 0.1);
    assertFalse("Point just outside buffer should not be contained", lineAsCircle.contains(pointOutside));
  }

  /**
   * This is a randomized test verifying the internal logic of {@code InfBufLine.quadrant(Point)}.
   * The {@code quadrant} method is designed to identify which of the four quadrants a point
   * lies in, relative to the line. This is particularly useful for determining which corner
   * of a bounding box is farthest from the line.
   * <p>
   * The test works as follows:
   * <ol>
   * <li>A random line and a random rectangle are generated.</li>
   * <li>It calculates the distance from the un-buffered line to each of the rectangle's four corners.</li>
   * <li>It identifies which corner(s) are farthest from the line. These corners define the
   *    "farthest quadrants".</li>
   * <li>It calls the {@code line.getLinePrimary().quadrant(rect.getCenter())} method.</li>
   * <li>It asserts that the quadrant returned by the method is one of the farthest quadrants,
   *    confirming the method correctly identifies the region farthest from the line.</li>
   * </ol>
   */
  @Test
  @Repeat(iterations = 15)
  public void testQuadrantOfPoint_matchesFarthestCornerQuadrant() {
    BufferedLine line = newRandomLine();
    // This test logic doesn't handle zero-length lines correctly.
    if (line.getA().equals(line.getB())) {
      return;
    }
    Rectangle rect = newRandomLine().getBoundingBox();

    // Brute-force find the farthest corner(s) of the rectangle from the line.
    ArrayList<Point> corners = getCornersOfRectangle(rect);
    Collection<Integer> farthestQuadrants = new LinkedList<>();
    double maxDistance = -1;

    // Quadrants are 1-based: 1:NE, 2:NW, 3:SW, 4:SE
    int cornerQuadrant = 1;
    for (Point corner : corners) {
      double distance = line.getLinePrimary().distanceUnbuffered(corner);
      if (Math.abs(distance - maxDistance) < 1e-6) { // Handle ties
        farthestQuadrants.add(cornerQuadrant);
      } else if (distance > maxDistance) {
        maxDistance = distance;
        farthestQuadrants.clear();
        farthestQuadrants.add(cornerQuadrant);
      }
      cornerQuadrant++;
    }

    // Get the quadrant calculated by the method under test for the rectangle's center.
    int quadrantOfCenter = line.getLinePrimary().quadrant(rect.getCenter());

    // The quadrant of the center point should correspond to the farthest corner's quadrant.
    assertTrue("The calculated quadrant of the center " + quadrantOfCenter +
            " should be one of the farthest quadrants " + farthestQuadrants,
        farthestQuadrants.contains(quadrantOfCenter));
  }

  private BufferedLine newRandomLine() {
    Point pA = new PointImpl(randomInt(9), randomInt(9), ctx);
    Point pB = new PointImpl(randomInt(9), randomInt(9), ctx);
    int buf = randomInt(5);
    return new BufferedLine(pA, pB, buf, ctx);
  }

  private ArrayList<Point> getCornersOfRectangle(Rectangle rect) {
    ArrayList<Point> corners = new ArrayList<>(4);
    corners.add(ctx.makePoint(rect.getMaxX(), rect.getMaxY())); // Q1: North-East
    corners.add(ctx.makePoint(rect.getMinX(), rect.getMaxY())); // Q2: North-West
    corners.add(ctx.makePoint(rect.getMinX(), rect.getMinY())); // Q3: South-West
    corners.add(ctx.makePoint(rect.getMaxX(), rect.getMinY())); // Q4: South-East
    return corners;
  }

  @Test
  public void testRelateWithRectangle() {
    new RectIntersectionTestHelper<BufferedLine>(ctx) {

      @Override
      protected BufferedLine generateRandomShape(Point nearP) {
        // Create a random rectangle near the provided point.
        Rectangle randomRect = randomRectangle(nearP);
        ArrayList<Point> corners = getCornersOfRectangle(randomRect);

        // Create a line segment along one of the diagonals of the random rectangle.
        int cornerIdx = randomInt(3); // 0..3
        Point pA = corners.get(cornerIdx);
        Point pB = corners.get((cornerIdx + 2) % 4); // Opposite corner

        // Generate a random buffer for the line.
        double maxBuf = Math.max(randomRect.getWidth(), randomRect.getHeight());
        double buf = Math.abs(randomGaussian()) * maxBuf / 4.0;
        buf = randomInt((int) divisible(buf));

        return new BufferedLine(pA, pB, buf, ctx);
      }

      @Override
      protected Point randomPointInEmptyShape(BufferedLine shape) {
        // For a BufferedLine, the endpoints are guaranteed to be on the boundary.
        // We return one of them randomly.
        return randomBoolean() ? shape.getA() : shape.getB();
      }
    }.testRelateWithRectangle();
  }
}