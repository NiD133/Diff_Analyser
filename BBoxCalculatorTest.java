/*******************************************************************************
 * Copyright (c) 2015 David Smiley
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.shape.impl;

import com.carrotsearch.randomizedtesting.annotations.Repeat;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.RandomizedShapeTest;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BBoxCalculatorTest extends RandomizedShapeTest {

    public BBoxCalculatorTest() {
        super(SpatialContext.GEO);
    }

    @Test
    @Repeat(iterations = 100)
    public void testGeoLongitude() {
        // Setup: Create calculator and random rectangles
        final int numShapes = randomIntBetween(1, 4);
        List<Rectangle> rectangles = generateRandomRectangles(numShapes);
        BBoxCalculator calculator = new BBoxCalculator(ctx);
        
        // Exercise: Expand calculator with all rectangles
        for (Rectangle rect : rectangles) {
            calculator.expandRange(rect);
        }
        Rectangle boundary = calculator.getBoundary();

        // Verify results based on number of shapes
        if (numShapes == 1) {
            verifySingleRectangleCase(rectangles.get(0), boundary);
        } else if (isWorldWideBoundary(boundary)) {
            verifyWorldWideBoundaryCoverage(rectangles);
        } else {
            verifyMultiRectangleCase(rectangles, boundary);
        }
    }

    private List<Rectangle> generateRandomRectangles(int count) {
        List<Rectangle> rects = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            // 30° divisible rectangles avoid edge cases with fractional degrees
            rects.add(randomRectangle(30));
        }
        return rects;
    }

    private void verifySingleRectangleCase(Rectangle expected, Rectangle actual) {
        assertEquals("Single rectangle should match calculated boundary", expected, actual);
    }

    private boolean isWorldWideBoundary(Rectangle boundary) {
        return boundary.getMinX() == -180 && boundary.getMaxX() == 180;
    }

    private void verifyWorldWideBoundaryCoverage(List<Rectangle> rectangles) {
        // Verify every longitude is covered by at least one rectangle
        for (int lon = -180; lon <= 180; lon++) {
            assertTrue("Longitude " + lon + " should be covered by at least one rectangle",
                isLonCoveredByAnyRectangle(rectangles, lon));
        }
    }

    private void verifyMultiRectangleCase(List<Rectangle> rectangles, Rectangle boundary) {
        // Verify boundary contains all input rectangles
        for (Rectangle rect : rectangles) {
            assertRelation("Boundary should contain rectangle: " + rect,
                SpatialRelation.CONTAINS, boundary, rect);
        }

        verifyBoundaryEdges(rectangles, boundary);
        
        // Only check minimality for boundaries wider than 180°
        if (boundary.getWidth() > 180) {
            verifyMinimalBoundary(rectangles, boundary);
        }
    }

    private void verifyBoundaryEdges(List<Rectangle> rectangles, Rectangle boundary) {
        final double leftBoundary = boundary.getMinX();
        final double rightBoundary = boundary.getMaxX();

        // Left edge verification
        assertTrue("Left boundary " + leftBoundary + " should be covered by at least one rectangle",
            isLonCoveredByAnyRectangle(rectangles, leftBoundary));
        assertFalse("Area just left of boundary should not be covered",
            isLonCoveredByAnyRectangle(rectangles, normalizeX(leftBoundary - 0.5)));

        // Right edge verification
        assertTrue("Right boundary " + rightBoundary + " should be covered by at least one rectangle",
            isLonCoveredByAnyRectangle(rectangles, rightBoundary));
        assertFalse("Area just right of boundary should not be covered",
            isLonCoveredByAnyRectangle(rectangles, normalizeX(rightBoundary + 0.5)));
    }

    private void verifyMinimalBoundary(List<Rectangle> rectangles, Rectangle boundary) {
        /*
         * Verify the boundary is minimal by ensuring the gap opposite the boundary
         * is the largest possible. We attempt to find a gap larger than the current
         * maximum gap (360° - boundary width), which should fail if the boundary is minimal.
         */
        final double currentGapSize = 360.0 - boundary.getWidth();
        final double testGapSize = currentGapSize + 0.5; // Try a gap larger than current max

        for (Rectangle rect : rectangles) {
            // Position test gap immediately right of this rectangle
            final double gapStart = rect.getMaxX() + 0.25;
            final double gapEnd = gapStart + testGapSize;
            Rectangle testGap = makeNormalizedRectangle(gapStart, gapEnd, -90, 90);

            // Verify this larger gap conflicts with at least one rectangle
            assertTrue("Should not fit gap of size " + testGapSize,
                gapConflictsWithRectangles(testGap, rectangles));
        }
    }

    private boolean gapConflictsWithRectangles(Rectangle gap, List<Rectangle> rectangles) {
        for (Rectangle rect : rectangles) {
            if (rect.relate(gap).intersects()) {
                return true;
            }
        }
        return false;
    }

    private boolean isLonCoveredByAnyRectangle(List<Rectangle> rectangles, double longitude) {
        for (Rectangle rect : rectangles) {
            if (rect.relateXRange(longitude, longitude).intersects()) {
                return true;
            }
        }
        return false;
    }

    private double normalizeX(double x) {
        return ctx.getShapeFactory().normX(x);
    }

    private Rectangle makeNormalizedRectangle(double minX, double maxX, double minY, double maxY) {
        return ctx.getShapeFactory().rect(minX, maxX, minY, maxY);
    }
}