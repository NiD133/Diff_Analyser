package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    private static final double TOLERANCE = 0.01;

    @Test
    public void xStats_sum_afterAddingSinglePair_isCorrect() {
        // Arrange
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        double xValue = 863464.0975884801;
        double yValue = 38.358751;

        // Act
        accumulator.add(xValue, yValue);
        Stats xStats = accumulator.xStats();

        // Assert
        assertEquals(
                "The sum of x-values should be the single x-value that was added.",
                xValue,
                xStats.sum(),
                TOLERANCE);
    }
}