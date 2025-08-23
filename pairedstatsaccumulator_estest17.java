package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void populationCovariance_withTwoAnticorrelatedPoints_isNegative() {
        // Arrange: Create an accumulator and add two points that are almost perfectly
        // negatively correlated: (approx. 0, 1) and (1, approx. 0).
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(Double.MIN_VALUE, 1.0);
        accumulator.add(1.0, Double.MIN_VALUE);

        // Act: Calculate the population covariance.
        // For points (0, 1) and (1, 0), the means are μx=0.5 and μy=0.5.
        // The population covariance is the average of (x_i - μx)(y_i - μy):
        //   = 0.5 * [ (0-0.5)*(1-0.5) + (1-0.5)*(0-0.5) ]
        //   = 0.5 * [ (-0.25) + (-0.25) ] = -0.25
        double actualCovariance = accumulator.populationCovariance();

        // Assert: Verify the result is negative and close to -0.25.
        double expectedCovariance = -0.25;
        double tolerance = 1e-9; // A small tolerance for floating-point precision.
        assertEquals(expectedCovariance, actualCovariance, tolerance);
    }
}