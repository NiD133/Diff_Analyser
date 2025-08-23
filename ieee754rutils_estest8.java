package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.min(float, float) correctly returns the value
     * when both inputs are identical.
     */
    @Test
    public void testMinWithEqualFloats() {
        // Arrange: Define the input values for the test.
        final float value = -513.9F;
        final float expected = -513.9F;

        // Act: Call the method under test.
        final float actual = IEEE754rUtils.min(value, value);

        // Assert: Verify the result is as expected.
        // The minimum of two identical numbers should be the number itself.
        assertEquals(expected, actual, 0.0f);
    }
}