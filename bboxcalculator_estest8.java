package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 * This improved version focuses on clarity and maintainability over the original
 * auto-generated test.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that calling expandRange on a new BBoxCalculator correctly sets the initial
     * minimum and maximum Y values. This test verifies the most basic case where the
     * calculator's bounds are established from a single range.
     */
    @Test
    public void expandRange_onNewCalculator_shouldSetInitialYBounds() {
        // Arrange
        // The X values are not relevant for this test, as we only verify the Y bounds.
        final double anyMinX = -104.6;
        final double anyMaxX = 2621.8;

        // Define the Y coordinates for the initial bounding box.
        final double initialMinY = 2621.8399;
        final double initialMaxY = 1.0;
        final double delta = 0.01;

        // A BBoxCalculator needs a SpatialContext, GEO is standard for geographic coordinates.
        BBoxCalculator bboxCalculator = new BBoxCalculator(SpatialContext.GEO);

        // Act
        // Expand the calculator's bounds with the initial range.
        bboxCalculator.expandRange(anyMinX, anyMaxX, initialMinY, initialMaxY);

        // Assert
        // Verify that the calculator's Y bounds match the initial range provided.
        assertEquals("The minimum Y should be set to the initial minY value.",
                initialMinY, bboxCalculator.getMinY(), delta);

        assertEquals("The maximum Y should be set to the initial maxY value.",
                initialMaxY, bboxCalculator.getMaxY(), delta);
    }
}