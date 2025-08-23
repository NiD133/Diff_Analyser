package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    private static final double TOLERANCE = 1e-9;

    /**
     * Tests that the Pearson's correlation coefficient is -1.0 for a dataset
     * with a perfect negative linear relationship.
     */
    @Test
    public void pearsonsCorrelationCoefficient_forPerfectNegativeCorrelation_returnsNegativeOne() {
        // Arrange: Create an accumulator and add two data points that are perfectly
        // negatively correlated. Any two distinct points forming a line with a
        // negative slope will have a correlation of -1.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(1.0, 3.0); // Point 1
        accumulator.add(2.0, 1.0); // Point 2: As x increases, y decreases.

        // Act: Calculate the Pearson's correlation coefficient.
        double correlation = accumulator.pearsonsCorrelationCoefficient();

        // Assert: The result should be -1.0. A small tolerance is used to account
        // for potential floating-point inaccuracies.
        assertEquals(
                "Pearson's correlation for perfectly negatively correlated data should be -1.0",
                -1.0,
                correlation,
                TOLERANCE);
    }
}