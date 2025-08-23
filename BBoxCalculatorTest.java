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

import static org.junit.Assert.*;

/**
 * Tests BBoxCalculator longitude handling with emphasis on geodetic wrap-around (dateline).
 * Notes:
 * - We focus on longitude behavior because latitude behavior is straightforward and mirrors
 *   the implementation too closely to provide additional value.
 * - Randomized tests are used to cover a wide range of cases. Helper methods and clear
 *   assertions are provided to make the intent explicit.
 */
public class BBoxCalculatorTest extends RandomizedShapeTest {

  private static final double WORLD_MIN_LON = -180d;
  private static final double WORLD_MAX_LON = 180d;
  private static final double WORLD_MIN_LAT = -90d;
  private static final double WORLD_MAX_LAT = 90d;

  // A small step used to probe just outside a boundary
  private static final double PROBE_DELTA = 0.5d;

  public BBoxCalculatorTest() {
    super(SpatialContext.GEO);
  }

  @Test
  @Repeat(iterations = 100)
  public void testLongitudeBoundingBox_randomized() {
    // 1 to 4 rectangles makes it easy to reason about and still covers interesting combinations
    final int numRects = randomIntBetween(1, 4);
    final List<Rectangle> rects = new ArrayList<>(numRects);

    // Use a grid that makes probing with 0.25/0.5 degrees line up nicely.
    // See PROBE_DELTA and the 0.25 used in gap testing.
    for (int i = 0; i < numRects; i++) {
      rects.add(randomRectangle(30));
    }

    final Rectangle boundary = computeBoundary(rects);

    // Single rectangle: the boundary must equal that rectangle
    if (numRects == 1) {
      assertEquals("Single rectangle should be its own boundary", rects.get(0), boundary);
      return;
    }

    // If the boundary spans the entire world-longitude, ensure every longitude is covered
    if (boundary.getMinX() == WORLD_MIN_LON && boundary.getMaxX() == WORLD_MAX_LON) {
      assertEveryWholeDegreeLongitudeIsCovered(rects);
      return;
    }

    // General case: the boundary should contain all rectangles...
    assertBoundaryContainsAllRectangles(boundary, rects);

    // ...and both boundary edges must be tight (touched by at least one rectangle),
    // with nothing immediately beyond them.
    assertBoundaryEdgesAreTight(boundary, rects);

    // If the chosen boundary is wider than 180 degrees, verify that the gap on the opposite
    // side is the maximum possible (i.e., no strictly larger gap can exist without intersecting).
    assertOppositeGapIsMaximalIfWiderThanHalfWorld(boundary, rects);
  }

  @Test
  public void testLongitudeBoundingBox_worldBoundsWhenEveryLongitudeIsCovered() {
    // Construct rectangles that collectively cover every longitude.
    List<Rectangle> rects = new ArrayList<>();
    rects.add(makeNormRect(-180, -90, WORLD_MIN_LAT, WORLD_MAX_LAT));
    rects.add(makeNormRect(-90, 0, WORLD_MIN_LAT, WORLD_MAX_LAT));
    rects.add(makeNormRect(0, 90, WORLD_MIN_LAT, WORLD_MAX_LAT));
    rects.add(makeNormRect(90, 180, WORLD_MIN_LAT, WORLD_MAX_LAT));

    Rectangle boundary = computeBoundary(rects);

    assertEquals(WORLD_MIN_LON, boundary.getMinX(), 0);
    assertEquals(WORLD_MAX_LON, boundary.getMaxX(), 0);
    assertEveryWholeDegreeLongitudeIsCovered(rects);
  }

  // --- Helpers ----------------------------------------------------------------

  private Rectangle computeBoundary(List<Rectangle> rects) {
    BBoxCalculator calc = new BBoxCalculator(ctx);
    for (Rectangle r : rects) {
      calc.expandRange(r);
    }
    return calc.getBoundary();
  }

  private void assertEveryWholeDegreeLongitudeIsCovered(List<Rectangle> rects) {
    for (int lon = (int) WORLD_MIN_LON; lon <= (int) WORLD_MAX_LON; lon++) {
      assertTrue("Expected some rectangle to cover lon=" + lon,
          hasAnyRectangleCoveringLongitude(rects, lon));
    }
  }

  private void assertBoundaryContainsAllRectangles(Rectangle boundary, List<Rectangle> rects) {
    for (Rectangle rect : rects) {
      assertRelation(SpatialRelation.CONTAINS, boundary, rect);
    }
  }

  private void assertBoundaryEdgesAreTight(Rectangle boundary, List<Rectangle> rects) {
    // Left edge must be touched; just to the left must not be covered
    assertTrue("No rectangle touches boundary minX=" + boundary.getMinX(),
        hasAnyRectangleCoveringLongitude(rects, boundary.getMinX()));
    assertFalse("Some rectangle covers just left of boundary minX",
        hasAnyRectangleCoveringLongitude(rects, normX(boundary.getMinX() - PROBE_DELTA)));

    // Right edge must be touched; just to the right must not be covered
    assertTrue("No rectangle touches boundary maxX=" + boundary.getMaxX(),
        hasAnyRectangleCoveringLongitude(rects, boundary.getMaxX()));
    assertFalse("Some rectangle covers just right of boundary maxX",
        hasAnyRectangleCoveringLongitude(rects, normX(boundary.getMaxX() + PROBE_DELTA)));
  }

  private void assertOppositeGapIsMaximalIfWiderThanHalfWorld(Rectangle boundary, List<Rectangle> rects) {
    if (boundary.getWidth() <= 180) {
      return; // if not wider than half the world, there can't exist a wider gap
    }

    // If the boundary is wider than 180°, then the opposite gap is smaller than 180°,
    // and must be the maximum possible. Try to place a (strictly) larger gap next to each
    // rectangle and assert it can't fit without intersecting some rectangle.
    double strictlyLargerGap = 360.0 - boundary.getWidth() + PROBE_DELTA;

    for (Rectangle rect : rects) {
      // Place the candidate gap immediately to the right of this rectangle
      double gapLeft = rect.getMaxX() + 0.25; // lines up with randomRectangle(30) grid
      double gapRight = gapLeft + strictlyLargerGap;

      Rectangle candidateGap = makeNormRect(gapLeft, gapRight, WORLD_MIN_LAT, WORLD_MAX_LAT);

      boolean intersectsAny = false;
      for (Rectangle other : rects) {
        if (other.relate(candidateGap).intersects()) {
          intersectsAny = true;
          break;
        }
      }
      // The "strictly larger" gap must not be feasible.
      assertTrue("Found a gap larger than the theoretical maximum", intersectsAny);
    }
  }

  private boolean hasAnyRectangleCoveringLongitude(List<Rectangle> rects, double lon) {
    for (Rectangle rect : rects) {
      if (rect.relateXRange(lon, lon).intersects()) {
        return true;
      }
    }
    return false;
  }
}