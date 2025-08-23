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

public class BufferedLineTest extends RandomizedTest {

  private final SpatialContext ctx = new SpatialContextFactory()
    {{geo = false; worldBounds = new RectangleImpl(-100, 100, -50, 50, null);}}.newSpatialContext();

  @Rule
  public TestLog testLog = TestLog.instance;

  /**
   * Logs the shapes of a BufferedLine and its bounding rectangle in WKT format.
   */
  public static void logShapes(final BufferedLine line, final Rectangle rect) {
    String lineWKT = String.format(
        "LINESTRING(%f %f, %f %f)", 
        line.getA().getX(), line.getA().getY(), 
        line.getB().getX(), line.getB().getY()
    );
    String rectWKT = rectToWkt(rect);
    System.out.println("GEOMETRYCOLLECTION(" + lineWKT + "," + rectWKT + ")");
    System.out.println(rectWKT);
  }

  /**
   * Converts a rectangle to WKT format.
   */
  private static String rectToWkt(Rectangle rect) {
    return String.format(
        "POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
        rect.getMinX(), rect.getMinY(),
        rect.getMaxX(), rect.getMinY(),
        rect.getMaxX(), rect.getMaxY(),
        rect.getMinX(), rect.getMaxY(),
        rect.getMinX(), rect.getMinY()
    );
  }

  /**
   * Tests the distance from a point to a BufferedLine.
   */
  @Test
  public void testDistanceToPoint() {
    // Test with negative slope
    verifyDistanceToPoint(ctx.makePoint(7, -4), ctx.makePoint(3, 2), ctx.makePoint(5, 6), 3.88290);
    // Test with positive slope
    verifyDistanceToPoint(ctx.makePoint(3, 2), ctx.makePoint(7, 5), ctx.makePoint(5, 6), 2.0);
    // Test with vertical line
    verifyDistanceToPoint(ctx.makePoint(3, 2), ctx.makePoint(3, 8), ctx.makePoint(4, 3), 1.0);
    // Test with horizontal line
    verifyDistanceToPoint(ctx.makePoint(3, 2), ctx.makePoint(6, 2), ctx.makePoint(4, 3), 1.0);
  }

  /**
   * Verifies the distance from a point to a BufferedLine.
   */
  private void verifyDistanceToPoint(Point pA, Point pB, Point pC, double expectedDistance) {
    if (expectedDistance > 0) {
      assertFalse(new BufferedLine(pA, pB, expectedDistance * 0.999, ctx).contains(pC));
    } else {
      assert expectedDistance == 0;
      assertTrue(new BufferedLine(pA, pB, 0, ctx).contains(pC));
    }
    assertTrue(new BufferedLine(pA, pB, expectedDistance * 1.001, ctx).contains(pC));
  }

  /**
   * Tests miscellaneous scenarios for BufferedLine.
   */
  @Test
  public void testMiscellaneous() {
    Point point = ctx.makePoint(10, 1);
    BufferedLine line = new BufferedLine(point, point, 3, ctx);
    assertTrue(line.contains(ctx.makePoint(10, 1 + 3 - 0.1)));
    assertFalse(line.contains(ctx.makePoint(10, 1 + 3 + 0.1)));
  }

  /**
   * Tests the quadrant calculation for a BufferedLine.
   */
  @Test
  @Repeat(iterations = 15)
  public void testQuadrantCalculation() {
    BufferedLine line = createRandomLine();
    Rectangle boundingBox = createRandomLine().getBoundingBox();
    ArrayList<Point> corners = getRectangleCorners(boundingBox);

    Collection<Integer> farthestDistanceQuads = new LinkedList<>();
    double farthestDistance = -1;
    int quadrant = 1;

    for (Point corner : corners) {
      double distance = line.getLinePrimary().distanceUnbuffered(corner);
      if (Math.abs(distance - farthestDistance) < 0.000001) {
        farthestDistanceQuads.add(quadrant);
      } else if (distance > farthestDistance) {
        farthestDistanceQuads.clear();
        farthestDistanceQuads.add(quadrant);
        farthestDistance = distance;
      }
      quadrant++;
    }

    int calculatedQuadrant = line.getLinePrimary().quadrant(boundingBox.getCenter());
    assertTrue(farthestDistanceQuads.contains(calculatedQuadrant));
  }

  /**
   * Creates a random BufferedLine.
   */
  private BufferedLine createRandomLine() {
    Point pA = new PointImpl(randomInt(9), randomInt(9), ctx);
    Point pB = new PointImpl(randomInt(9), randomInt(9), ctx);
    int buffer = randomInt(5);
    return new BufferedLine(pA, pB, buffer, ctx);
  }

  /**
   * Returns the corners of a rectangle.
   */
  private ArrayList<Point> getRectangleCorners(Rectangle rect) {
    ArrayList<Point> corners = new ArrayList<>(4);
    corners.add(ctx.makePoint(rect.getMaxX(), rect.getMaxY()));
    corners.add(ctx.makePoint(rect.getMinX(), rect.getMaxY()));
    corners.add(ctx.makePoint(rect.getMinX(), rect.getMinY()));
    corners.add(ctx.makePoint(rect.getMaxX(), rect.getMinY()));
    return corners;
  }

  /**
   * Tests rectangle intersection with BufferedLine.
   */
  @Test
  public void testRectangleIntersection() {
    new RectIntersectionTestHelper<BufferedLine>(ctx) {

      @Override
      protected BufferedLine generateRandomShape(Point nearPoint) {
        Rectangle nearRectangle = randomRectangle(nearPoint);
        ArrayList<Point> corners = getRectangleCorners(nearRectangle);
        int randomCornerIndex = randomInt(3);
        Point pA = corners.get(randomCornerIndex);
        Point pB = corners.get((randomCornerIndex + 2) % 4);
        double maxBuffer = Math.max(nearRectangle.getWidth(), nearRectangle.getHeight());
        double buffer = Math.abs(randomGaussian()) * maxBuffer / 4;
        buffer = randomInt((int) buffer);
        return new BufferedLine(pA, pB, buffer, ctx);
      }

      @Override
      protected Point randomPointInEmptyShape(BufferedLine shape) {
        return randomInt(1) == 0 ? shape.getA() : shape.getB();
      }
    }.testRelateWithRectangle();
  }

  /**
   * Creates a new BufferedLine with specified coordinates and buffer.
   */
  private BufferedLine createBufferedLine(int x1, int y1, int x2, int y2, int buffer) {
    Point pA = ctx.makePoint(x1, y1);
    Point pB = ctx.makePoint(x2, y2);
    return randomBoolean() ? new BufferedLine(pB, pA, buffer, ctx) : new BufferedLine(pA, pB, buffer, ctx);
  }
}