package org.jsoup.helper;

import org.junit.Test;
import java.util.IllegalFormatConversionException;

/**
 * Test suite for the {@link Validate} utility class.
 * This test focuses on the behavior of the fail() method.
 */
public class ValidateTest {

    /**
     * Verifies that Validate.fail() throws an IllegalFormatConversionException
     * when a format string specifier does not match the type of its argument.
     */
    @Test(expected = IllegalFormatConversionException.class)
    public void failShouldThrowExceptionForMismatchedFormatSpecifier() {
        // Arrange: The format specifier "%E" expects a floating-point number (e.g., float, double).
        String formatString = "Scientific notation value: %E";
        Object[] args = { "this is a string, not a number" };

        // Act: Call fail() with a string argument for a numeric format specifier.
        Validate.fail(formatString, args);

        // Assert: The test expects an IllegalFormatConversionException, which is
        // handled by the @Test(expected=...) annotation.
    }
}