package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.*;

public class BufferedLineTest {

  // Helpers

  private static SpatialContext geo() {
    return SpatialContext.GEO;
  }

  /**
   * A Cartesian-like context that doesn't enforce geo world bounds.
   * Useful when we don't want bbox clamping to interfere with assertions.
   */
  private static SpatialContext cartesian() {
    return new SpatialContext(new SpatialContextFactory());
  }

  private static Point pt(SpatialContext ctx, double x, double y) {
    return new PointImpl(x, y, ctx);
  }

  // Construction and basic getters

  @Test
  public void construction_withZeroBuffer_hasNoArea_andCorrectBuf() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine line = new BufferedLine(a, a, 0d, ctx);

    assertEquals(0d, line.getBuf(), 0.0);
    assertFalse(line.hasArea());
    assertNotNull(line.getA());
    assertNotNull(line.getB());
    assertNotNull(line.getCenter());
  }

  @Test
  public void construction_withPositiveBuffer_hasArea_andCorrectBuf() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 1.5, 1.5);
    BufferedLine line = new BufferedLine(a, a, 1.5, ctx);

    assertTrue(line.hasArea());
    assertEquals(1.5, line.getBuf(), 0.0);
  }

  @Test
  public void getArea_geo_zeroBuffer_isZero() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine line = new BufferedLine(a, a, 0d, ctx);

    assertEquals(0d, line.getArea(ctx), 0.0);
  }

  @Test
  public void getArea_geo_positiveBuffer_isPositive() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 10, 10);
    BufferedLine line = new BufferedLine(a, a, 10d, ctx);

    assertTrue(line.getArea(ctx) > 0d);
  }

  // equals / hashCode

  @Test
  public void equals_and_hashCode_selfAndCopy_areEqual() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l1 = new BufferedLine(a, a, 5d, ctx);
    BufferedLine l2 = new BufferedLine(a, a, 5d, ctx);

    assertEquals(l1, l2);
    assertEquals(l1.hashCode(), l2.hashCode());
  }

  @Test
  public void equals_differsByBuffer_notEqual() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l1 = new BufferedLine(a, a, 5d, ctx);
    BufferedLine l2 = new BufferedLine(a, a, 6d, ctx);

    assertNotEquals(l1, l2);
    assertNotEquals(l2, l1);
  }

  @Test
  public void equals_differsByEndpoints_notEqual() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    Point b = pt(ctx, 1, 1);
    BufferedLine l1 = new BufferedLine(a, a, 0d, ctx);
    BufferedLine l2 = new BufferedLine(b, a, 0d, ctx);

    assertNotEquals(l1, l2);
  }

  @Test
  public void equals_handlesNullAndDifferentType() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    assertNotEquals(l, null);
    assertNotEquals(l, a);
    assertEquals(l, l);
  }

  // contains

  @Test
  public void contains_endpointWithPositiveBuffer_returnsTrue() {
    SpatialContext ctx = cartesian();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 10d, ctx);

    assertTrue(l.contains(a));
  }

  @Test
  public void contains_farPoint_returnsFalse() {
    SpatialContext ctx = cartesian();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 1d, ctx);

    Point far = pt(ctx, 100, 0);
    assertFalse(l.contains(far));
  }

  @Test(expected = NullPointerException.class)
  public void contains_nullPoint_throwsNPE() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    l.contains(null);
  }

  // relate

  @Test
  public void relate_worldBounds_withZeroBuffer_isWithin() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    SpatialRelation rel = l.relate(ctx.getWorldBounds());
    assertEquals(SpatialRelation.WITHIN, rel);
  }

  @Test
  public void relate_point_withZeroBuffer_containsPoint() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    SpatialRelation rel = l.relate(a);
    assertEquals(SpatialRelation.CONTAINS, rel);
  }

  @Test
  public void relate_rectangle_disjointWhenFarAway() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 1d, ctx);

    Rectangle far = new RectangleImpl(50, 60, 10, 20, ctx);
    assertEquals(SpatialRelation.DISJOINT, l.relate(far));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void relate_nullShape_throwsUnsupportedOperation() {
    SpatialContext ctx = cartesian();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    l.relate((Shape) null);
  }

  @Test(expected = NullPointerException.class)
  public void relate_nullRectangle_throwsNPE() {
    SpatialContext ctx = cartesian();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    l.relate((Rectangle) null);
  }

  // buffering

  @Test
  public void getBuffered_zeroDistance_returnsEquivalentBufferedLine() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    Shape buffered = l.getBuffered(0d, ctx);
    assertTrue(buffered instanceof BufferedLine);
    assertEquals(0d, ((BufferedLine) buffered).getBuf(), 0.0);
  }

  @Test(expected = AssertionError.class)
  public void getBuffered_negativeDistance_asserts() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    l.getBuffered(-1d, ctx);
  }

  @Test(expected = NullPointerException.class)
  public void getBuffered_nullContext_throwsNPE() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    l.getBuffered(0d, null);
  }

  // expandBufForLongitudeSkew

  @Test(expected = NullPointerException.class)
  public void expandBufForLongitudeSkew_nullArgs_throwsNPE() {
    BufferedLine.expandBufForLongitudeSkew(null, null, 1d);
  }

  @Test
  public void expandBufForLongitudeSkew_equator_zeroBuf_returnsZero() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);

    double expanded = BufferedLine.expandBufForLongitudeSkew(a, a, 0d);
    assertEquals(0d, expanded, 0.0);
  }

  @Test(expected = AssertionError.class)
  public void expandBufForLongitudeSkew_invalidLatitude_asserts() {
    SpatialContext ctx = geo();
    // Extremely invalid latitude to trigger internal assertion
    Point insaneLat = pt(ctx, 0, 5181.5);
    Point a = pt(ctx, 0, 0);

    BufferedLine.expandBufForLongitudeSkew(insaneLat, a, Math.PI / 4);
  }

  // bbox and internal lines (light-touch)

  @Test
  public void getBoundingBox_notNull() {
    SpatialContext ctx = cartesian();
    Point a = pt(ctx, 2, 2);
    BufferedLine l = new BufferedLine(a, a, 5d, ctx);

    Rectangle bbox = l.getBoundingBox();
    assertNotNull(bbox);
  }

  @Test
  public void internalLines_areAvailable_forDegenerateSegment() {
    SpatialContext ctx = cartesian();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 10d, ctx);

    InfBufLine primary = l.getLinePrimary();
    InfBufLine perp = l.getLinePerp();
    assertNotNull(primary);
    assertNotNull(perp);
    assertEquals(10d, primary.getBuf(), 0.0);
  }

  // toString

  @Test
  public void toString_includesEndpointsAndBuffer() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    BufferedLine l = new BufferedLine(a, a, 0d, ctx);

    assertEquals("BufferedLine(Pt(x=0.0,y=0.0), Pt(x=0.0,y=0.0) b=0.0)", l.toString());
  }

  // Validation and error handling

  @Test(expected = AssertionError.class)
  public void constructor_negativeBuffer_asserts() {
    SpatialContext ctx = geo();
    Point a = pt(ctx, 0, 0);
    new BufferedLine(a, a, -1d, ctx);
  }

  @Test(expected = NullPointerException.class)
  public void constructor_nullPoints_throwsNPE() {
    SpatialContext ctx = geo();
    new BufferedLine(null, null, 1d, ctx);
  }
}