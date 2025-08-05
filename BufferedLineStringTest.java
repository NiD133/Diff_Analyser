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
 * This test uses {@link RectIntersectionTestHelper} to perform a randomized
 * test of the spatial relationship logic of {@link BufferedLineString}. It
 * generates random line strings and random rectangles to test the `relate()`
 * method.
 */
public class BufferedLineStringTest extends RandomizedTest {

  private static final int MIN_POINTS = 2;
  private static final int MAX_POINTS = 5;

  private final SpatialContext ctx = createSpatialContext();

  private static SpatialContext createSpatialContext() {
    SpatialContextFactory factory = new SpatialContextFactory();
    factory.geo = false;
    factory.worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
    return factory.newSpatialContext();
  }

  /**
   * This test leverages the RectIntersectionTestHelper, a template-method pattern
   * for testing the `relate()` method of any Shape. We provide the logic for
   * generating random BufferedLineString instances, and the helper class runs a
   * battery of randomized relationship tests against random rectangles.
   */
  @Test
  public void testRelateWithRandomRectangles() {
    new RectIntersectionTestHelper<BufferedLineString>(ctx) {

      @Override
      protected BufferedLineString generateRandomShape(Point nearP) {
        // Create a bounding box for the line string's vertices
        Rectangle-based randomRectangle = randomRectangle(nearP);

        // Generate a random number of vertices for the line string
        int numPoints = randomIntBetween(MIN_POINTS, MAX_POINTS);
        List<Point> points = new ArrayList<>(numPoints);
        for (int i = 0; i < numPoints; i++) {
          points.add(randomPointIn(randomRectangle));
        }

        // Generate a buffer distance for the line string.
        // The buffer is based on a gaussian distribution scaled by the size of the
        // rectangle, creating a varied set of buffer sizes.
        double maxDimension = Math.max(randomRectangle.getWidth(), randomRectangle.getHeight());
        double generatedBuf = Math.abs(randomGaussian()) * maxDimension / 4.0;

        // The buffer is then converted to a "divisible" integer. This is a feature of
        // RandomizedTest to produce rounder numbers, which can help avoid floating-point
        // precision issues. This also allows for a buffer of 0.
        int intBuf = (int) divisible(generatedBuf);
        double finalBuf = randomInt(intBuf);

        return new BufferedLineString(points, finalBuf, ctx);
      }

      /**
       * Called by the test helper for shapes that have no area (i.e., buffer is 0).
       * For a line string, returning one of its vertices is a valid way to get a
       * point that is on the shape.
       */
      @Override
      protected Point randomPointInEmptyShape(BufferedLineString shape) {
        List<Point> points = shape.getPoints();
        int randomIndex = randomInt(points.size() - 1);
        return points.get(randomIndex);
      }
    }.testRelateWithRectangle();
  }
}