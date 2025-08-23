package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Tests that pearsonsCorrelationCoefficient() throws an IllegalStateException when the
     * population variance of the data is zero.
     */
    @Test(expected = IllegalStateException.class)
    public void pearsonsCorrelationCoefficient_throwsIllegalStateException_whenVarianceIsZero() {
        // Arrange: Create an accumulator and add data points that result in zero variance.
        // By adding the same (x, y) pair multiple times, the population variance for both
        // x and y datasets will be zero.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(10.0, 20.0);
        accumulator.add(10.0, 20.0);

        // Act: Attempt to calculate the Pearson's correlation coefficient.
        // This is expected to throw an exception because the calculation is undefined
        // when either dataset has zero variance.
        accumulator.pearsonsCorrelationCoefficient();

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}