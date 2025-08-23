package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Verifies that calling sampleCovariance() throws an IllegalStateException when only one
     * data pair has been added. The sample covariance is undefined for a single data point
     * because its formula involves division by (n-1), which would be zero.
     */
    @Test(expected = IllegalStateException.class)
    public void sampleCovariance_throwsIllegalStateException_whenOnlyOnePairAdded() {
        // Arrange: Create an accumulator and add a single data pair.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(15.0, 25.0);

        // Act: Attempt to calculate the sample covariance.
        // Assert: An IllegalStateException is expected, as declared by the @Test annotation.
        accumulator.sampleCovariance();
    }
}