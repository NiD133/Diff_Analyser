package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Test suite for {@link BBoxCalculator}.
 * This class focuses on verifying the behavior of BBoxCalculator, particularly its handling of edge cases.
 */
public class BBoxCalculatorTest {

    /**
     * Verifies that creating a BBoxCalculator with a null SpatialContext
     * and then calling a method on it results in a NullPointerException.
     * This ensures the constructor's preconditions are handled correctly downstream.
     */
    @Test(expected = NullPointerException.class)
    public void expandXRangeShouldThrowNPEWhenContextIsNull() {
        // Arrange: Create a BBoxCalculator with a null SpatialContext, which is an invalid state.
        BBoxCalculator calculator = new BBoxCalculator((SpatialContext) null);

        // Act: Attempt to use the calculator. This is expected to throw a NullPointerException
        // because the internal context is null and required for calculations.
        // The specific arguments (0, 0) are arbitrary as the exception should precede their use.
        calculator.expandXRange(0, 0);

        // Assert: The test framework asserts that a NullPointerException was thrown,
        // as specified by the 'expected' attribute on the @Test annotation.
    }
}