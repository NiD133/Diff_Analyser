package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Verifies that attempting to calculate the sample covariance on an empty accumulator
     * results in an IllegalStateException, as the calculation is undefined for a dataset
     * with fewer than two entries.
     */
    @Test(expected = IllegalStateException.class)
    public void sampleCovariance_onEmptyAccumulator_throwsIllegalStateException() {
        // Arrange: Create an empty accumulator with no data points.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act: Attempt to calculate the sample covariance.
        // Assert: An IllegalStateException is expected, as declared by the @Test annotation.
        accumulator.sampleCovariance();
    }
}