package org.jsoup.helper;

import org.junit.Test;
import java.util.IllegalFormatFlagsException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that ensureNotNull propagates exceptions from String.format
     * when an invalid format string is provided for the error message.
     * This occurs because the method attempts to format the message with the
     * provided arguments before throwing its own ValidationException.
     */
    @Test
    @SuppressWarnings("deprecation") // The tested method, ensureNotNull, is deprecated.
    public void ensureNotNullWithInvalidFormatMessageThrowsFormattingException() {
        // Arrange: Define the conditions for the test.
        // The object must be null to trigger the error message formatting.
        Object objectToValidate = null;

        // The format specifier "%,-%" is invalid because the ',' (grouping) and '-' (left-justify)
        // flags cannot be applied to the '%' conversion, which simply outputs a literal '%' character.
        String invalidFormatString = "Invalid specifier: %,-%";
        Object[] formatArgs = new Object[0]; // Arguments are not used before the format exception occurs.

        // Act & Assert: Execute the method and verify the outcome.
        try {
            Validate.ensureNotNull(objectToValidate, invalidFormatString, formatArgs);
            fail("Expected an IllegalFormatFlagsException to be thrown due to the invalid format string.");
        } catch (IllegalFormatFlagsException e) {
            // This is the expected outcome.
            // Verify the exception message to confirm the failure reason.
            assertEquals("Flags = '-,'", e.getMessage());
        }
    }
}