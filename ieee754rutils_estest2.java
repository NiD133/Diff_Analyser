package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    @Test
    public void minOfFloatArrayShouldReturnSmallestValue() {
        // Arrange: Create an array of floats with a clear minimum value.
        final float[] numbers = {825.0F, 1.0F, 825.0F, 1.0F};
        final float expectedMin = 1.0F;

        // Act: Call the method under test to find the minimum value.
        final float actualMin = IEEE754rUtils.min(numbers);

        // Assert: Verify that the returned value is the expected minimum.
        assertEquals("The minimum value in the array should be correctly identified", expectedMin, actualMin, 0.0f);
    }
}