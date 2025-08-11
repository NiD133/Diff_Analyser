package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.*;

/**
 * Readable, behavior-oriented tests for BBoxCalculator.
 * 
 * Notes:
 * - Focuses on core behaviors (initial state, expanding ranges, boundary creation).
 * - Avoids EvoSuite-specific runners and scaffolding.
 * - Uses clear test names, minimal magic numbers, and explanatory comments.
 */
public class BBoxCalculatorTest {

  private static final double DELTA = 1e-9;

  @Test
  public void initialState_usesExtremeSentinelValues() {
    BBoxCalculator calc = new BBoxCalculator(SpatialContext.GEO);

    assertEquals(Double.POSITIVE_INFINITY, calc.getMinX(), DELTA);
    assertEquals(Double.NEGATIVE_INFINITY, calc.getMaxX(), DELTA);
    assertEquals(Double.POSITIVE_INFINITY, calc.getMinY(), DELTA);
    assertEquals(Double.NEGATIVE_INFINITY, calc.getMaxY(), DELTA);
  }

  @Test
  public void expandRange_setsAllBounds_whenNothingSetBefore() {
    BBoxCalculator calc = new BBoxCalculator(SpatialContext.GEO);

    calc.expandRange(0.0, 10.0, -5.0, 5.0);

    assertEquals(0.0, calc.getMinX(), DELTA);
    assertEquals(10.0, calc.getMaxX(), DELTA);
    assertEquals(-5.0, calc.getMinY(), DELTA);
    assertEquals(5.0, calc.getMaxY(), DELTA);
  }

  @Test
  public void expandRange_withRectangle_updatesBounds() {
    SpatialContext ctx = SpatialContext.GEO;
    BBoxCalculator calc = new BBoxCalculator(ctx);
    Rectangle rect = ctx.getShapeFactory().rect(-20.0, -10.0, 0.0, 10.0);

    calc.expandRange(rect);

    assertEquals(-20.0, calc.getMinX(), DELTA);
    assertEquals(-10.0, calc.getMaxX(), DELTA);
    assertEquals(0.0, calc.getMinY(), DELTA);
    assertEquals(10.0, calc.getMaxY(), DELTA);
  }

  @Test
  public void expandXRange_doesNotChangeYBounds() {
    BBoxCalculator calc = new BBoxCalculator(SpatialContext.GEO);
    // Establish Y range first
    calc.expandRange(0.0, 0.0, -1.0, 1.0);

    calc.expandXRange(-10.0, 10.0);

    assertEquals(-1.0, calc.getMinY(), DELTA);
    assertEquals(1.0, calc.getMaxY(), DELTA);
  }

  @Test(expected = NullPointerException.class)
  public void getBoundary_throwsWhenContextIsNull() {
    BBoxCalculator calc = new BBoxCalculator(null);

    calc.getBoundary();
  }

  @Test(expected = NullPointerException.class)
  public void expandRange_nullRectangle_throwsNpe() {
    BBoxCalculator calc = new BBoxCalculator(SpatialContext.GEO);

    calc.expandRange((Rectangle) null);
  }

  @Test
  public void doesXWorldWrap_trueWhenWorldBoundsProvided() {
    SpatialContext ctx = SpatialContext.GEO;
    BBoxCalculator calc = new BBoxCalculator(ctx);

    calc.expandRange(ctx.getWorldBounds()); // full world bounds

    assertTrue("Expected world-wrap when bounds equal the world", calc.doesXWorldWrap());
    assertEquals(-90.0, calc.getMinY(), DELTA);
    assertEquals(90.0, calc.getMaxY(), DELTA);
  }

  @Test
  public void doesXWorldWrap_falseForSimpleNonWrappingRange() {
    BBoxCalculator calc = new BBoxCalculator(SpatialContext.GEO);

    calc.expandRange(-10.0, 10.0, -5.0, 5.0);

    assertFalse(calc.doesXWorldWrap());
  }

  @Test
  public void getBoundary_returnsRectangleMatchingCalculatedBounds() {
    SpatialContext ctx = new SpatialContextFactory().newSpatialContext();
    BBoxCalculator calc = new BBoxCalculator(ctx);

    calc.expandRange(-20.0, -10.0, 0.0, 10.0);
    Rectangle r = calc.getBoundary();

    assertEquals(-20.0, r.getMinX(), DELTA);
    assertEquals(-10.0, r.getMaxX(), DELTA);
    assertEquals(0.0, r.getMinY(), DELTA);
    assertEquals(10.0, r.getMaxY(), DELTA);
  }

  @Test(expected = RuntimeException.class)
  public void getBoundary_failsIfYBoundsWereNeverSet() {
    BBoxCalculator calc = new BBoxCalculator(SpatialContext.GEO);

    // Only X expanded; Y remains at +/- Infinity which should be invalid for creating a rectangle.
    calc.expandXRange(-10.0, 10.0);

    calc.getBoundary();
  }
}