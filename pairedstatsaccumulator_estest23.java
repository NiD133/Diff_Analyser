package com.google.common.math;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test(expected = NullPointerException.class)
    public void addAll_whenGivenNull_throwsNullPointerException() {
        // Arrange
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Act
        accumulator.addAll(null);

        // Assert: NullPointerException is expected, as declared by the @Test annotation.
    }
}