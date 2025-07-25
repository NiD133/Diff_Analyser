package org.locationtech.spatial4j.shape.impl;

import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;
import org.junit.Test;
import org.locationtech.spatial4j.shape.RandomizedShapeTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BBoxCalculatorTest extends RandomizedShapeTest {

  private static final int REPETITIONS = 100;

  public BBoxCalculatorTest() {
    super(SpatialContext.GEO);
  }

  @Test
  @Repeat(iterations = REPETITIONS)
  public void testGeoLongitude() {
    // Given
    BBoxCalculator calculator = new BBoxCalculator(ctx);
    int numberOfShapes = randomIntBetween(1, 4);
    List<Rectangle> rectangles = new ArrayList<>(numberOfShapes);

    // When
    for (int i = 0; i < numberOfShapes; i++) {
      Rectangle rectangle = randomRectangle(30); // Divisible by 30 for even distribution.
      rectangles.add(rectangle);
      calculator.expandRange(rectangle);
    }
    Rectangle calculatedBoundary = calculator.getBoundary();

    // Then
    // Case 1: Single Rectangle Test
    if (numberOfShapes == 1) {
      assertEquals("Boundary should be the same as the single rectangle.", rectangles.get(0), calculatedBoundary);
      return;
    }

    // Case 2: World Boundary Test
    if (calculatedBoundary.getMinX() == -180 && calculatedBoundary.getMaxX() == 180) {
      // Verify that every longitude is covered by at least one rectangle
      for (int longitude = -180; longitude <= 180; longitude++) {
        assertTrue("Longitude " + longitude + " should be covered by at least one rectangle.",
                isLongitudeCoveredByAnyRectangle(rectangles, longitude));
      }
      return;
    }

    // Case 3: Multiple Rectangles - General Boundary Test
    // 1. Boundary contains all rectangles.
    for (Rectangle rectangle : rectangles) {
      assertRelation(SpatialRelation.CONTAINS, calculatedBoundary, rectangle);
    }

    // 2. Boundary edges are actual boundaries of the rectangles.
    assertTrue("Minimum X should be a boundary of at least one rectangle.",
            isLongitudeCoveredByAnyRectangle(rectangles, calculatedBoundary.getMinX()));
    assertFalse("Slightly smaller than Minimum X should NOT be a boundary.",
            isLongitudeCoveredByAnyRectangle(rectangles, normX(calculatedBoundary.getMinX() - 0.5)));

    assertTrue("Maximum X should be a boundary of at least one rectangle.",
            isLongitudeCoveredByAnyRectangle(rectangles, calculatedBoundary.getMaxX()));
    assertFalse("Slightly larger than Maximum X should NOT be a boundary.",
            isLongitudeCoveredByAnyRectangle(rectangles, normX(calculatedBoundary.getMaxX() + 0.5)));

    // 3. Verify that no larger gap exists. This ensures the boundary is the tightest possible.
    //   The idea is to check if we can find a gap larger than the one opposite the bounding box.
    if (calculatedBoundary.getWidth() > 180) {
      // If the boundary is wider than 180, no wider gap is possible.
      return;
    }

    double biggerGap = 360.0 - calculatedBoundary.getWidth() + 0.5; // A gap larger than the calculated gap.

    for (Rectangle rectangle : rectangles) {
      // Attempt to create a gap that is larger than the calculated gap, positioned to the right of the current rectangle.
      double gapStartLongitude = rectangle.getMaxX() + 0.25;
      double gapEndLongitude = gapStartLongitude + biggerGap;
      Rectangle potentialGap = makeNormRect(gapStartLongitude, gapEndLongitude, -90, 90);

      boolean gapFits = true;
      for (Rectangle otherRectangle : rectangles) {
        if (otherRectangle.relate(potentialGap).intersects()) {
          gapFits = false;
          break;
        }
      }
      assertFalse("A gap larger than the biggest possible gap should never fit.", gapFits);
    }
  }


  private boolean isLongitudeCoveredByAnyRectangle(List<Rectangle> rectangles, double longitude) {
    for (Rectangle rectangle : rectangles) {
      if (rectangle.relateXRange(longitude, longitude).intersects()) {
        return true;
      }
    }
    return false;
  }
}