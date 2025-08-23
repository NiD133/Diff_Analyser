package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the {@link IEEE754rUtils} class, focusing on the max() method.
 */
public class IEEE754rUtilsTest {

    @Test
    public void testMaxFloatShouldReturnLargestValueForMixedSignInputs() {
        // Arrange: Define a set of float values including negative numbers and zero.
        final float negativeValue = -1.0F;
        final float zero = 0.0F;
        final float largeNegativeValue = -2806.0F;
        final float expectedMax = 0.0F;

        // Act: Call the max method with the test values.
        final float actualMax = IEEE754rUtils.max(negativeValue, zero, largeNegativeValue);

        // Assert: Verify that the result is the largest value in the set.
        assertEquals("The maximum of {-1.0F, 0.0F, -2806.0F} should be 0.0F",
                expectedMax, actualMax, 0.0F);
    }
}