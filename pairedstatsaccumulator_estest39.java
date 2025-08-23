package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test(expected = IllegalStateException.class)
    public void populationCovariance_onEmptyAccumulator_throwsIllegalStateException() {
        // Arrange: Create an accumulator with no data added.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act: Attempt to calculate the population covariance on the empty accumulator.
        // Assert: An IllegalStateException is expected because the calculation is
        // undefined for an empty dataset. This is handled by the @Test annotation.
        accumulator.populationCovariance();
    }
}