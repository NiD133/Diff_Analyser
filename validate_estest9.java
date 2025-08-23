package org.jsoup.helper;

import org.junit.Test;
import java.util.FormatFlagsConversionMismatchException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Validate}.
 */
public class ValidateTest {

    /**
     * Tests that Validate.fail() propagates a FormatFlagsConversionMismatchException
     * when provided with a message string that contains an invalid format specifier.
     * This ensures that underlying formatting errors are not masked.
     */
    @Test
    public void failWithIncompatibleFormatFlagThrowsException() {
        // Arrange: Define an invalid format string and corresponding arguments.
        // The '+' flag is incompatible with the 'b' (boolean) conversion specifier.
        String invalidFormatMessage = "Invalid boolean flag: %+b";
        Object[] args = { true }; // An argument is required to trigger this specific exception.

        try {
            // Act: Call the method under test.
            Validate.fail(invalidFormatMessage, args);

            // Assert: The test should fail if the expected exception is not thrown.
            fail("Expected a FormatFlagsConversionMismatchException to be thrown.");
        } catch (FormatFlagsConversionMismatchException e) {
            // Assert: Verify that the correct exception was caught and that its message
            // confirms the failure occurred for the expected reason.
            assertEquals("Conversion = b, Flags = +", e.getMessage());
        }
    }
}