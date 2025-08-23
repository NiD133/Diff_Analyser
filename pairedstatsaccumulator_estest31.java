package com.google.common.math;

import static org.junit.Assert.assertThrows;
import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Verifies that calling leastSquaresFit() on an accumulator with no data
     * throws an IllegalStateException, as there is not enough data to perform
     * the calculation.
     */
    @Test
    public void leastSquaresFit_whenAccumulatorIsEmpty_throwsIllegalStateException() {
        // Arrange: Create an empty accumulator.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act & Assert: Expect an IllegalStateException when calculating the least squares fit.
        // The method requires at least two data points for this calculation.
        assertThrows(IllegalStateException.class, accumulator::leastSquaresFit);
    }
}