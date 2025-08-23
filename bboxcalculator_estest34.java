package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expanding only the X-range (longitude) of a BBoxCalculator
     * does not alter the previously set Y-range (latitude).
     */
    @Test
    public void expandXRangeShouldNotAffectYBounds() {
        // ARRANGE
        // Use a geographic context, as it has more complex logic for bounding boxes.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // Establish an initial bounding box with a known Y-range.
        final double initialMinY = 10.0;
        final double initialMaxY = 20.0;
        bboxCalculator.expandRange(0, 90, initialMinY, initialMaxY);

        // ACT
        // Expand the calculator's bounds, but only along the X-axis.
        bboxCalculator.expandXRange(100, 110);

        // The getBoundary() method must be called to finalize calculations
        // before querying the final min/max values.
        bboxCalculator.getBoundary();

        // ASSERT
        // Verify that the Y-range remains unchanged.
        assertEquals("Min Y should not be changed by expanding the X-range.",
                initialMinY, bboxCalculator.getMinY(), 0.0);
        assertEquals("Max Y should not be changed by expanding the X-range.",
                initialMaxY, bboxCalculator.getMaxY(), 0.0);
    }
}