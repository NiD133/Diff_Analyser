package com.google.common.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void addAll_withEmptyStats_onEmptyAccumulator_remainsEmpty() {
        // Arrange: Create an empty accumulator and an empty PairedStats object.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        // A snapshot of a new accumulator is a convenient way to get an empty PairedStats instance.
        PairedStats emptyStats = new PairedStatsAccumulator().snapshot();

        // Act: Add the empty stats to the empty accumulator.
        accumulator.addAll(emptyStats);

        // Assert: The accumulator should still be empty.
        assertEquals("Count should remain zero after adding empty stats.", 0L, accumulator.count());
    }
}