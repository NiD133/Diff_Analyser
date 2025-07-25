/*******************************************************************************
 * Copyright (c) 2015 David Smiley
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.shape.impl;

import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.RandomizedShapeTest;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the BBoxCalculator class in a geo space.
 */
public class BBoxCalculatorTest extends RandomizedShapeTest {

  public BBoxCalculatorTest() {
    super(SpatialContext.GEO);
  }

  /**
   * Verifies that the BBoxCalculator correctly calculates the bounding box for a set of rectangles in a geo space.
   */
  @Test
  @Repeat(iterations = 100)
  public void testGeoLongitude() {
    // Create a BBoxCalculator instance
    BBoxCalculator calc = new BBoxCalculator(ctx);

    // Generate a random number of rectangles (between 1 and 4)
    int numShapes = randomIntBetween(1, 4);
    List<Rectangle> rects = new ArrayList<>(numShapes);

    // Expand the range for each rectangle
    for (int i = 0; i < numShapes; i++) {
      Rectangle rect = randomRectangle(30); // 30 is divisible by
      rects.add(rect);
      calc.expandRange(rect);
    }

    // Get the calculated boundary
    Rectangle boundary = calc.getBoundary();

    // Test the boundary when there is only one rectangle
    testSingleRectangleBoundary(rects, boundary);

    // Test the boundary when it covers the entire world
    testWorldBoundary(rects, boundary);

    // Test that the boundary contains all rectangles
    testContainsAllRectangles(rects, boundary);

    // Test that the left and right boundaries are correct
    testBoundaryEdges(rects, boundary);

    // Test that the calculated boundary is the smallest enclosing boundary
    testSmallestEnclosingBoundary(rects, boundary);
  }

  /**
   * Tests the boundary when there is only one rectangle.
   */
  private void testSingleRectangleBoundary(List<Rectangle> rects, Rectangle boundary) {
    if (rects.size() == 1) {
      assertEquals(rects.get(0), boundary);
    }
  }

  /**
   * Tests the boundary when it covers the entire world.
   */
  private void testWorldBoundary(List<Rectangle> rects, Rectangle boundary) {
    if (boundary.getMinX() == -180 && boundary.getMaxX() == 180) {
      // Each longitude should be present in at least one rectangle
      for (int lon = -180; lon <= +180; lon++) {
        assertTrue(atLeastOneRectHasLon(rects, lon));
      }
    }
  }

  /**
   * Tests that the boundary contains all rectangles.
   */
  private void testContainsAllRectangles(List<Rectangle> rects, Rectangle boundary) {
    for (Rectangle rect : rects) {
      assertRelation(SpatialRelation.CONTAINS, boundary, rect);
    }
  }

  /**
   * Tests that the left and right boundaries are correct.
   */
  private void testBoundaryEdges(List<Rectangle> rects, Rectangle boundary) {
    assertTrue(atLeastOneRectHasLon(rects, boundary.getMinX()));
    assertFalse(atLeastOneRectHasLon(rects, normX(boundary.getMinX() - 0.5)));

    assertTrue(atLeastOneRectHasLon(rects, boundary.getMaxX()));
    assertFalse(atLeastOneRectHasLon(rects, normX(boundary.getMaxX() + 0.5)));
  }

  /**
   * Tests that the calculated boundary is the smallest enclosing boundary.
   */
  private void testSmallestEnclosingBoundary(List<Rectangle> rects, Rectangle boundary) {
    if (boundary.getWidth() > 180) {
      double biggerGap = 360.0 - boundary.getWidth() + 0.5;
      for (Rectangle rect : rects) {
        double gapRectLeft = rect.getMaxX() + 0.25;
        double gapRectRight = gapRectLeft + biggerGap;
        Rectangle testGap = makeNormRect(gapRectLeft, gapRectRight, -90, 90);
        boolean fits = true;
        for (Rectangle rect2 : rects) {
          if (rect2.relate(testGap).intersects()) {
            fits = false;
            break;
          }
        }
        assertFalse(fits); // Should never fit because it's larger than the biggest gap
      }
    }
  }

  /**
   * Checks if at least one rectangle has a specific longitude.
   */
  private boolean atLeastOneRectHasLon(List<Rectangle> rects, double lon) {
    for (Rectangle rect : rects) {
      if (rect.relateXRange(lon, lon).intersects()) {
        return true;
      }
    }
    return false;
  }
}