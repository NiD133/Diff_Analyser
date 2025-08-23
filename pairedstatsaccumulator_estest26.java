package com.google.common.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Verifies that a newly instantiated accumulator has a count of zero.
     */
    @Test
    public void count_onNewAccumulator_returnsZero() {
        // Arrange: Create a new, empty accumulator.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act: Get the count of data points.
        long count = accumulator.count();

        // Assert: The count should be 0.
        assertEquals(0L, count);
    }
}