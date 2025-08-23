package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expanding only the X-axis range does not affect the Y-axis bounds.
     * The Y-axis bounds should remain in their initial, uninitialized state.
     */
    @Test
    public void expandXRangeShouldNotAffectYBounds() {
        // ARRANGE: Create a BBoxCalculator with a geographic context.
        // A new calculator starts with Y bounds at positive/negative infinity.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bBoxCalculator = new BBoxCalculator(geoContext);

        // Sanity check the initial state before acting.
        assertEquals("Initial minY should be positive infinity",
                Double.POSITIVE_INFINITY, bBoxCalculator.getMinY(), 0.0);
        assertEquals("Initial maxY should be negative infinity",
                Double.NEGATIVE_INFINITY, bBoxCalculator.getMaxY(), 0.0);

        // ACT: Expand the X-axis range. The original test used NaN, which is a
        // valid edge case to ensure stability. This operation should not
        // modify the Y-axis bounds.
        bBoxCalculator.expandXRange(10.0, Double.NaN);

        // ASSERT: Verify that the Y-axis bounds have not changed from their initial state.
        assertEquals("minY should remain unchanged after expandXRange",
                Double.POSITIVE_INFINITY, bBoxCalculator.getMinY(), 0.0);
        assertEquals("maxY should remain unchanged after expandXRange",
                Double.NEGATIVE_INFINITY, bBoxCalculator.getMaxY(), 0.0);
    }
}