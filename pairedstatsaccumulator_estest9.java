package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void add_afterAddAllWithInvalidStats_updatesStateWithoutException() {
        // This test verifies the behavior of the accumulator when a valid pair is added
        // after the accumulator has been populated with invalid, pre-computed statistics.
        // The original test was auto-generated and lacked assertions, implicitly testing
        // only that no exception was thrown. This version adds explicit assertions to
        // clarify and verify the resulting state.

        // Arrange: Create a PairedStats object with invalid data, including a negative count
        // and NaN values. This simulates a scenario with corrupted input stats.
        Stats invalidStats = new Stats(
                -2859L,         // count (invalid, should be non-negative)
                1736.0,         // mean
                -205.958587102, // sum of squares of deltas (invalid, should be non-negative)
                -205.958587102, // min
                Double.NaN      // max (invalid)
        );
        PairedStats invalidPairedStats = new PairedStats(invalidStats, invalidStats, 69.34955793);
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act 1: Add the invalid, pre-computed stats to the accumulator.
        accumulator.addAll(invalidPairedStats);

        // Assert 1: The accumulator's state should reflect the corrupted data.
        assertEquals("Count should match the invalid count after addAll", -2859L, accumulator.count());

        // Act 2: Add a single, valid pair of values to the accumulator.
        accumulator.add(0.0, -1.0);

        // Assert 2: The accumulator should update its state based on the previous invalid state.
        // The count is incremented, and statistical properties become NaN due to the initial data.
        assertEquals("Count should be incremented after adding a new pair", -2858L, accumulator.count());
        assertTrue(
                "xStats variance should be NaN due to invalid initial state",
                Double.isNaN(accumulator.xStats().populationVariance()));
        assertTrue(
                "yStats variance should be NaN due to invalid initial state",
                Double.isNaN(accumulator.yStats().populationVariance()));
        assertTrue(
                "Population covariance should be NaN due to invalid initial state",
                Double.isNaN(accumulator.populationCovariance()));
    }
}