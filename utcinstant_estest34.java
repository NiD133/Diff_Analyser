package org.threeten.extra.scale;

import java.time.format.DateTimeParseException;
import org.junit.Test;

/**
 * Contains tests for the {@link UtcInstant#parse(CharSequence)} method, focusing on invalid input.
 * This class demonstrates how to write a clear, understandable test for exception cases.
 */
public class UtcInstantParseTest {

    /**
     * Verifies that UtcInstant.parse() throws a DateTimeParseException when given
     * a string that does not conform to the expected ISO-8601 instant format.
     */
    @Test(expected = DateTimeParseException.class)
    public void parse_shouldThrowException_forInvalidFormatString() {
        // Arrange: A string that is clearly not a valid instant.
        // This is more readable than the original test's buffer of null characters.
        String invalidInstantString = "this is not a valid instant";

        // Act: Attempt to parse the invalid string.
        // The @Test(expected=...) annotation handles the assertion,
        // ensuring the test passes only if the expected exception is thrown.
        UtcInstant.parse(invalidInstantString);
    }
}