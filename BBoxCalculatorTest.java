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

public class BBoxCalculatorTest extends RandomizedShapeTest {

  public BBoxCalculatorTest() {
    super(SpatialContext.GEO);
  }

  /**
   * Test the calculation of the bounding box for random rectangles on the geodetic longitude.
   * This test is repeated 100 times to ensure robustness.
   */
  @Test
  @Repeat(iterations = 100)
  public void testGeoLongitude() {
    BBoxCalculator bboxCalculator = new BBoxCalculator(ctx);
    final int numberOfRectangles = randomIntBetween(1, 4);
    List<Rectangle> rectangles = new ArrayList<>(numberOfRectangles);

    // Create random rectangles and expand the bounding box to include them
    for (int i = 0; i < numberOfRectangles; i++) {
      Rectangle rectangle = randomRectangle(30);
      rectangles.add(rectangle);
      bboxCalculator.expandRange(rectangle);
    }

    Rectangle calculatedBoundary = bboxCalculator.getBoundary();

    // If only one rectangle, the boundary should be the rectangle itself
    if (numberOfRectangles == 1) {
      assertEquals(rectangles.get(0), calculatedBoundary);
      return;
    }

    // Check if the boundary covers the entire world longitude range
    if (isWorldLongitudeBoundary(calculatedBoundary)) {
      assertWorldLongitudeCoverage(rectangles);
      return;
    }

    // Verify that the boundary contains all rectangles
    assertBoundaryContainsAllRectangles(calculatedBoundary, rectangles);

    // Verify that the boundary edges are correct
    assertBoundaryEdges(calculatedBoundary, rectangles);

    // Verify that the boundary is the smallest possible enclosing boundary
    assertSmallestEnclosingBoundary(calculatedBoundary, rectangles);
  }

  private boolean isWorldLongitudeBoundary(Rectangle boundary) {
    return boundary.getMinX() == -180 && boundary.getMaxX() == 180;
  }

  private void assertWorldLongitudeCoverage(List<Rectangle> rectangles) {
    for (int lon = -180; lon <= 180; lon++) {
      assertTrue(atLeastOneRectangleCoversLongitude(rectangles, lon));
    }
  }

  private void assertBoundaryContainsAllRectangles(Rectangle boundary, List<Rectangle> rectangles) {
    for (Rectangle rectangle : rectangles) {
      assertRelation(SpatialRelation.CONTAINS, boundary, rectangle);
    }
  }

  private void assertBoundaryEdges(Rectangle boundary, List<Rectangle> rectangles) {
    assertTrue(atLeastOneRectangleCoversLongitude(rectangles, boundary.getMinX()));
    assertFalse(atLeastOneRectangleCoversLongitude(rectangles, normX(boundary.getMinX() - 0.5)));

    assertTrue(atLeastOneRectangleCoversLongitude(rectangles, boundary.getMaxX()));
    assertFalse(atLeastOneRectangleCoversLongitude(rectangles, normX(boundary.getMaxX() + 0.5)));
  }

  private void assertSmallestEnclosingBoundary(Rectangle boundary, List<Rectangle> rectangles) {
    if (boundary.getWidth() > 180) {
      double largerGap = 360.0 - boundary.getWidth() + 0.5;
      for (Rectangle rectangle : rectangles) {
        double gapStart = rectangle.getMaxX() + 0.25;
        double gapEnd = gapStart + largerGap;
        Rectangle testGap = makeNormRect(gapStart, gapEnd, -90, 90);
        assertFalse(isGapFittingInAnyRectangle(testGap, rectangles));
      }
    }
  }

  private boolean atLeastOneRectangleCoversLongitude(List<Rectangle> rectangles, double longitude) {
    for (Rectangle rectangle : rectangles) {
      if (rectangle.relateXRange(longitude, longitude).intersects()) {
        return true;
      }
    }
    return false;
  }

  private boolean isGapFittingInAnyRectangle(Rectangle gap, List<Rectangle> rectangles) {
    for (Rectangle rectangle : rectangles) {
      if (rectangle.relate(gap).intersects()) {
        return true;
      }
    }
    return false;
  }
}