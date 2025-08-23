package com.google.common.math;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void leastSquaresFit_throwsIllegalStateException_whenBothXAndYHaveZeroVariance() {
        // Arrange: Create an accumulator and add two identical points.
        // According to the Javadoc, leastSquaresFit() requires at least one of the
        // x or y datasets to have a non-zero population variance. Adding identical
        // points ensures both have zero variance.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(10.0, 20.0);
        accumulator.add(10.0, 20.0);

        // Act & Assert: Verify that calling leastSquaresFit() throws an IllegalStateException.
        assertThrows(IllegalStateException.class, accumulator::leastSquaresFit);
    }
}