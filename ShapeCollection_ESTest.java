package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable, behavior-oriented tests for ShapeCollection.
 *
 * These tests focus on:
 * - Constructor preconditions (RandomAccess, context)
 * - Basic accessors (size, get, getShapes, getContext)
 * - Area/geometry semantics (hasArea, getArea, getBoundingBox, getCenter)
 * - Buffering and relation behavior
 * - Equality and basic contracts
 * - Utility method computeMutualDisjoint
 *
 * Notes:
 * - We intentionally avoid brittle assertions on string formatting or specific
 *   numeric values that depend on geodesic calculators; instead we assert
 *   general, stable properties (e.g., area is > 0, relations intersect()).
 * - The tests use SpatialContext.GEO for clarity and consistency.
 */
public class ShapeCollectionTest {

  // ---------------------------------------------------------------------------
  // Constructor behavior
  // ---------------------------------------------------------------------------

  @Test
  public void constructor_rejectsNonRandomAccessList() {
    // LinkedList does not implement RandomAccess -> should be rejected.
    List<Shape> shapes = new LinkedList<>();
    try {
      new ShapeCollection<>(shapes, SpatialContext.GEO);
      fail("Expected IllegalArgumentException for non-RandomAccess list");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("RandomAccess"));
    }
  }

  @Test
  public void constructor_nullContext_throwsNPE() {
    List<Shape> shapes = new ArrayList<>();
    try {
      new ShapeCollection<>(shapes, null);
      fail("Expected NullPointerException for null SpatialContext");
    } catch (NullPointerException expected) {
      // ok
    }
  }

  // ---------------------------------------------------------------------------
  // Accessors and identity
  // ---------------------------------------------------------------------------

  @Test
  public void getContext_returnsSameInstance() {
    SpatialContext ctx = SpatialContext.GEO;
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(), ctx);
    assertSame(ctx, sc.getContext());
  }

  @Test
  public void getShapes_returnsBackingListReference() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Shape> shapes = new ArrayList<>();
    shapes.add(ctx.makePoint(0, 0));

    ShapeCollection<Shape> sc = new ShapeCollection<>(shapes, ctx);

    // The collection documents that it keeps a reference to the provided list.
    assertSame(shapes, sc.getShapes());

    // Mutating the original list is reflected in the collection.
    shapes.add(ctx.makePoint(1, 1));
    assertEquals(2, sc.size());
    assertSame(shapes.get(1), sc.get(1));
  }

  @Test
  public void get_withInvalidIndex_throwsIndexOutOfBounds() {
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(), SpatialContext.GEO);
    try {
      sc.get(1);
      fail("Expected IndexOutOfBoundsException for index 1 on empty collection");
    } catch (IndexOutOfBoundsException expected) {
      // ok
    }
  }

  // ---------------------------------------------------------------------------
  // Area and geometry semantics
  // ---------------------------------------------------------------------------

  @Test
  public void emptyCollection_hasNoArea_andZeroArea() {
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(), SpatialContext.GEO);
    assertFalse(sc.hasArea());
    assertEquals(0.0, sc.getArea(sc.getContext()), 0.0);
  }

  @Test
  public void pointsOnly_haveNoArea_andZeroArea() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Shape> shapes = Arrays.asList(ctx.makePoint(-10, 0), ctx.makePoint(20, 5));
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(shapes), ctx);

    assertFalse(sc.hasArea());
    assertEquals(0.0, sc.getArea(ctx), 0.0);
  }

  @Test
  public void rectanglePresent_hasArea_andPositiveArea() {
    SpatialContext ctx = SpatialContext.GEO;
    // A small rectangle with non-zero area.
    Shape rect = ctx.makeRectangle(-10, -5, -3, 3);
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(Collections.singletonList(rect)), ctx);

    assertTrue(sc.hasArea());
    assertTrue(sc.getArea(ctx) > 0.0);
  }

  @Test
  public void getBoundingBox_containsAllMembers() {
    SpatialContext ctx = SpatialContext.GEO;
    Rectangle r1 = ctx.makeRectangle(-20, -10, -10, -5);
    Rectangle r2 = ctx.makeRectangle(5, 15, 0, 8);
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(Arrays.asList(r1, r2)), ctx);

    Rectangle bbox = sc.getBoundingBox();
    assertEquals(Math.min(r1.getMinX(), r2.getMinX()), bbox.getMinX(), 0.0);
    assertEquals(Math.max(r1.getMaxX(), r2.getMaxX()), bbox.getMaxX(), 0.0);
    assertEquals(Math.min(r1.getMinY(), r2.getMinY()), bbox.getMinY(), 0.0);
    assertEquals(Math.max(r1.getMaxY(), r2.getMaxY()), bbox.getMaxY(), 0.0);
  }

  @Test
  public void getCenter_forEmpty_isEmptyPoint() {
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(), SpatialContext.GEO);
    assertTrue(sc.getCenter().isEmpty());
  }

  // ---------------------------------------------------------------------------
  // Buffering
  // ---------------------------------------------------------------------------

  @Test
  public void getBuffered_zeroDistance_keepsSameSize() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Shape> shapes = new ArrayList<>();
    shapes.add(ctx.makePoint(0, 0));
    shapes.add(ctx.makeRectangle(-2, 2, -1, 1));

    ShapeCollection<Shape> sc = new ShapeCollection<>(shapes, ctx);
    ShapeCollection<?> buffered = sc.getBuffered(0.0, ctx);

    assertEquals(sc.size(), buffered.size());
  }

  @Test
  public void getBuffered_nullContext_throwsNPE() {
    SpatialContext ctx = SpatialContext.GEO;
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(), ctx);
    try {
      sc.getBuffered(1.0, null);
      fail("Expected NullPointerException for null SpatialContext");
    } catch (NullPointerException expected) {
      // ok
    }
  }

  // ---------------------------------------------------------------------------
  // Relations
  // ---------------------------------------------------------------------------

  @Test
  public void relate_emptyCollection_isDisjointFromAnything() {
    SpatialContext ctx = SpatialContext.GEO;
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(), ctx);
    SpatialRelation rel = sc.relate(ctx.getWorldBounds());
    assertEquals(SpatialRelation.DISJOINT, rel);
  }

  @Test
  public void relate_intersectsWhenAnyMemberIntersects() {
    SpatialContext ctx = SpatialContext.GEO;
    Rectangle r1 = ctx.makeRectangle(-20, -10, -10, -5);
    Rectangle r2 = ctx.makeRectangle(0, 10, 0, 5); // Will intersect with 'other'
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(Arrays.asList(r1, r2)), ctx);

    Rectangle other = ctx.makeRectangle(5, 15, 2, 8);
    SpatialRelation rel = sc.relate(other);

    assertTrue("Expected intersection with at least one member", rel.intersects());
  }

  @Test
  public void operations_throwOnNullMemberShape() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Shape> shapes = new ArrayList<>();
    shapes.add(null); // Intentionally invalid element

    ShapeCollection<Shape> sc = new ShapeCollection<>(shapes, ctx);

    try {
      sc.hasArea();
      fail("Expected NullPointerException when a member shape is null");
    } catch (NullPointerException expected) {
      // ok
    }
  }

  // ---------------------------------------------------------------------------
  // Utility: computeMutualDisjoint
  // ---------------------------------------------------------------------------

  @Test
  public void computeMutualDisjoint_emptyList_returnsTrue() {
    assertTrue(ShapeCollection.computeMutualDisjoint(Collections.emptyList()));
  }

  @Test
  public void computeMutualDisjoint_disjointShapes_returnsTrue() {
    SpatialContext ctx = SpatialContext.GEO;
    Rectangle r1 = ctx.makeRectangle(-20, -10, -10, -5);
    Rectangle r2 = ctx.makeRectangle(10, 20, 5, 10);

    assertTrue(ShapeCollection.computeMutualDisjoint(Arrays.asList(r1, r2)));
  }

  @Test
  public void computeMutualDisjoint_overlappingShapes_returnsFalse() {
    SpatialContext ctx = SpatialContext.GEO;
    Rectangle r1 = ctx.makeRectangle(-10, 10, -5, 5);
    Rectangle r2 = ctx.makeRectangle(0, 20, 0, 10);

    assertFalse(ShapeCollection.computeMutualDisjoint(Arrays.asList(r1, r2)));
  }

  // ---------------------------------------------------------------------------
  // Equality, hashCode, and toString
  // ---------------------------------------------------------------------------

  @Test
  public void equalsAndHashCode_basicContracts() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Shape> shapes = new ArrayList<>();
    shapes.add(ctx.makePoint(0, 0));

    ShapeCollection<Shape> sc1 = new ShapeCollection<>(shapes, ctx);
    ShapeCollection<Shape> sc2 = new ShapeCollection<>(shapes, ctx); // same backing list & ctx

    assertTrue(sc1.equals(sc1));          // reflexive
    assertTrue(sc1.equals(sc2));          // equal state
    assertEquals(sc1.hashCode(), sc2.hashCode()); // equal hash
    assertFalse(sc1.equals(null));        // not equal to null
    assertFalse(sc1.equals(ctx));         // not equal to different type
  }

  @Test
  public void toString_isReadable() {
    SpatialContext ctx = SpatialContext.GEO;
    List<Shape> shapes = new ArrayList<>();
    shapes.add(ctx.makePoint(1, 2));

    ShapeCollection<Shape> sc = new ShapeCollection<>(shapes, ctx);
    String s = sc.toString();

    assertNotNull(s);
    assertTrue(s.startsWith("ShapeCollection("));
    assertTrue(s.endsWith(")"));
  }

  // ---------------------------------------------------------------------------
  // Relate short-circuiting policy (protected hook)
  // ---------------------------------------------------------------------------

  @Test
  public void relateContainsShortCircuits_returnsTrueByDefault() {
    ShapeCollection<Shape> sc = new ShapeCollection<>(new ArrayList<>(), SpatialContext.GEO);
    assertTrue(sc.relateContainsShortCircuits());
  }
}