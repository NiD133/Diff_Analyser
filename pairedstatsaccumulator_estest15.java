package com.google.common.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    public void sampleCovariance_withTwoIdenticalPoints_isZero() {
        // Arrange: Create an accumulator and add the same data point twice.
        // The sample covariance should be zero when there is no variation in the data.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(50.0, 150.0);
        accumulator.add(50.0, 150.0);

        // Act: Calculate the sample covariance.
        double actualCovariance = accumulator.sampleCovariance();

        // Assert: The result should be zero.
        assertEquals(0.0, actualCovariance, TOLERANCE);
    }
}