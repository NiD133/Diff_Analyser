package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that calling expandXRange does not affect the Y-axis boundaries of the
     * bounding box. The Y boundaries should remain at their initial, un-set values.
     */
    @Test
    public void expandXRangeShouldNotAffectYBoundaries() {
        // Arrange: Create a calculator for a geographic context.
        // The initial Y boundaries are positive and negative infinity, respectively.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        double initialMinY = bboxCalculator.getMinY();
        double initialMaxY = bboxCalculator.getMaxY();

        // Act: Expand the X-range with various values, including non-standard ones.
        // These calls should only affect the X-axis boundaries.
        bboxCalculator.expandXRange(10.0, 20.0);
        bboxCalculator.expandXRange(-10.0, 1400.0); // Value outside standard [-180, 180] longitude
        bboxCalculator.expandXRange(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY); // Edge case

        // Assert: Verify that the Y boundaries remain unchanged from their initial state.
        assertEquals("minY should not be changed by expandXRange",
                initialMinY, bboxCalculator.getMinY(), 0.0);
        assertEquals("maxY should not be changed by expandXRange",
                initialMaxY, bboxCalculator.getMaxY(), 0.0);

        // For completeness, we can also assert the known initial values.
        assertEquals("minY should remain at its initial value of +Infinity",
                Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), 0.0);
        assertEquals("maxY should remain at its initial value of -Infinity",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.0);
    }
}