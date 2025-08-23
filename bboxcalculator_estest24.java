package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link BBoxCalculator} class.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that a newly created BBoxCalculator is initialized with inverted,
     * infinite bounds. This is a standard practice to ensure that the first
     * coordinate or shape added correctly establishes the initial bounding box.
     */
    @Test
    public void newCalculatorShouldHaveDefaultInfiniteBounds() {
        // Arrange: Create a new BBoxCalculator.
        // A default geodetic context is sufficient for testing the initial state.
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);

        // Act & Assert: Verify the initial state of the bounding box coordinates.
        // The min values should be positive infinity and max values negative infinity.
        // This ensures any valid coordinate will be "smaller" than the min and
        // "larger" than the max, correctly setting the box on the first expansion.

        assertEquals("Initial minX should be positive infinity",
                Double.POSITIVE_INFINITY, calculator.getMinX(), 0.0);
        assertEquals("Initial maxX should be negative infinity",
                Double.NEGATIVE_INFINITY, calculator.getMaxX(), 0.0);
        assertEquals("Initial minY should be positive infinity",
                Double.POSITIVE_INFINITY, calculator.getMinY(), 0.0);
        assertEquals("Initial maxY should be negative infinity",
                Double.NEGATIVE_INFINITY, calculator.getMaxY(), 0.0);
    }
}