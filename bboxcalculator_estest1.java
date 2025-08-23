package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expandXRange correctly merges overlapping ranges and does not
     * modify the Y-axis boundaries.
     */
    @Test
    public void expandXRange_withOverlappingRanges_shouldMergeAndNotAffectYAxis() {
        // ARRANGE: Set up a BBoxCalculator with a standard Cartesian context.
        // A Cartesian context is used because it has simpler, non-wrapping logic.
        SpatialContext cartesianContext = new SpatialContext(false); // isGeo = false
        BBoxCalculator calculator = new BBoxCalculator(cartesianContext);

        // The initial state of the Y-axis is min=inf, max=-inf.
        // This test will verify that these values remain unchanged by X-axis operations.
        final double initialMinY = Double.POSITIVE_INFINITY;
        final double initialMaxY = Double.NEGATIVE_INFINITY;

        // ACT: Expand the X-axis with two overlapping ranges.
        // The first range is (-inf, 0.0).
        calculator.expandXRange(Double.NEGATIVE_INFINITY, 0.0);
        // The second range (-inf, 100.0) completely contains the first.
        // The calculator should merge them into a single, larger range: (-inf, 100.0).
        calculator.expandXRange(Double.NEGATIVE_INFINITY, 100.0);

        // ASSERT: Verify the final state of the bounding box.

        // 1. The X-axis should reflect the merged range.
        assertEquals("Min X should be negative infinity",
                Double.NEGATIVE_INFINITY, calculator.getMinX(), 0.0);
        assertEquals("Max X should be the maximum of the merged ranges",
                100.0, calculator.getMaxX(), 0.0);

        // 2. The Y-axis should remain in its initial, unexpanded state.
        assertEquals("Min Y should not be affected by expandXRange",
                initialMinY, calculator.getMinY(), 0.0);
        assertEquals("Max Y should not be affected by expandXRange",
                initialMaxY, calculator.getMaxY(), 0.0);
    }
}