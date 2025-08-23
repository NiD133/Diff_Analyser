package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are kept to match the provided context.
// In a real-world scenario, this would be renamed to something like PairedStatsAccumulatorTest.
public class PairedStatsAccumulator_ESTestTest12 extends PairedStatsAccumulator_ESTest_scaffolding {

    /**
     * Tests that addAll correctly updates the accumulator's y-axis statistics,
     * even when the input PairedStats contains malformed data (a negative count).
     */
    @Test
    public void addAll_withPairedStatsHavingNegativeCount_updatesYStatsSum() {
        // Arrange
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // This test uses a Stats object with a negative count. This is an invalid state
        // not achievable through the public API. The test generation tool (EvoSuite)
        // likely used a non-public constructor to create this instance to test edge-case handling.
        // The assumed constructor signature is:
        // Stats(long count, double mean, double sumOfSquaresOfDeltas, double min, double max)
        long count = -481L;
        double mean = 1660997.3216492385;
        Stats statsWithNegativeCount = new Stats(
                count,
                mean,
                1660997.3216492385, // sumOfSquaresOfDeltas
                1660997.3216492385, // min
                -481.0              // max
        );

        // The PairedStats object uses the same malformed Stats for both x and y axes.
        PairedStats pairedStatsWithInvalidData = new PairedStats(
                statsWithNegativeCount,
                statsWithNegativeCount,
                -116.2946 // sumOfProductsOfDeltas
        );

        // Act
        accumulator.addAll(pairedStatsWithInvalidData);
        Stats resultingYStats = accumulator.yStats();

        // Assert
        // The sum of a Stats object is its count multiplied by its mean.
        // We verify that the accumulator correctly calculates this sum for its y-stats.
        double expectedSum = count * mean;
        assertEquals(expectedSum, resultingYStats.sum(), 0.01);
    }
}