package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that nextFloat() throws an IllegalArgumentException if the start value
     * is greater than the end value, which constitutes an invalid range.
     */
    @Test
    public void nextFloatShouldThrowIllegalArgumentExceptionWhenStartIsGreaterThanEnd() {
        // Arrange: Define a range where the start value is clearly greater than the end value.
        final float startValue = 10.0f;
        final float endValue = 5.0f;

        // Act & Assert
        try {
            RandomUtils.nextFloat(startValue, endValue);
            fail("Expected an IllegalArgumentException because the start value is greater than the end value.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            final String expectedMessage = "Start value must be smaller or equal to end value.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}