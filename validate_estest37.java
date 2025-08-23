package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Tests that {@link Validate#ensureNotNull(Object, String, Object...)} throws an
     * {@link IllegalArgumentException} when the input object is null.
     * It also verifies that the exception message is correctly propagated from the arguments.
     */
    @Test
    public void ensureNotNullThrowsIllegalArgumentExceptionWhenObjectIsNull() {
        // Arrange
        final String expectedMessage = "Object must not be null";

        try {
            // Act
            Validate.ensureNotNull(null, expectedMessage, (Object[]) null);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("The exception message should match the one provided.", expectedMessage, e.getMessage());
        }
    }
}