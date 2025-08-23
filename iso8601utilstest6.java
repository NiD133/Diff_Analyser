package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    @Test
    public void parse_withInvalidMonth_throwsParseException() {
        // Arrange: A date string with a month (14) outside the valid range of 1-12.
        String invalidDateString = "2022-14-30";

        // Act & Assert: Verify that parsing this string throws a ParseException.
        assertThrows(
            ParseException.class,
            () -> ISO8601Utils.parse(invalidDateString, new ParsePosition(0))
        );
    }
}