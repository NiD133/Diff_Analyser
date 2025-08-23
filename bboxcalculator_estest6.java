package org.locationtech.spatial4j.shape.impl;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    private BBoxCalculator bboxCalculator;

    @Before
    public void setUp() {
        // Using SpatialContext.GEO is a standard way to get a geodetic context.
        SpatialContext spatialContext = SpatialContext.GEO;
        bboxCalculator = new BBoxCalculator(spatialContext);
    }

    /**
     * Tests that calling expandRange on an empty calculator correctly sets the initial
     * bounding box coordinates. This test also covers the edge case where the provided
     * minimum Y is greater than the maximum Y.
     */
    @Test
    public void expandRange_withFirstRectangle_shouldSetInitialBounds() {
        // Arrange: The BBoxCalculator is initialized in setUp() to an empty state.
        // (minX, maxX, minY, maxY are at their respective infinities)
        double minX = -388.0;
        double maxX = 1.0;
        double minY = -388.0;
        // Note: Using a maxY value smaller than minY to test how the calculator handles it.
        double maxY = -2148.3;
        double delta = 0.01;

        // Act: Expand the bounding box with a single rectangle's coordinates.
        bboxCalculator.expandRange(minX, maxX, minY, maxY);

        // Assert: The calculator's bounds should now match the provided coordinates.
        assertEquals("Initial minX should be set", minX, bboxCalculator.getMinX(), delta);
        assertEquals("Initial maxX should be set", maxX, bboxCalculator.getMaxX(), delta);
        assertEquals("Initial minY should be set", minY, bboxCalculator.getMinY(), delta);
        assertEquals("Initial maxY should be set", maxY, bboxCalculator.getMaxY(), delta);
    }
}