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

/**
 * Tests for BufferedLineString shape operations, particularly intersection relationships with rectangles.
 */
public class BufferedLineStringTest extends RandomizedTest {

    // Test constants
    private static final int MIN_POINTS_IN_LINE = 2;
    private static final int MAX_ADDITIONAL_POINTS = 3; // Results in 2-5 total points
    private static final double BUFFER_SCALE_FACTOR = 4.0;
    
    // Non-geographic spatial context with custom world bounds for testing
    private final SpatialContext spatialContext = createTestSpatialContext();

    /**
     * Creates a spatial context suitable for testing with non-geographic coordinates.
     */
    private SpatialContext createTestSpatialContext() {
        return new SpatialContextFactory() {{
            geo = false;
            worldBounds = new RectangleImpl(-100, 100, -50, 50, null);
        }}.newSpatialContext();
    }

    /**
     * Tests that BufferedLineString correctly calculates spatial relationships (intersects, contains, etc.)
     * with rectangles using randomized test data.
     */
    @Test
    public void testBufferedLineStringIntersectionWithRectangles() {
        BufferedLineStringIntersectionTester tester = new BufferedLineStringIntersectionTester(spatialContext);
        tester.testRelateWithRectangle();
    }

    /**
     * Helper class that generates random BufferedLineString shapes and tests their intersection
     * relationships with rectangles.
     */
    private class BufferedLineStringIntersectionTester extends RectIntersectionTestHelper<BufferedLineString> {

        public BufferedLineStringIntersectionTester(SpatialContext context) {
            super(context);
        }

        /**
         * Generates a random BufferedLineString near the given point for testing.
         * 
         * @param nearPoint The point around which to generate the line string
         * @return A randomly generated BufferedLineString with 2-5 points and appropriate buffer
         */
        @Override
        protected BufferedLineString generateRandomShape(Point nearPoint) {
            Rectangle boundingArea = randomRectangle(nearPoint);
            List<Point> linePoints = generateRandomPointsInArea(boundingArea);
            double bufferDistance = calculateRandomBufferDistance(boundingArea);
            
            return new BufferedLineString(linePoints, bufferDistance, spatialContext);
        }

        /**
         * Generates 2-5 random points within the specified rectangular area.
         */
        private List<Point> generateRandomPointsInArea(Rectangle area) {
            int totalPoints = MIN_POINTS_IN_LINE + randomInt(MAX_ADDITIONAL_POINTS);
            List<Point> points = new ArrayList<>(totalPoints);
            
            for (int i = 0; i < totalPoints; i++) {
                points.add(randomPointIn(area));
            }
            
            return points;
        }

        /**
         * Calculates a random buffer distance based on the dimensions of the bounding area.
         * The buffer is scaled to be proportional to the area size.
         */
        private double calculateRandomBufferDistance(Rectangle boundingArea) {
            double maxDimension = Math.max(boundingArea.getWidth(), boundingArea.getHeight());
            double gaussianBuffer = Math.abs(randomGaussian()) * maxDimension / BUFFER_SCALE_FACTOR;
            
            // Ensure buffer is a whole number for consistency
            return randomInt((int) divisible(gaussianBuffer));
        }

        /**
         * Returns a random point that lies on the line string itself (not in the buffer area).
         * This is used for testing edge cases where shapes are expected to be empty.
         * 
         * @param lineString The BufferedLineString to get a point from
         * @return A point that lies exactly on one of the line segments
         */
        @Override
        protected Point randomPointInEmptyShape(BufferedLineString lineString) {
            List<Point> controlPoints = lineString.getPoints();
            int randomIndex = randomInt(controlPoints.size());
            return controlPoints.get(randomIndex);
        }
    }
}