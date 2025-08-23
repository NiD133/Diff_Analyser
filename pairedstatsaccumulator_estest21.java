package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void addAll_withPairedStatsHavingNegativeCount_updatesAccumulatorCount() {
        // This test case, derived from automatically generated code, explores an edge case:
        // adding a PairedStats object that was constructed with a negative count.
        // While negative counts are not expected in typical usage, this test verifies
        // that the accumulator's internal count correctly reflects the data from the
        // added PairedStats object.

        // Arrange
        final long negativeCount = -1418L;
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Create a Stats object with a negative count. The other statistical values (mean, etc.)
        // are not relevant to this test's purpose and are set to simple defaults.
        Stats xStats = new Stats(negativeCount, 0.0, 0.0, 0.0, 0.0);
        Stats yStats = new Stats(negativeCount, 0.0, 0.0, 0.0, 0.0);
        PairedStats statsToAdd = new PairedStats(xStats, yStats, 0.0 /* population covariance */);

        // Act
        accumulator.addAll(statsToAdd);
        long actualCount = accumulator.count();

        // Assert
        assertEquals(
                "The accumulator's count should match the negative count from the added PairedStats.",
                negativeCount,
                actualCount);
    }
}