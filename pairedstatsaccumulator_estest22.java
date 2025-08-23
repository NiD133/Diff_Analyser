package com.google.common.math;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    /**
     * Tests that {@code leastSquaresFit()} throws an {@code IllegalArgumentException} when
     * intermediate calculations for variance overflow to infinity.
     *
     * <p>This overflow causes the slope of the best-fit line to be calculated as NaN
     * (Infinity / Infinity), which is an illegal argument for the underlying linear transformation.
     */
    @Test
    public void leastSquaresFit_whenVarianceOverflows_throwsIllegalArgumentException() {
        // ARRANGE: Create an accumulator and add data points designed to cause an overflow.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();

        // Add two points where one coordinate is extremely large. The variance calculation
        // involves squaring the difference between this large value and the mean, which
        // results in Double.POSITIVE_INFINITY.
        double smallValue = 1.0;
        double largeValue = 1.0e200; // A value large enough that its square overflows
        accumulator.add(smallValue, smallValue);
        accumulator.add(largeValue, largeValue);

        // ACT & ASSERT: Verify that the method throws the expected exception.
        try {
            accumulator.leastSquaresFit();
            fail("Expected IllegalArgumentException for non-finite slope due to overflow");
        } catch (IllegalArgumentException expected) {
            // This is the expected behavior. The internal slope calculation results in NaN,
            // which is not a valid argument for the resulting LinearTransformation.
        }
    }
}