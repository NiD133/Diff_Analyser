package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test case for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    @Test
    public void maxOfFloatArrayShouldReturnLargestValue() {
        // Arrange: Create an array of floats with a clear maximum value.
        // The array includes positive, negative, and zero values to ensure robustness.
        final float[] values = new float[]{-10.0f, 0.0f, 50.5f, 981.74023f, -200.1f};
        final float expectedMaximum = 981.74023f;

        // Act: Call the method under test.
        final float actualMaximum = IEEE754rUtils.max(values);

        // Assert: Verify that the returned value is the expected maximum.
        // The delta is set to 0.0f because we expect the exact value to be returned.
        assertEquals(expectedMaximum, actualMaximum, 0.0f);
    }
}