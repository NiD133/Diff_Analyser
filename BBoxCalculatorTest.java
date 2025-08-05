/*******************************************************************************
 * Copyright (c) 2015 David Smiley
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.shape.impl;

import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.RandomizedShapeTest;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Randomized test for {@link BBoxCalculator}.
 * Focuses on the complex geodetic (longitude) calculations.
 */
public class BBoxCalculatorTest extends RandomizedShapeTest {

  public BBoxCalculatorTest() {
    super(SpatialContext.GEO);
  }

  // Note: Latitude calculations are straightforward (a simple min/max) and testing them
  // would effectively duplicate the implementation logic. Therefore, this test focuses
  // on the more complex longitude calculations which involve dateline wrapping.

  @Test
  @Repeat(iterations = 100)
  public void testGeoLongitudeBoundingBox() {
    // 1. Setup: Create a BBoxCalculator and add 1 to 4 random rectangles.
    BBoxCalculator calc = new BBoxCalculator(ctx);
    final int numShapes = randomIntBetween(1, 4);
    List<Rectangle> rects = new ArrayList<>(numShapes);
    for (int i = 0; i < numShapes; i++) {
      Rectangle rect = randomRectangle(30); // Rectangles with width divisible by 30
      rects.add(rect);
      calc.expandRange(rect);
    }
    Rectangle boundary = calc.getBoundary();

    // 2. Test Case: Single Rectangle
    // If only one rectangle is added, the boundary should be that same rectangle.
    if (numShapes == 1) {
      assertEquals("A single rectangle's bbox should be itself", rects.get(0), boundary);
      return; // Test is complete for this simple case
    }

    // 3. Test Property: Containment
    // The calculated boundary must contain all the original rectangles.
    for (Rectangle rect : rects) {
      assertRelation("Boundary should contain input rectangle " + rect,
          SpatialRelation.CONTAINS, boundary, rect);
    }

    // 4. Test Case: World-Wrapping Boundary
    // If the boundary wraps the entire world, every longitude must be covered by at least one
    // of the input rectangles.
    if (boundary.getWidth() == 360) {
      for (int lon = -180; lon <= 180; lon++) {
        String message = "Longitude " + lon + " should be covered in a world-wrapping bbox";
        assertTrue(message, atLeastOneRectHasLon(rects, lon));
      }
      return; // Further boundary checks are not meaningful for a world-wrapping box.
    }

    // 5. Test Property: Boundary Tightness
    // The edges of the boundary must touch at least one of the input rectangles. A point just
    // outside the boundary should not be covered.
    assertTrue("Min X of boundary should be covered by an input rectangle",
        atLeastOneRectHasLon(rects, boundary.getMinX()));
    assertFalse("A point just outside Min X should not be covered",
        atLeastOneRectHasLon(rects, normX(boundary.getMinX() - 0.5)));

    assertTrue("Max X of boundary should be covered by an input rectangle",
        atLeastOneRectHasLon(rects, boundary.getMaxX()));
    assertFalse("A point just outside Max X should not be covered",
        atLeastOneRectHasLon(rects, normX(boundary.getMaxX() + 0.5)));

    // 6. Test Property: Minimality (Smallest Possible Boundary)
    // For a geo bbox, the smallest boundary is the one that leaves the largest possible "gap"
    // on the other side of the globe. This test verifies minimality by constructing a
    // hypothetical gap that is slightly *larger* than the one our calculated boundary leaves.
    // We then assert that this larger gap CANNOT be placed anywhere without intersecting one
    // of the input rectangles. If it can't fit, it proves our original gap was indeed the
    // maximum possible, and thus our boundary is the minimum possible.
    // This check is most relevant for "wide" rectangles (>180 degrees) where the gap is narrow.
    if (boundary.getWidth() > 180) {
      // The actual gap on the other side of the world has a width of (360 - boundary.getWidth()).
      // We will test if a slightly larger gap could have been created.
      double actualGapWidth = 360.0 - boundary.getWidth();
      double hypotheticalLargerGapWidth = actualGapWidth + 0.5;

      // We try to place this hypothetical larger gap somewhere on the globe. A good place to
      // start looking is next to each of the original rectangles.
      for (Rectangle rect : rects) {
        // Let's try to place the gap to the "right" of the current rectangle's max X.
        double gapStartX = rect.getMaxX() + 0.25; // Start just past the edge of the rect
        double gapEndX = gapStartX + hypotheticalLargerGapWidth;
        Rectangle hypotheticalGap = makeNormRect(gapStartX, gapEndX, -90, 90);

        // This hypothetical gap should NOT fit; it must intersect with at least one of the
        // input rectangles. If it fit, our boundary was not minimal.
        String message = "A gap larger than the one found should not fit. Failed at " + hypotheticalGap;
        assertTrue(message, anyRectIntersects(hypotheticalGap, rects));
      }
    }
  }

  /**
   * Checks if a given longitude is contained within any of the rectangles in the list.
   */
  private boolean atLeastOneRectHasLon(List<Rectangle> rects, double lon) {
    for (Rectangle rect : rects) {
      if (rect.relateXRange(lon, lon).intersects()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the target rectangle intersects with any rectangle in the provided list.
   */
  private boolean anyRectIntersects(Rectangle target, List<Rectangle> rects) {
    for (Rectangle rect : rects) {
      if (rect.relate(target).intersects()) {
        return true;
      }
    }
    return false;
  }
}