/*******************************************************************************
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.shape;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BufferedLineStringTest extends RandomizedTest {

    // Configure non-geo context with specific world bounds
    private static final double WEST = -100;
    private static final double EAST = 100;
    private static final double SOUTH = -50;
    private static final double NORTH = 50;
    
    private final SpatialContext ctx = new SpatialContextFactory() {{
        geo = false;
        worldBounds = new RectangleImpl(WEST, EAST, SOUTH, NORTH, null);
    }}.newSpatialContext();

    /**
     * Tests intersection relationships between randomly generated BufferedLineString shapes
     * and rectangles to ensure correct spatial relation behavior.
     */
    @Test
    public void testRectIntersect() {
        new BufferedLineStringRectIntersectionTester().testRelateWithRectangle();
    }

    /**
     * Helper class to generate random BufferedLineString shapes and test their
     * spatial relationships with rectangles.
     */
    private class BufferedLineStringRectIntersectionTester extends RectIntersectionTestHelper<BufferedLineString> {
        
        private static final int MIN_POINTS = 2;
        private static final int MAX_ADDITIONAL_POINTS = 3;
        
        public BufferedLineStringRectIntersectionTester() {
            super(ctx);
        }

        /**
         * Generates a random BufferedLineString near the specified point.
         * 
         * @param nearPoint the point around which to generate the shape
         * @return randomly generated BufferedLineString
         */
        @Override
        protected BufferedLineString generateRandomShape(Point nearPoint) {
            // Create bounding box around nearPoint for shape generation
            Rectangle boundingBox = randomRectangle(nearPoint);
            
            // Determine number of points (2-5)
            int pointCount = MIN_POINTS + randomInt(MAX_ADDITIONAL_POINTS);
            
            // Generate random control points within bounding box
            List<Point> controlPoints = new ArrayList<>(pointCount);
            for (int i = 0; i < pointCount; i++) {
                controlPoints.add(randomPointIn(boundingBox));
            }
            
            // Calculate appropriate buffer distance based on bounding box size
            double maxDimension = Math.max(boundingBox.getWidth(), boundingBox.getHeight());
            double bufferDistance = Math.abs(randomGaussian()) * maxDimension / 4;
            bufferDistance = randomInt((int) divisible(bufferDistance));
            
            return new BufferedLineString(controlPoints, bufferDistance, ctx);
        }

        /**
         * Selects a random point from an empty BufferedLineString's control points.
         * 
         * @param shape the BufferedLineString shape (expected to be empty)
         * @return random control point from the shape
         */
        @Override
        protected Point randomPointInEmptyShape(BufferedLineString shape) {
            List<Point> controlPoints = shape.getPoints();
            int randomIndex = randomInt(controlPoints.size() - 1);
            return controlPoints.get(randomIndex);
        }
    }
}