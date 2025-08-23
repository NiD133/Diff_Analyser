package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for BufferedLineString.
 * - Uses clear, descriptive test names
 * - Avoids EvoSuite-specific runtime and scaffolding
 * - Groups assertions by behavior
 * - Uses simple, deterministic inputs
 */
public class BufferedLineStringTest {

  private static Point p(SpatialContext ctx, double lon, double lat) {
    return new PointImpl(lon, lat, ctx);
  }

  @Test
  public void constructor_withEmptyPointList_isEmptyAndHasNoSegments() {
    SpatialContext ctx = SpatialContext.GEO;

    BufferedLineString bls = new BufferedLineString(new ArrayList<>(), 0.0, ctx);

    assertTrue("Empty input should yield empty shape", bls.isEmpty());
    assertEquals("Empty input should yield zero segments", 0, bls.getSegments().size());
    assertEquals(0.0, bls.getBuf(), 0.0);
  }

  @Test
  public void constructor_withSinglePoint_createsDegenerateSegment_noAreaWhenBufZero() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 0, 0));

    BufferedLineString bls = new BufferedLineString(pts, 0.0, ctx);

    assertFalse("Single point should be treated as one degenerate segment (non-empty)", bls.isEmpty());
    assertEquals(1, bls.getSegments().size());
    assertFalse("Bounding box for a point should not be empty", bls.getBoundingBox().isEmpty());
    assertFalse("Zero buffer means no area", bls.hasArea());
  }

  @Test
  public void hasArea_trueWhenPositiveBuffer() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 10, 5));

    BufferedLineString bls = new BufferedLineString(pts, 2.0, ctx);

    assertTrue("Positive buffer should imply area", bls.hasArea());
  }

  @Test
  public void getBuffered_returnsNewInstanceWithUpdatedBufferAndSamePoints() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 0, 0), p(ctx, 10, 5));
    BufferedLineString original = new BufferedLineString(pts, 1.0, ctx);

    BufferedLineString expanded = (BufferedLineString) original.getBuffered(2.0, ctx);

    assertNotSame("getBuffered should return a new instance", original, expanded);
    assertEquals("Buffer should be updated on returned instance", 2.0, expanded.getBuf(), 0.0);
    assertEquals("Original buffer should remain unchanged", 1.0, original.getBuf(), 0.0);

    // Points should be the same (order preserved)
    List<Point> originalPts = original.getPoints();
    List<Point> expandedPts = expanded.getPoints();
    assertEquals(originalPts.size(), expandedPts.size());
    for (int i = 0; i < originalPts.size(); i++) {
      assertEquals(originalPts.get(i), expandedPts.get(i));
    }
  }

  @Test
  public void equalsAndHashCode_respectContentAndBuffer() {
    SpatialContext ctx = SpatialContext.GEO;
    Point a = p(ctx, 0, 0);
    Point b = p(ctx, 1, 1);
    List<Point> pts1 = Arrays.asList(a, b);
    List<Point> pts2 = Arrays.asList(a, b);

    BufferedLineString x = new BufferedLineString(pts1, 3.0, ctx);
    BufferedLineString y = new BufferedLineString(pts2, 3.0, ctx);
    BufferedLineString z = new BufferedLineString(pts2, 4.0, ctx); // different buffer

    assertTrue(x.equals(x));
    assertTrue("Same points and buffer should be equal", x.equals(y) && y.equals(x));
    assertEquals("Equal objects must have same hashCode", x.hashCode(), y.hashCode());

    assertFalse("Different buffer should make them not equal", x.equals(z));
    assertFalse("Must not be equal to null", x.equals(null));
    assertFalse("Must not be equal to an unrelated type", x.equals("not-a-shape"));
  }

  @Test
  public void getSegments_withTwoPoints_createsOneSegment() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, -5, 2), p(ctx, 10, 4));

    BufferedLineString bls = new BufferedLineString(pts, 0.0, ctx);
    ShapeCollection<BufferedLine> segments = bls.getSegments();

    assertEquals(1, segments.size());
    assertFalse(bls.isEmpty());
  }

  @Test
  public void boundingBox_twoPoints_zeroBuffer_isMinToMax() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 0, 0), p(ctx, 10, 5));

    BufferedLineString bls = new BufferedLineString(pts, 0.0, ctx);
    Rectangle bbox = bls.getBoundingBox();

    assertFalse(bbox.isEmpty());
    assertEquals(0.0, bbox.getMinX(), 0.0);
    assertEquals(10.0, bbox.getMaxX(), 0.0);
    assertEquals(0.0, bbox.getMinY(), 0.0);
    assertEquals(5.0, bbox.getMaxY(), 0.0);
  }

  @Test
  public void relate_self_isWithin() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 1, 1), p(ctx, 2, 2));

    BufferedLineString bls = new BufferedLineString(pts, 1.0, ctx);

    SpatialRelation rel = bls.relate(bls);
    assertEquals(SpatialRelation.WITHIN, rel);
  }

  @Test
  public void toString_containsTypeAndBuffer() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 1, 2));
    BufferedLineString bls = new BufferedLineString(pts, 7.5, ctx);

    String s = bls.toString();
    assertTrue(s.contains("BufferedLineString"));
    assertTrue(s.contains("buf=7.5"));
  }

  @Test
  public void getPoints_returnsOriginalOrder() {
    SpatialContext ctx = SpatialContext.GEO;
    Point p1 = p(ctx, -10, 0);
    Point p2 = p(ctx, 5, 3);
    Point p3 = p(ctx, 20, -1);
    List<Point> pts = Arrays.asList(p1, p2, p3);

    BufferedLineString bls = new BufferedLineString(pts, 0.0, ctx);

    List<Point> actual = bls.getPoints();
    assertEquals(3, actual.size());
    assertEquals(p1, actual.get(0));
    assertEquals(p2, actual.get(1));
    assertEquals(p3, actual.get(2));
  }

  @Test
  public void getBuffered_withNullContext_throwsNPE() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 0, 0));

    BufferedLineString bls = new BufferedLineString(pts, 1.0, ctx);

    try {
      bls.getBuffered(2.0, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // ok
    }
  }

  @Test
  public void relate_withNullShape_throwsNPE() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Point> pts = Arrays.asList(p(ctx, 0, 0));

    BufferedLineString bls = new BufferedLineString(pts, 1.0, ctx);

    try {
      bls.relate((Shape) null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // ok
    }
  }
}