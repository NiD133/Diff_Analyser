package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    // A delta of 0.0 is used for exact comparisons of infinity constants.
    private static final double DELTA = 0.0;

    /**
     * Tests that a newly instantiated BBoxCalculator has inverted, infinite bounds,
     * which is the expected initial state before any shapes are added.
     */
    @Test
    public void newBBoxCalculatorShouldHaveInfiniteBounds() {
        // Arrange: Create a BBoxCalculator with a standard geographic context.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // Act & Assert: Verify the initial boundary values.
        // An empty calculator should have its min values set to positive infinity
        // and its max values set to negative infinity, representing an empty state.
        assertEquals("Initial minX should be positive infinity",
                Double.POSITIVE_INFINITY, bboxCalculator.getMinX(), DELTA);
        assertEquals("Initial maxX should be negative infinity",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxX(), DELTA);
        assertEquals("Initial minY should be positive infinity",
                Double.POSITIVE_INFINITY, bboxCalculator.getMinY(), DELTA);
        assertEquals("Initial maxY should be negative infinity",
                Double.NEGATIVE_INFINITY, bboxCalculator.getMaxY(), DELTA);
    }
}