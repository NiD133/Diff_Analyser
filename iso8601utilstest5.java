package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils} focusing on parsing invalid date strings.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that parsing a date string with a day-of-month value greater than the maximum
     * possible (e.g., 33) results in a {@link ParseException}.
     */
    @Test
    public void parse_withInvalidDayOfMonth_throwsParseException() {
        // Arrange: A date string with an invalid day (33).
        String invalidDateString = "2022-12-33";
        ParsePosition position = new ParsePosition(0);

        // Act & Assert: Expect a ParseException to be thrown.
        ParseException exception = assertThrows(
                ParseException.class,
                () -> ISO8601Utils.parse(invalidDateString, position)
        );

        // Further assert that the exception message contains the invalid string for better diagnostics.
        assertThat(exception).hasMessageThat().contains(invalidDateString);
    }
}