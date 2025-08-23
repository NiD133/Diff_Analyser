package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// Note: The original test was auto-generated. This version has been refactored for clarity.
// The class name and inheritance structure are preserved from the original file.
public class IEEE754rUtils_ESTestTest15 extends IEEE754rUtils_ESTest_scaffolding {

    /**
     * Tests that {@link IEEE754rUtils#max(double...)} correctly identifies the largest value
     * in an array that contains one positive number and several zeros.
     */
    @Test
    public void testMaxShouldReturnLargestValueWhenOneElementIsPositive() {
        // Arrange: Define the input array and the expected result.
        // The array is set up to ensure the max value is not at the beginning or end.
        final double expectedMaximum = 1769.7924036104557;
        final double[] numbers = {0.0, expectedMaximum, 0.0, 0.0};

        // Act: Call the method under test.
        final double actualMaximum = IEEE754rUtils.max(numbers);

        // Assert: Verify that the method returned the expected maximum value.
        // A delta of 0.0 is used because no calculation is performed that would introduce a floating-point error.
        assertEquals(expectedMaximum, actualMaximum, 0.0);
    }
}