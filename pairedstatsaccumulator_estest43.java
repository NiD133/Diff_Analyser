package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void yStats_onNewAccumulator_returnsEmptyStats() {
        // Arrange: Create a new, empty accumulator.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act: Get the statistics for the y-values.
        Stats yStats = accumulator.yStats();

        // Assert: Verify the statistics are empty (count is 0, sum is 0.0).
        assertEquals(0, yStats.count());
        assertEquals(0.0, yStats.sum(), 0.0);
    }
}