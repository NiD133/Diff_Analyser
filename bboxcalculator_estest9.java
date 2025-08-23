package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expanding only the X-dimension of the bounding box
     * does not alter the existing Y-dimension (latitude) bounds.
     */
    @Test
    public void expandXRangeShouldNotAffectYBounds() {
        // ARRANGE
        // Use a geodetic context, which has special handling for longitude ranges.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // Establish an initial bounding box with a known Y-range.
        final double initialMinY = -90.0;
        final double initialMaxY = -1.0;
        bboxCalculator.expandRange(1.0, 10.0, initialMinY, initialMaxY);

        // Sanity check to ensure the initial Y-bounds are set correctly.
        assertEquals("Pre-condition failed: initial minY is incorrect.",
                initialMinY, bboxCalculator.getMinY(), 0.0);
        assertEquals("Pre-condition failed: initial maxY is incorrect.",
                initialMaxY, bboxCalculator.getMaxY(), 0.0);

        // ACT
        // Expand only the X-range. This operation should not modify the Y-range.
        // The specific X values are not critical to this test's objective.
        bboxCalculator.expandXRange(-4.5, -90.0);

        // ASSERT
        // Verify that the Y-bounds remain unchanged after the X-range expansion.
        assertEquals("minY should not be changed by expandXRange",
                initialMinY, bboxCalculator.getMinY(), 0.0);
        assertEquals("maxY should not be changed by expandXRange",
                initialMaxY, bboxCalculator.getMaxY(), 0.0);
    }
}