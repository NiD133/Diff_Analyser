package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    // A small tolerance for floating-point comparisons.
    private static final double TOLERANCE = 1e-8;

    /**
     * Tests that the population covariance is calculated correctly for a dataset with two pairs of
     * values.
     */
    @Test
    public void populationCovariance_withTwoDataPoints_returnsCorrectValue() {
        // ARRANGE
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        final double x1 = 863464.0975884801;
        final double y1 = 38.358751;
        final double x2 = -2001.999;
        final double y2 = -2001.999;

        accumulator.add(x1, y1);
        accumulator.add(x2, y2);

        // For a dataset of two points (x1, y1) and (x2, y2), the population covariance
        // has a simplified formula: (x1 - x2) * (y1 - y2) / 4.
        // This makes the test's expectation clear and verifiable without external tools.
        double expectedPopulationCovariance = (x1 - x2) * (y1 - y2) / 4.0;

        // ACT
        double actualPopulationCovariance = accumulator.populationCovariance();

        // ASSERT
        assertEquals(
            "The calculated population covariance should match the expected value.",
            expectedPopulationCovariance,
            actualPopulationCovariance,
            TOLERANCE);
    }
}