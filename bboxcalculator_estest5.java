package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BBoxCalculator} class.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that after expanding the BBoxCalculator with a single range,
     * the getter methods return the correct coordinate values.
     * This specific test verifies getMinX() and getMaxY().
     */
    @Test
    public void expandRangeShouldCorrectlyUpdateBoundaryCoordinates() {
        // Arrange
        // Create a standard spatial context and the calculator under test.
        SpatialContext ctx = new SpatialContextFactory().newSpatialContext();
        BBoxCalculator calculator = new BBoxCalculator(ctx);

        // Define the range to expand the bounding box with.
        // Note: The original test used an inverted Y-range (minY > maxY),
        // which this test preserves to check this edge case.
        double rangeMinX = 0.0;
        double rangeMaxX = 1.0;
        double rangeMinY = 1.0;
        double rangeMaxY = -388.0;

        // Act
        // Expand the calculator's bounding box with the defined range.
        calculator.expandRange(rangeMinX, rangeMaxX, rangeMinY, rangeMaxY);

        // Assert
        // The calculator's initial state has min/max values at +/- infinity.
        // After expansion, the minX should be updated to rangeMinX, and maxY to rangeMaxY.
        double delta = 0.01;
        assertEquals("Min X should be updated to the value from the new range.",
                rangeMinX, calculator.getMinX(), delta);
        assertEquals("Max Y should be updated to the value from the new range.",
                rangeMaxY, calculator.getMaxY(), delta);
    }
}