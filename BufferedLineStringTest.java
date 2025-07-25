/*******************************************************************************
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test suite for the BufferedLineString class.
 */
public class BufferedLineStringTest extends RandomizedTest {

  // Spatial context with specific world bounds
  private final SpatialContext spatialContext = new SpatialContextFactory() {{
    geo = false;
    worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
  }}.newSpatialContext();

  /**
   * Tests the intersection of BufferedLineString with rectangles.
   */
  @Test
  public void testRectangleIntersection() {
    new RectIntersectionTestHelper<BufferedLineString>(spatialContext) {

      /**
       * Generates a random BufferedLineString near a given point.
       *
       * @param nearPoint The point near which the BufferedLineString is generated.
       * @return A randomly generated BufferedLineString.
       */
      @Override
      protected BufferedLineString generateRandomShape(Point nearPoint) {
        Rectangle nearbyRectangle = randomRectangle(nearPoint);
        int numberOfPoints = 2 + randomInt(3); // Generates between 2 to 5 points

        List<Point> points = new ArrayList<>(numberOfPoints);
        while (points.size() < numberOfPoints) {
          points.add(randomPointIn(nearbyRectangle));
        }

        double maxBuffer = Math.max(nearbyRectangle.getWidth(), nearbyRectangle.getHeight());
        double buffer = Math.abs(randomGaussian()) * maxBuffer / 4;
        buffer = randomInt((int) Math.ceil(buffer));

        return new BufferedLineString(points, buffer, spatialContext);
      }

      /**
       * Selects a random point from the points of a BufferedLineString.
       *
       * @param shape The BufferedLineString from which to select a point.
       * @return A random point from the BufferedLineString.
       */
      protected Point randomPointInEmptyShape(BufferedLineString shape) {
        List<Point> points = shape.getPoints();
        return points.get(randomInt(points.size() - 1));
      }
    }.testRelateWithRectangle();
  }
}