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

/**
 * Tests for ShapeCollection functionality including bounding box calculations,
 * world wrapping behavior, and rectangle intersection operations.
 */
public class ShapeCollectionTest extends RandomizedShapeTest {

    private static final String WORLD_LONGITUDE_RANGE = "-180.0 180.0";

    @Rule
    public final TestLog testLog = TestLog.instance;

    @Test
    public void testBoundingBoxCalculation_WorldWrappingScenarios() {
        // Test various combinations that should result in world-wrapping bounding boxes
        validateWorldWrappingBoundingBox(-180, 180, -180, 180);
        validateWorldWrappingBoundingBox(-180, 0, 0, +180);
        validateWorldWrappingBoundingBox(-90, +90, +90, -90);
    }

    @Test
    public void testBoundingBoxCalculation_NoWorldWrapping() {
        ctx = SpatialContext.GEO;
        
        // Create rectangles that don't span the entire world longitude range
        // Gap exists around longitude 102, so shouldn't world-wrap
        Rectangle americasRectangle = ctx.makeRectangle(-92, 90, -10, 10);
        Rectangle asiaRectangle = ctx.makeRectangle(130, 172, -10, 10);
        Rectangle pacificRectangle = ctx.makeRectangle(172, -60, -10, 10);
        
        ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(
            Arrays.asList(americasRectangle, asiaRectangle, pacificRectangle), ctx);
        
        String expectedLongitudeRange = "130.0 90.0";
        String actualLongitudeRange = getLongitudeRangeString(shapeCollection.getBoundingBox());
        assertEquals("Bounding box should not world-wrap when gaps exist", 
                    expectedLongitudeRange, actualLongitudeRange);
    }

    @Test
    public void testRectangleIntersection_NonGeographicContext() {
        SpatialContext nonGeoContext = createNonGeographicSpatialContext();
        
        ShapeCollectionRectIntersectionTestHelper testHelper = 
            new ShapeCollectionRectIntersectionTestHelper(nonGeoContext);
        testHelper.testRelateWithRectangle();
    }

    @Test
    public void testRectangleIntersection_GeographicContext() {
        ctx = SpatialContext.GEO;
        
        ShapeCollectionRectIntersectionTestHelper testHelper = 
            new ShapeCollectionRectIntersectionTestHelper(ctx);
        testHelper.testRelateWithRectangle();
    }

    /**
     * Validates that a ShapeCollection containing two rectangles results in a world-wrapping bounding box.
     * Tests both possible orderings of the rectangles to ensure order independence.
     */
    private void validateWorldWrappingBoundingBox(double rect1MinX, double rect1MaxX, 
                                                 double rect2MinX, double rect2MaxX) {
        ctx = SpatialContext.GEO;
        
        Rectangle rectangle1 = ctx.makeRectangle(rect1MinX, rect1MaxX, -10, 10);
        Rectangle rectangle2 = ctx.makeRectangle(rect2MinX, rect2MaxX, -10, 10);

        // Test first ordering
        ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(
            Arrays.asList(rectangle1, rectangle2), ctx);
        assertEquals("First ordering should produce world-wrapping bounding box",
                    WORLD_LONGITUDE_RANGE, getLongitudeRangeString(shapeCollection.getBoundingBox()));

        // Test reversed ordering to ensure order independence
        shapeCollection = new ShapeCollection<>(Arrays.asList(rectangle2, rectangle1), ctx);
        assertEquals("Reversed ordering should produce same world-wrapping bounding box",
                    WORLD_LONGITUDE_RANGE, getLongitudeRangeString(shapeCollection.getBoundingBox()));
    }

    /**
     * Creates a non-geographic spatial context with custom world bounds for testing.
     */
    private SpatialContext createNonGeographicSpatialContext() {
        return new SpatialContextFactory() {{
            geo = false;
            worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
        }}.newSpatialContext();
    }

    /**
     * Extracts the longitude range from a rectangle as a formatted string.
     */
    private static String getLongitudeRangeString(Rectangle boundingBox) {
        return boundingBox.getMinX() + " " + boundingBox.getMaxX();
    }

