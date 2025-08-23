package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that max() returns 0.0f for an array containing only zero values.
     */
    @Test
    public void maxFloatArray_shouldReturnZero_whenAllElementsAreZero() {
        // Arrange: Create an array containing only the value 0.0f.
        final float[] numbers = {0.0f, 0.0f, 0.0f};
        final float expectedMax = 0.0f;

        // Act: Call the method under test.
        final float actualMax = IEEE754rUtils.max(numbers);

        // Assert: Verify that the result is the expected maximum value.
        assertEquals(expectedMax, actualMax, 0.0f);
    }
}