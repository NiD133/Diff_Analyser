package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 * This focuses on specific behaviors of the bounding box calculation logic.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expanding only the X-range (longitude) does not affect the Y-range (latitude).
     * The Y-range coordinates should remain in their initial, unexpanded state.
     */
    @Test
    public void expandXRange_shouldNotModifyYCoordinates() {
        // Arrange: Create a calculator for a geodetic context.
        // By default, minY is Double.POSITIVE_INFINITY and maxY is Double.NEGATIVE_INFINITY.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(geoContext);

        // Act: Expand the bounding box along the X-axis (longitude) multiple times.
        // These operations should only affect the X-dimension of the bounding box.
        calculator.expandXRange(-10.0, 10.0);
        calculator.expandXRange(170.0, -170.0); // A range crossing the anti-meridian

        // Assert: Verify that the Y-coordinates remain at their initial, sentinel values.
        String minYMessage = "Min Y should remain at its initial positive infinity value after only X-expansions.";
        assertEquals(minYMessage, Double.POSITIVE_INFINITY, calculator.getMinY(), 0.0);

        String maxYMessage = "Max Y should remain at its initial negative infinity value after only X-expansions.";
        assertEquals(maxYMessage, Double.NEGATIVE_INFINITY, calculator.getMaxY(), 0.0);
    }
}