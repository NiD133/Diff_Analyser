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
 * Tests for BBoxCalculator which computes minimum bounding boxes for geographic shapes.
 * Focuses on longitude calculations since latitude calculations are trivial.
 */
public class BBoxCalculatorTest extends RandomizedShapeTest {

  private static final double WORLD_MIN_LON = -180;
  private static final double WORLD_MAX_LON = 180;
  private static final double WORLD_MIN_LAT = -90;
  private static final double WORLD_MAX_LAT = 90;
  private static final double FULL_CIRCLE_DEGREES = 360.0;
  private static final double HALF_DEGREE = 0.5;
  private static final double QUARTER_DEGREE = 0.25;

  public BBoxCalculatorTest() {
    super(SpatialContext.GEO);
  }

  /**
   * Tests that BBoxCalculator correctly computes the minimum bounding box
   * for geographic rectangles, handling edge cases like date-line crossing.
   */
  @Test 
  @Repeat(iterations = 100)
  public void testGeoLongitudeBoundingBoxCalculation() {
    // Given: A calculator and random rectangles
    BBoxCalculator calculator = new BBoxCalculator(ctx);
    List<Rectangle> inputRectangles = generateRandomRectangles();
    
    // When: We expand the calculator's range with each rectangle
    for (Rectangle rect : inputRectangles) {
      calculator.expandRange(rect);
    }
    
    // Then: The resulting boundary should be correct
    Rectangle calculatedBoundary = calculator.getBoundary();
    
    if (inputRectangles.size() == 1) {
      assertSingleRectangleCase(inputRectangles.get(0), calculatedBoundary);
    } else if (isWorldWideBoundary(calculatedBoundary)) {
      assertWorldWideBoundaryIsCorrect(inputRectangles);
    } else {
      assertNormalBoundaryIsCorrect(inputRectangles, calculatedBoundary);
    }
  }

  private List<Rectangle> generateRandomRectangles() {
    final int numShapes = randomIntBetween(1, 4);
    List<Rectangle> rectangles = new ArrayList<>(numShapes);
    
    for (int i = 0; i < numShapes; i++) {
      Rectangle rect = randomRectangle(30); // divisible by 30 for cleaner test values
      rectangles.add(rect);
    }
    
    return rectangles;
  }

  private void assertSingleRectangleCase(Rectangle inputRect, Rectangle boundary) {
    assertEquals("Single rectangle should equal its own boundary", inputRect, boundary);
  }

  private boolean isWorldWideBoundary(Rectangle boundary) {
    return boundary.getMinX() == WORLD_MIN_LON && boundary.getMaxX() == WORLD_MAX_LON;
  }

  private void assertWorldWideBoundaryIsCorrect(List<Rectangle> rectangles) {
    // When boundary spans the entire world, every longitude should be covered
    for (int longitude = WORLD_MIN_LON; longitude <= WORLD_MAX_LON; longitude++) {
      assertTrue("Longitude " + longitude + " should be covered by at least one rectangle",
          isLongitudeCoveredByAnyRectangle(rectangles, longitude));
    }
  }

  private void assertNormalBoundaryIsCorrect(List<Rectangle> rectangles, Rectangle boundary) {
    assertBoundaryContainsAllRectangles(rectangles, boundary);
    assertBoundaryEdgesAreTight(rectangles, boundary);
    assertBoundaryIsMinimal(rectangles, boundary);
  }

  private void assertBoundaryContainsAllRectangles(List<Rectangle> rectangles, Rectangle boundary) {
    for (Rectangle rect : rectangles) {
      assertRelation(SpatialRelation.CONTAINS, boundary, rect);
    }
  }

  private void assertBoundaryEdgesAreTight(List<Rectangle> rectangles, Rectangle boundary) {
    // Left edge should touch at least one rectangle
    assertTrue("Left boundary edge should touch at least one rectangle",
        isLongitudeCoveredByAnyRectangle(rectangles, boundary.getMinX()));
    assertFalse("Point just left of boundary should not be covered",
        isLongitudeCoveredByAnyRectangle(rectangles, normX(boundary.getMinX() - HALF_DEGREE)));

    // Right edge should touch at least one rectangle
    assertTrue("Right boundary edge should touch at least one rectangle",
        isLongitudeCoveredByAnyRectangle(rectangles, boundary.getMaxX()));
    assertFalse("Point just right of boundary should not be covered",
        isLongitudeCoveredByAnyRectangle(rectangles, normX(boundary.getMaxX() + HALF_DEGREE)));
  }

  private void assertBoundaryIsMinimal(List<Rectangle> rectangles, Rectangle boundary) {
    // Skip test if boundary is already more than half the world
    if (boundary.getWidth() <= WORLD_MAX_LON) {
      double currentGapSize = FULL_CIRCLE_DEGREES - boundary.getWidth();
      double largerGapSize = currentGapSize + HALF_DEGREE;
      
      assertNoLargerGapExists(rectangles, largerGapSize);
    }
  }

  private void assertNoLargerGapExists(List<Rectangle> rectangles, double gapSize) {
    // Try to place a gap of the given size starting from each rectangle's right edge
    for (Rectangle rect : rectangles) {
      double gapStartLon = rect.getMaxX() + QUARTER_DEGREE;
      double gapEndLon = gapStartLon + gapSize;
      Rectangle testGap = makeNormRect(gapStartLon, gapEndLon, WORLD_MIN_LAT, WORLD_MAX_LAT);
      
      boolean gapIntersectsAnyRectangle = doesGapIntersectAnyRectangle(rectangles, testGap);
      assertFalse("A gap of size " + gapSize + " should not fit between rectangles", 
          !gapIntersectsAnyRectangle);
    }
  }

  private boolean doesGapIntersectAnyRectangle(List<Rectangle> rectangles, Rectangle gap) {
    for (Rectangle rect : rectangles) {
      if (rect.relate(gap).intersects()) {
        return true;
      }
    }
    return false;
  }

  private boolean isLongitudeCoveredByAnyRectangle(List<Rectangle> rectangles, double longitude) {
    for (Rectangle rect : rectangles) {
      if (rect.relateXRange(longitude, longitude).intersects()) {
        return true;
      }
    }
    return false;
  }
}