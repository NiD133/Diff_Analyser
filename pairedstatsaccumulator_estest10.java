package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    public void yStats_afterAddingSinglePair_returnsCorrectSum() {
        // Arrange
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        double xValue = 10.0;
        double yValue = 20.0;

        // Act
        accumulator.add(xValue, yValue);
        Stats yStats = accumulator.yStats();

        // Assert
        assertEquals("The sum of y-values should be the single y-value added.", yValue, yStats.sum(), TOLERANCE);
    }
}