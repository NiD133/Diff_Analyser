/*******************************************************************************
 * Copyright (c) 2015 MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.shape;

import org.locationtech.spatial4j.TestLog;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.locationtech.spatial4j.shape.SpatialRelation.CONTAINS;

public class ShapeCollectionTest extends RandomizedShapeTest {

  @Rule
  public final TestLog testLog = TestLog.instance;

  @Test
  public void testBoundingBox_shouldWrapWorldWhenSpanningMoreThan180Degrees() {
    ctx = SpatialContext.GEO;
    // Two rectangles that are far apart and cross the anti-meridian.
    // The bounding box should wrap the world.
    Rectangle r1 = ctx.makeRectangle(-170, 0, -10, 10);
    Rectangle r2 = ctx.makeRectangle(170, 0, -10, 10);

    // Test with one order
    ShapeCollection<Rectangle> s1 = new ShapeCollection<>(Arrays.asList(r1, r2), ctx);
    Rectangle bbox1 = s1.getBoundingBox();
    assertEquals("Should wrap world", -180.0, bbox1.getMinX(), 0.0);
    assertEquals("Should wrap world", 180.0, bbox1.getMaxX(), 0.0);

    // Test with reversed order to ensure consistency
    ShapeCollection<Rectangle> s2 = new ShapeCollection<>(Arrays.asList(r2, r1), ctx);
    Rectangle bbox2 = s2.getBoundingBox();
    assertEquals("Should wrap world", -180.0, bbox2.getMinX(), 0.0);
    assertEquals("Should wrap world", 180.0, bbox2.getMaxX(), 0.0);
  }

  @Test
  public void testBoundingBox_shouldWrapWorldWhenAdjacentAcrossDateline() {
    ctx = SpatialContext.GEO;
    // Two rectangles that meet at the anti-meridian.
    Rectangle r1 = ctx.makeRectangle(90, 180, -10, 10);
    Rectangle r2 = ctx.makeRectangle(-180, -90, -10, 10);

    ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(Arrays.asList(r1, r2), ctx);
    Rectangle bbox = shapeCollection.getBoundingBox();

    assertEquals("Should wrap world", -180.0, bbox.getMinX(), 0.0);
    assertEquals("Should wrap world", 180.0, bbox.getMaxX(), 0.0);
  }

  @Test
  public void testBoundingBox_shouldNotWrapWorldWhenGapExists() {
    ctx = SpatialContext.GEO;
    // This test ensures that the bounding box calculation doesn't unnecessarily
    // wrap the world. We create a set of rectangles that have a distinct gap
    // in their longitudinal coverage (between 90 and 130 degrees).
    Rectangle r1 = ctx.makeRectangle(-92, 90, -10, 10);
    Rectangle r2 = ctx.makeRectangle(130, 172, -10, 10);
    Rectangle r3 = ctx.makeRectangle(172, -60, -10, 10); // crosses dateline from 172 to -60

    ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(Arrays.asList(r1, r2, r3), ctx);

    // The smallest bounding box should be the one that crosses the dateline,
    // from 130 degrees east to 90 degrees east, rather than wrapping the globe.
    Rectangle bbox = shapeCollection.getBoundingBox();
    assertEquals(130.0, bbox.getMinX(), 0.0);
    assertEquals(90.0, bbox.getMaxX(), 0.0);
    // note: BBoxCalculatorTest thoroughly tests the longitude range logic.
  }

  @Test
  public void testRelateWithRectangle_nonGeoContext() {
    SpatialContextFactory factory = new SpatialContextFactory();
    factory.geo = false;
    factory.worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
    SpatialContext nonGeoCtx = factory.newSpatialContext();

    new ShapeCollectionRectIntersectionTestHelper(nonGeoCtx).testRelateWithRectangle();
  }

  @Test
  public void testRelateWithRectangle_geoContext() {
    this.ctx = SpatialContext.GEO;
    new ShapeCollectionRectIntersectionTestHelper(ctx).testRelateWithRectangle();
  }

  private class ShapeCollectionRectIntersectionTestHelper extends RectIntersectionTestHelper<ShapeCollection> {

    private ShapeCollectionRectIntersectionTestHelper(SpatialContext ctx) {
      super(ctx);
    }

    @Override
    protected ShapeCollection generateRandomShape(Point nearP) {
      testLog.log("Break on nearP.toString(): {}", nearP);
      List<Rectangle> shapes = new ArrayList<>();
      int count = randomIntBetween(1, 4);
      for (int i = 0; i < count; i++) {
        // The first 2 shapes are generated near the provided point, others are random.
        shapes.add(randomRectangle(i < 2 ? nearP : null));
      }
      ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(shapes, ctx);

      validateBoundingBox(shapeCollection);

      return shapeCollection;
    }

    /**
     * Verifies that the bounding box of the generated ShapeCollection is correct.
     */
    private void validateBoundingBox(ShapeCollection<Rectangle> shapeCollection) {
      List<Rectangle> shapes = shapeCollection.getShapes();
      Rectangle msBbox = shapeCollection.getBoundingBox();

      if (shapes.size() == 1) {
        assertEquals(shapes.get(0), msBbox.getBoundingBox());
      } else {
        // First, verify that the bounding box of the collection contains every individual shape.
        for (Rectangle shape : shapes) {
          assertRelation("bbox contains shape", CONTAINS, msBbox, shape);
        }

        // Second, for geographic contexts, if the bounding box wraps the world,
        // we must ensure it's not a false positive (i.e. the shapes don't actually
        // cover all longitudes). We do this by picking a random longitude and
        // checking if at least one shape in the collection covers it.
        if (ctx.isGeo() && msBbox.getWidth() == 360) {
          int lonTest = randomIntBetween(-180, 180);
          boolean covered = false;
          for (Rectangle shape : shapes) {
            if (shape.relateXRange(lonTest, lonTest).intersects()) {
              covered = true;
              break;
            }
          }
          if (!covered) {
            fail("ShapeCollection bbox world-wrap doesn't contain " + lonTest + " for shapes: " + shapes);
          }
        }
      }
    }

    /**
     * Generates a random point known to be within the first shape of the collection.
     * This is a helper for the randomized test to generate a point that is guaranteed
     * to be inside the shape collection, for testing CONTAINS or INTERSECTS relations.
     */
    @Override
    protected Point randomPointInEmptyShape(ShapeCollection shape) {
      Rectangle r = (Rectangle) shape.getShapes().get(0);
      return randomPointIn(r);
    }
  }
}