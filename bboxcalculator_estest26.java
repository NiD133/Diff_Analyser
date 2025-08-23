package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that calling getBoundary() throws a RuntimeException if the Y-axis range
     * has not been initialized.
     *
     * The BBoxCalculator starts with an invalid Y-range (minY=+Infinity, maxY=-Infinity).
     * If only the X-range is expanded, the Y-range remains invalid. This test ensures
     * that attempting to create a boundary rectangle in this state fails with a clear
     * error message.
     */
    @Test
    public void getBoundaryShouldThrowExceptionWhenYRangeIsUninitialized() {
        // Arrange: Create a BBoxCalculator and expand only the X-range.
        // This leaves the Y-range in its initial, invalid state where minY > maxY.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);
        bboxCalculator.expandXRange(0, 10); // Expand X, but not Y.

        // Act & Assert: Verify that getBoundary() throws a RuntimeException because the
        // Y-range is invalid (minY > maxY).
        RuntimeException exception = assertThrows(
            "Expected an exception due to uninitialized Y-range",
            RuntimeException.class,
            bboxCalculator::getBoundary
        );

        // Verify the exception message to confirm the cause of the failure.
        String expectedMessage = "maxY must be >= minY: Infinity to -Infinity";
        assertEquals(expectedMessage, exception.getMessage());
    }
}