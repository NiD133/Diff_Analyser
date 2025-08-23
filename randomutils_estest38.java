package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that nextDouble() throws an IllegalArgumentException when the start value
     * is greater than the end value.
     */
    @Test
    public void nextDoubleShouldThrowExceptionForInvalidRange() {
        // Arrange: Define a start value that is larger than the end value.
        final double startInclusive = Double.MAX_VALUE;
        final double endExclusive = 2616.0;
        final String expectedMessage = "Start value must be smaller or equal to end value.";

        // Act & Assert: Call the method and verify that the correct exception is thrown.
        try {
            RandomUtils.nextDouble(startInclusive, endExclusive);
            fail("Expected an IllegalArgumentException to be thrown, but no exception was thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}