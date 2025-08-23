package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    private static final double TOLERANCE = 0.0;

    @Test
    public void populationCovariance_withSingleDataPoint_isZero() {
        // Arrange
        // The specific values do not matter, only that there is a single data point.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(10.0, 20.0);

        // Act
        double covariance = accumulator.populationCovariance();

        // Assert
        // The population covariance of a dataset with a single point is defined as 0.0.
        assertEquals(
                "Population covariance of a single point should be 0.0",
                0.0,
                covariance,
                TOLERANCE);
    }
}