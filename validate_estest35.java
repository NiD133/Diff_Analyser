package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Validate}.
 * This test focuses on the isTrue() method.
 */
public class ValidateTest {

    /**
     * Verifies that isTrue() throws an IllegalArgumentException when the condition is false.
     * The exception message should clearly state the validation failure.
     */
    @Test
    public void isTrue_shouldThrowIllegalArgumentException_whenConditionIsFalse() {
        // Arrange
        final String expectedMessage = "Must be true";

        // Act & Assert
        try {
            Validate.isTrue(false);
            fail("Expected an IllegalArgumentException to be thrown, but nothing was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}