package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    private final SpatialContext spatialContext = new SpatialContextFactory().newSpatialContext();

    @Test
    public void getMinYShouldReturnCorrectValueAfterExpandingWithSingleRange() {
        // Arrange
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);
        
        // Define the range to be added to the calculator
        final double expectedMinY = -388.0;
        final double rangeMaxY = 0.0;
        final double rangeMinX = 1.0;
        final double rangeMaxX = 1.0;

        // Act
        // Expand the calculator's bounding box with the new range
        bboxCalculator.expandRange(rangeMinX, rangeMaxX, expectedMinY, rangeMaxY);
        double actualMinY = bboxCalculator.getMinY();

        // Assert
        // Verify that the calculator's minimum Y is now the minimum Y from the added range
        assertEquals("The minimum Y should be updated after the first expansion.",
                expectedMinY, actualMinY, 0.01);
    }
}