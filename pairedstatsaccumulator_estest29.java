package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    public void leastSquaresFit_withVerticalData_returnsVerticalTransformation() {
        // The Javadoc for leastSquaresFit() states that if there is variance in the y-values
        // but not in the x-values, the result is a vertical linear transformation.
        // This test verifies that specific behavior.

        // Arrange: Create an accumulator and add data points that form a vertical line,
        // ensuring the x-values have zero variance.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(-1.0, 1.0);
        accumulator.add(-1.0, -1.0);

        // Act: Calculate the least-squares fit for the data.
        LinearTransformation fit = accumulator.leastSquaresFit();

        // Assert: The resulting transformation should be a vertical line at x = -1.0.
        assertTrue("Expected the fit to be a vertical line", fit.isVertical());
        assertEquals(
            "The x-coordinate of the vertical line should match the input data",
            -1.0,
            fit.verticalSlope(),
            TOLERANCE);
    }
}