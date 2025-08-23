package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BBoxCalculator}.
 * This test suite focuses on verifying the behavior of the bounding box calculation logic.
 */
public class BBoxCalculatorTest {

    /**
     * Verifies that expanding only the X-axis of the BBoxCalculator does not alter the
     * Y-axis boundaries. The Y-axis should remain in its initial, un-set state.
     * This behavior is crucial as it confirms the independence of the axis calculations.
     */
    @Test
    public void expandXRangeShouldNotAffectYBounds() {
        // Arrange: Create a BBoxCalculator in a geographic context.
        // The Y-bounds are initialized to positive/negative infinity, representing an empty state.
        BBoxCalculator bboxCalculator = new BBoxCalculator(SpatialContext.GEO);

        // Pre-condition check: Verify the initial state of the Y-bounds.
        assertEquals("Initial minY should be positive infinity",
                Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), 0.0);
        assertEquals("Initial maxY should be negative infinity",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.0);

        // Act: Expand the X-range. The original test used unusual infinite values,
        // but any valid range demonstrates the same principle more clearly.
        bboxCalculator.expandXRange(0.0, 10.0);
        bboxCalculator.expandXRange(20.0, 30.0);

        // Assert: The Y-bounds should remain unchanged from their initial state.
        assertEquals("minY should be unaffected by expandXRange",
                Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), 0.0);
        assertEquals("maxY should be unaffected by expandXRange",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.0);
    }
}