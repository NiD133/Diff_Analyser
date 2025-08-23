package org.jsoup.helper;

import org.junit.Test;
import java.util.MissingFormatWidthException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Validate} helper class, focusing on exception-throwing methods.
 */
public class ValidateTest {

    /**
     * Verifies that Validate.fail() propagates a MissingFormatWidthException when the
     * format string is malformed in a way that violates java.util.Formatter rules.
     */
    @Test
    public void fail_whenFormatStringIsMissingRequiredWidth_throwsMissingFormatWidthException() {
        // Arrange: A format string that is invalid according to java.util.Formatter.
        // The specifier "%-." is malformed because specifying a precision ('.')
        // requires a width to be defined beforehand, but it is absent.
        final String malformedFormatString = "Invalid specifier: %-.f";
        final Object[] args = { 3.14 };

        // Act & Assert: The call to Validate.fail() should fail during string formatting
        // and propagate the underlying exception.
        try {
            Validate.fail(malformedFormatString, args);
            fail("Expected a MissingFormatWidthException to be thrown, but no exception occurred.");
        } catch (MissingFormatWidthException e) {
            // This is the expected outcome.
            // We can further assert that the exception message correctly identifies the
            // faulty specifier, confirming the exception was thrown for the right reason.
            assertEquals("The exception message should pinpoint the invalid format specifier.", "%-.", e.getMessage());
        }
    }
}