package com.google.common.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void pearsonsCorrelationCoefficient_withInfiniteValues_returnsNaN() {
        // Arrange: Create an accumulator and add pairs containing non-finite values.
        // The Javadoc for pearsonsCorrelationCoefficient() states that if the dataset
        // contains any non-finite values, the result should be NaN.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        accumulator.add(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        // Act: Calculate Pearson's correlation coefficient.
        double correlation = accumulator.pearsonsCorrelationCoefficient();

        // Assert: The result must be NaN.
        assertTrue(
            "Pearson's correlation coefficient should be NaN when the dataset contains non-finite values.",
            Double.isNaN(correlation));
    }
}