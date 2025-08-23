package com.google.common.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void count_afterAddingOnePair_isOne() {
        // Arrange: Create a new accumulator instance.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act: Add a single pair of values to the accumulator.
        accumulator.add(10.0, 20.0);
        long count = accumulator.count();

        // Assert: Verify that the count is 1.
        assertEquals(1L, count);
    }
}