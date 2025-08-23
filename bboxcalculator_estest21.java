package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * This test verifies the initial state of a newly created BBoxCalculator.
     * A new calculator should have its bounding box extents initialized to inverted
     * infinities, signifying that it is empty and has not yet processed any shapes.
     */
    @Test
    public void newBBoxCalculatorShouldHaveDefaultInfiniteBounds() {
        // --- ARRANGE ---
        // A BBoxCalculator requires a SpatialContext. We use the standard geodetic context.
        final SpatialContext geoContext = SpatialContext.GEO;
        final BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // --- ACT ---
        // No action is performed. We are testing the state immediately after construction.

        // --- ASSERT ---
        // Verify that the initial Y boundaries are inverted infinities,
        // indicating that no vertical range has been defined yet.
        assertEquals("Initial minY should be positive infinity",
                Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), 0.0);
        assertEquals("Initial maxY should be negative infinity",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.0);

        // For completeness, also verify the initial X boundaries, which follow the same pattern.
        assertEquals("Initial minX should be positive infinity",
                Double.POSITIVE_INFINITY, bboxCalculator.getMinX(), 0.0);
        assertEquals("Initial maxX should be negative infinity",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxX(), 0.0);
    }
}