package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Verifies that pearsonsCorrelationCoefficient() throws an IllegalStateException
     * when the y-values have zero variance, as the correlation is undefined in this case.
     */
    @Test(expected = IllegalStateException.class)
    public void pearsonsCorrelationCoefficient_throwsIllegalStateExceptionWhenYValuesHaveZeroVariance() {
        // Arrange: Create an accumulator and add data points where y-values are constant,
        // resulting in zero variance for the y-dataset.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(10.0, 5.0);
        accumulator.add(20.0, 5.0); // Different x, but same y

        // Act: Attempt to calculate the correlation coefficient.
        // This is expected to throw an IllegalStateException.
        accumulator.pearsonsCorrelationCoefficient();
    }
}