package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Verifies that leastSquaresFit() throws an IllegalStateException when the accumulator
     * contains only a single data point, as a linear fit is undefined in this case.
     */
    @Test(expected = IllegalStateException.class)
    public void leastSquaresFit_withSingleDataPoint_throwsIllegalStateException() {
        // Arrange: Create an accumulator and add a single data point.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(10.0, 20.0);

        // Act: Attempt to calculate the least squares fit, which is expected to fail.
        accumulator.leastSquaresFit();

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}