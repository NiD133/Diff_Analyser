package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test for {@link PairedStatsAccumulator}.
 * This class contains an improved version of a test case that was originally auto-generated.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void pearsonsCorrelationCoefficient_xValuesHaveZeroVariance_throwsIllegalStateException() {
        // Arrange: Create an accumulator and add data points where all x-values are identical.
        // This results in the set of x-values having zero variance.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(5.0, 10.0);
        accumulator.add(5.0, 20.0);

        // Act & Assert: Attempting to calculate the Pearson's correlation coefficient
        // should fail because it is undefined when one of the variables has zero variance.
        try {
            accumulator.pearsonsCorrelationCoefficient();
            fail("Expected an IllegalStateException to be thrown when x-variance is zero.");
        } catch (IllegalStateException expected) {
            assertEquals("x-values must not be all equal", expected.getMessage());
        }
    }
}