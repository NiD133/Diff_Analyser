package org.jsoup.helper;

import org.junit.Test;
import java.util.IllegalFormatWidthException;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Verifies that the Validate.fail() method propagates exceptions from the underlying
     * String.format() call when provided with an invalid format string.
     */
    @Test
    public void failWithInvalidFormatWidthThrowsException() {
        // GIVEN a format string with an invalid width specifier.
        // The %n specifier (for a platform-specific newline) does not support a width argument.
        // Therefore, the "%9n" part of this string is invalid.
        String invalidFormatString = "wz%c^4e%9no&N8";
        Object[] formatArgs = new Object[5]; // Arguments are irrelevant as formatting fails before they are used.

        // WHEN Validate.fail is called with the invalid format string
        // THEN it should throw an IllegalFormatWidthException, as thrown by String.format.
        assertThrows(
            "Validate.fail should propagate the underlying formatting exception",
            IllegalFormatWidthException.class,
            () -> Validate.fail(invalidFormatString, formatArgs)
        );
    }
}