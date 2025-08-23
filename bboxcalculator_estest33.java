package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expanding the bounding box along the X-axis does not
     * affect the Y-axis boundaries. The Y boundaries should remain at their
     * initial, unset state.
     */
    @Test
    public void expandXRangeShouldNotModifyYBounds() {
        // Arrange
        // Use a geographic context, as it has more complex logic for the X-axis (longitude).
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // The initial state for Y is min=+infinity, max=-infinity.
        double expectedMinY = Double.POSITIVE_INFINITY;
        double expectedMaxY = Double.NEGATIVE_INFINITY;

        // Act
        // Expand the X-range. The specific values are not critical for this test;
        // the key is that we are only modifying the X-axis.
        bboxCalculator.expandXRange(-10, 10);

        // Assert
        // Verify that the Y boundaries have not changed from their initial state.
        assertEquals(
            "Min Y should not be modified by expandXRange",
            expectedMinY,
            bboxCalculator.getMinY(),
            0.0
        );
        assertEquals(
            "Max Y should not be modified by expandXRange",
            expectedMaxY,
            bboxCalculator.getMaxY(),
            0.0
        );
    }
}