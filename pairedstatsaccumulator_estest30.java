package com.google.common.math;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void leastSquaresFit_withNaNValue_returnsNaNTransformation() {
        // Arrange
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(1018.9124, 1.0);
        accumulator.add(1.0, Double.NaN); // Add a pair containing a non-finite value

        // Act
        LinearTransformation fit = accumulator.leastSquaresFit();

        // Assert
        // Per the documentation, if the dataset contains any non-finite values,
        // the result of leastSquaresFit() should be a NaN transformation.
        assertTrue(
                "Expected a NaN transformation when a NaN value is present in the data",
                fit.isNan());
    }
}