package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code randomFloat()} throws an IllegalArgumentException when called with
     * negative range boundaries, as the method requires non-negative inputs.
     */
    @Test
    public void randomFloat_withNegativeBounds_throwsIllegalArgumentException() {
        // Arrange
        final RandomUtils randomUtils = new RandomUtils();
        final float negativeBound = -1.0F; // Any negative value will trigger the validation.

        // Act & Assert
        try {
            randomUtils.randomFloat(negativeBound, negativeBound);
            fail("Expected an IllegalArgumentException because range values cannot be negative.");
        } catch (final IllegalArgumentException e) {
            // Then: Verify the exception message is correct.
            final String expectedMessage = "Both range values must be non-negative.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}