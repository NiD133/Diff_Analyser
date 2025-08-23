package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expanding the bounding box along the X-axis does not affect the Y-axis coordinates.
     * The Y coordinates should remain at their initial default values.
     */
    @Test
    public void expandXRange_shouldNotAffectYCoordinates() {
        // Arrange: Create a BBoxCalculator.
        // By default, minY is +Infinity and maxY is -Infinity.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // Capture the initial Y-axis boundaries for later comparison.
        double initialMinY = bboxCalculator.getMinY();
        double initialMaxY = bboxCalculator.getMaxY();

        // Act: Expand the bounding box along the X-axis.
        // This operation should only modify the X-axis boundaries.
        bboxCalculator.expandXRange(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        // Assert: Verify that the Y-coordinates remain unchanged.
        assertEquals("minY should not be changed by expandXRange",
                initialMinY, bboxCalculator.getMinY(), 0.0);
        assertEquals("maxY should not be changed by expandXRange",
                initialMaxY, bboxCalculator.getMaxY(), 0.0);
    }
}