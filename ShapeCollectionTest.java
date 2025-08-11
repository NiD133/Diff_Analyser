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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.locationtech.spatial4j.shape.SpatialRelation.CONTAINS;

public class ShapeCollectionTest extends RandomizedShapeTest {

    private static final String WORLD_LON_RANGE = getLonRangeString(SpatialContext.GEO.getWorldBounds());

    @Rule
    public final TestLog testLog = TestLog.instance;

    //---------------- Bounding Box Tests ----------------//

    @Test
    public void boundingBox_shouldCoverFullWorld_whenShapesSpanGlobe() {
        assertWorldWideBoundingBox(-180, 180, -180, 180);
        assertWorldWideBoundingBox(-180, 0, 0, +180);
        assertWorldWideBoundingBox(-90, +90, +90, -90);
    }

    @Test
    public void boundingBox_shouldNotWrapWorld_whenShapesDoNotCoverFullLongitude() {
        ctx = SpatialContext.GEO;
        Rectangle leftAmerica = ctx.makeRectangle(-92, 90, -10, 10);
        Rectangle australia = ctx.makeRectangle(130, 172, -10, 10);
        Rectangle pacific = ctx.makeRectangle(172, -60, -10, 10); // Wraps dateline

        ShapeCollection<Rectangle> collection = new ShapeCollection<>(Arrays.asList(leftAmerica, australia, pacific), ctx);
        
        String expectedLonRange = "130.0 90.0";
        assertEquals("Longitude range should not wrap when shapes don't cover entire globe", 
                     expectedLonRange, 
                     getLonRangeString(collection.getBoundingBox()));
    }

    //---------------- Rectangle Intersection Tests ----------------//

    @Test
    public void shouldCorrectlyIntersectWithRectangleInCartesianContext() {
        SpatialContext cartesianContext = createCartesianContext();
        new ShapeCollectionIntersectionTester(cartesianContext).testRelateWithRectangle();
    }

    @Test
    public void shouldCorrectlyIntersectWithRectangleInGeographicContext() {
        ctx = SpatialContext.GEO;
        new ShapeCollectionIntersectionTester(ctx).testRelateWithRectangle();
    }

    //---------------- Helper Methods ----------------//

    private static String getLonRangeString(Rectangle bbox) {
        return bbox.getMinX() + " " + bbox.getMaxX();
    }

    private void assertWorldWideBoundingBox(double rect1MinLon, double rect1MaxLon, 
                                            double rect2MinLon, double rect2MaxLon) {
        ctx = SpatialContext.GEO;
        Rectangle rect1 = ctx.makeRectangle(rect1MinLon, rect1MaxLon, -10, 10);
        Rectangle rect2 = ctx.makeRectangle(rect2MinLon, rect2MaxLon, -10, 10);

        assertBoundingBoxForCollection(WORLD_LON_RANGE, Arrays.asList(rect1, rect2));
        assertBoundingBoxForCollection(WORLD_LON_RANGE, Arrays.asList(rect2, rect1)); // Test order independence
    }

    private void assertBoundingBoxForCollection(String expectedLonRange, List<Rectangle> rectangles) {
        ShapeCollection<Rectangle> collection = new ShapeCollection<>(rectangles, ctx);
        assertEquals("Bounding box should cover entire longitude range",
                     expectedLonRange, 
                     getLonRangeString(collection.getBoundingBox()));
    }

    private SpatialContext createCartesianContext() {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.geo = false;
        factory.worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
        return factory.newSpatialContext();
    }

    //---------------- Test Helper Class ----------------//

    private class ShapeCollectionIntersectionTester extends RectIntersectionTestHelper<ShapeCollection> {

        ShapeCollectionIntersectionTester(SpatialContext ctx) {
            super(ctx);
        }

        @Override
        protected ShapeCollection generateRandomShape(Point nearPoint) {
            testLog.log("Generating shape near: {}", nearPoint);
            List<Rectangle> rectangles = generateRandomRectangles(nearPoint);
            ShapeCollection<Rectangle> collection = new ShapeCollection<>(rectangles, ctx);
            
            validateBoundingBox(collection, rectangles);
            return collection;
        }

        @Override
        protected Point randomPointInEmptyShape(ShapeCollection shape) {
            Rectangle firstRectangle = (Rectangle) shape.getShapes().get(0);
            return randomPointIn(firstRectangle);
        }

        private List<Rectangle> generateRandomRectangles(Point nearPoint) {
            List<Rectangle> rectangles = new ArrayList<>();
            int count = randomIntBetween(1, 4);
            for (int i = 0; i < count; i++) {
                // First two rectangles are near reference point
                boolean placeNearReference = i < 2;
                rectangles.add(randomRectangle(placeNearReference ? nearPoint : null));
            }
            return rectangles;
        }

        private void validateBoundingBox(ShapeCollection<Rectangle> collection, List<Rectangle> rectangles) {
            Rectangle computedBbox = collection.getBoundingBox();
            
            if (rectangles.size() == 1) {
                assertEquals("Single-shape collection bbox should match the shape", 
                             rectangles.get(0), computedBbox);
            } else {
                for (Rectangle rect : rectangles) {
                    assertRelation("Collection bbox should contain all shapes", 
                                  CONTAINS, computedBbox, rect);
                }
                validateWorldWrapCoverage(rectangles, computedBbox);
            }
        }

        private void validateWorldWrapCoverage(List<Rectangle> rectangles, Rectangle computedBbox) {
            if (ctx.isGeo() && computedBbox.getMinX() == -180 && computedBbox.getMaxX() == 180) {
                int testLon = randomIntBetween(-180, 180);
                boolean covered = false;
                
                for (Rectangle rect : rectangles) {
                    if (rect.relateXRange(testLon, testLon).intersects()) {
                        covered = true;
                        break;
                    }
                }
                
                if (!covered) {
                    fail("World-wrapped bbox claims coverage at " + testLon + 
                         "° but no shape exists there. Shapes: " + rectangles);
                }
            }
        }
    }
}