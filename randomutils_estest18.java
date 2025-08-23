package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests that randomInt() throws an IllegalArgumentException when the start value
     * is greater than the end value, which is an invalid range.
     */
    @Test
    public void randomIntShouldThrowIllegalArgumentExceptionWhenStartIsGreaterThanEnd() {
        // Arrange
        final RandomUtils randomUtils = RandomUtils.secureStrong();
        final int startInclusive = 1;
        final int endExclusive = 0; // Create an invalid range where start > end

        // Act & Assert
        try {
            randomUtils.randomInt(startInclusive, endExclusive);
            fail("Expected an IllegalArgumentException to be thrown for an invalid range.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception and its message are correct.
            final String expectedMessage = "Start value must be smaller or equal to end value.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}