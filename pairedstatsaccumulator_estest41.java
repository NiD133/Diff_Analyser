package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    // A small tolerance for floating-point comparisons.
    private static final double TOLERANCE = 1e-9;

    @Test
    public void pearsonsCorrelationCoefficient_withTwoPoints_isPerfectlyPositive() {
        // Arrange: Any two distinct points that don't form a horizontal or vertical line
        // will lie on a perfect line, resulting in a correlation of 1.0 or -1.0.
        // We use simple points that clearly form a line with a positive slope (y = x + 1).
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(1.0, 2.0);
        accumulator.add(3.0, 4.0);

        // Act: Calculate Pearson's correlation coefficient.
        double correlation = accumulator.pearsonsCorrelationCoefficient();

        // Assert: The result should be 1.0, indicating a perfect positive linear correlation.
        assertEquals(1.0, correlation, TOLERANCE);
    }
}