package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test(expected = IllegalStateException.class)
    public void pearsonsCorrelationCoefficient_whenAccumulatorIsEmpty_throwsIllegalStateException() {
        // Arrange: Create an accumulator with no data.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act: Attempt to calculate the correlation coefficient, which is undefined for an empty set.
        accumulator.pearsonsCorrelationCoefficient();

        // Assert: The @Test(expected=...) annotation handles the exception assertion.
    }
}