    /**
     * Helper class for testing rectangle intersection behavior with ShapeCollections.
     * Generates random ShapeCollections and validates their bounding box calculations.
     */
    private class ShapeCollectionRectIntersectionTestHelper extends RectIntersectionTestHelper<ShapeCollection> {

        private ShapeCollectionRectIntersectionTestHelper(SpatialContext spatialContext) {
            super(spatialContext);
        }

        @Override
        protected ShapeCollection generateRandomShape(Point nearPoint) {
            testLog.log("Generating random shape near point: {}", nearPoint);
            
            List<Rectangle> rectangles = createRandomRectangles(nearPoint);
            ShapeCollection<Rectangle> shapeCollection = new ShapeCollection<>(rectangles, ctx);
            
            validateBoundingBoxCalculation(shapeCollection, rectangles);
            
            return shapeCollection;
        }

        @Override
        protected Point randomPointInEmptyShape(ShapeCollection shapeCollection) {
            // Get the first rectangle from the collection and generate a random point within it
            Rectangle firstRectangle = (Rectangle) shapeCollection.getShapes().get(0);
            return randomPointIn(firstRectangle);
        }

        /**
         * Creates a list of random rectangles, with the first two positioned near the given point.
         */
        private List<Rectangle> createRandomRectangles(Point nearPoint) {
            List<Rectangle> rectangles = new ArrayList<>();
            int rectangleCount = randomIntBetween(1, 4);
            
            for (int i = 0; i < rectangleCount; i++) {
                // First 2 rectangles are positioned near the given point, others are anywhere
                Point referencePoint = (i < 2) ? nearPoint : null;
                rectangles.add(randomRectangle(referencePoint));
            }
            
            return rectangles;
        }

        /**
         * Validates that the ShapeCollection's bounding box correctly encompasses all constituent shapes.
         */
        private void validateBoundingBoxCalculation(ShapeCollection<Rectangle> shapeCollection, 
                                                   List<Rectangle> rectangles) {
            Rectangle calculatedBoundingBox = shapeCollection.getBoundingBox();
            
            if (rectangles.size() == 1) {
                validateSingleRectangleBoundingBox(rectangles.get(0), calculatedBoundingBox);
            } else {
                validateMultipleRectanglesBoundingBox(rectangles, calculatedBoundingBox);
            }
        }

        /**
         * For single rectangle collections, the bounding box should equal the rectangle itself.
         */
        private void validateSingleRectangleBoundingBox(Rectangle singleRectangle, Rectangle boundingBox) {
            assertEquals("Single rectangle bounding box should equal the rectangle itself",
                        singleRectangle, boundingBox.getBoundingBox());
        }

        /**
         * For multiple rectangle collections, validates that the bounding box contains all rectangles
         * and handles world-wrapping scenarios correctly.
         */
        private void validateMultipleRectanglesBoundingBox(List<Rectangle> rectangles, Rectangle boundingBox) {
            // Verify bounding box contains all constituent rectangles
            for (Rectangle rectangle : rectangles) {
                assertRelation("Bounding box must contain all constituent rectangles", 
                              CONTAINS, boundingBox, rectangle);
            }
            
            // Special validation for geographic world-wrapping scenarios
            if (isWorldWrappingBoundingBox(boundingBox)) {
                validateWorldWrappingBoundingBox(rectangles);
            }
        }

        /**
         * Checks if the bounding box represents a world-wrapping scenario.
         */
        private boolean isWorldWrappingBoundingBox(Rectangle boundingBox) {
            return ctx.isGeo() && 
                   boundingBox.getMinX() == -180 && 
                   boundingBox.getMaxX() == 180;
        }

        /**
         * For world-wrapping bounding boxes, validates that every longitude is covered by at least one rectangle.
         */
        private void validateWorldWrappingBoundingBox(List<Rectangle> rectangles) {
            int testLongitude = randomIntBetween(-180, 180);
            boolean longitudeCovered = false;
            
            for (Rectangle rectangle : rectangles) {
                if (rectangle.relateXRange(testLongitude, testLongitude).intersects()) {
                    longitudeCovered = true;
                    break;
                }
            }
            
            if (!longitudeCovered) {
                fail(String.format(
                    "World-wrapping bounding box should cover longitude %d, but none of the rectangles do: %s",
                    testLongitude, rectangles));
            }
        }
    }
}