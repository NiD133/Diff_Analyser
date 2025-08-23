package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link BBoxCalculator} class.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that calling expandRange with a null Rectangle argument
     * correctly throws a NullPointerException.
     */
    @Test
    public void expandRange_withNullRectangle_shouldThrowNullPointerException() {
        // Arrange: Create a BBoxCalculator with a standard geographic context.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(geoContext);

        // Act & Assert: Verify that a NullPointerException is thrown when a null
        // rectangle is passed to the expandRange method.
        assertThrows(NullPointerException.class, () -> {
            calculator.expandRange(null);
        });
    }
}