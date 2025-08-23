package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the BBoxCalculator.
 * This particular test was improved for clarity from an auto-generated version.
 */
public class BBoxCalculator_ESTestTest28 {

    /**
     * Tests that the maximum Y-coordinate (maxY) is correctly updated from its
     * initial state (Double.NEGATIVE_INFINITY) when a new range is added.
     * This test specifically verifies the handling of a negative maxY value.
     */
    @Test
    public void expandRangeWithNegativeValueShouldUpdateMaxY() {
        // Arrange: Create a BBoxCalculator for a geodetic context.
        // The initial maxY of a new calculator is Double.NEGATIVE_INFINITY.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);
        assertEquals("Initial maxY should be negative infinity",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), 0.0);

        // Define the input for the range expansion. The key value is the new maxY.
        // The other coordinate values are irrelevant to this specific assertion.
        double newMaxY = -5743.7453681;
        double irrelevantMinX = 0;
        double irrelevantMaxX = 0;
        double irrelevantMinY = Double.POSITIVE_INFINITY;

        // Act: Expand the bounding box with a new range. The calculator should update
        // its maxY to the maximum of its current value and the new one.
        bboxCalculator.expandRange(irrelevantMinX, irrelevantMaxX, irrelevantMinY, newMaxY);

        // Assert: The calculator's maxY should be updated to the new negative value.
        assertEquals("maxY should be updated to the new negative value",
                newMaxY, bboxCalculator.getMaxY(), 0.01);
    }
}