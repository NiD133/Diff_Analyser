package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the BBoxCalculator class.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expandRange correctly updates the min and max Y coordinates
     * based on the provided values, even if the input minY is greater than maxY.
     * The BBoxCalculator should simply expand its bounds to include both values independently.
     */
    @Test
    public void expandRangeShouldUpdateMinAndMaxYIndependently() {
        // Arrange
        // Using the default geodetic SpatialContext is sufficient for this test.
        SpatialContext spatialContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(spatialContext);

        // The test focuses on the Y coordinates. X values are arbitrary.
        // The key aspect is that the input minY (0.0) is greater than the input maxY (-388.0).
        double arbitraryMinX = 10.0;
        double arbitraryMaxX = 20.0;
        double inputMinY = 0.0;
        double inputMaxY = -388.0;

        // Act
        // The BBoxCalculator is initially empty (minY=inf, maxY=-inf).
        // After this call, the new minY should be min(inf, 0.0) = 0.0
        // and the new maxY should be max(-inf, -388.0) = -388.0.
        bboxCalculator.expandRange(arbitraryMinX, arbitraryMaxX, inputMinY, inputMaxY);

        // Assert
        // The delta is used for floating-point comparisons.
        final double delta = 0.01;
        
        assertEquals("Min Y should be updated to the input minY value.",
                inputMinY, bboxCalculator.getMinY(), delta);

        assertEquals("Max Y should be updated to the input maxY value.",
                inputMaxY, bboxCalculator.getMaxY(), delta);
    }
}