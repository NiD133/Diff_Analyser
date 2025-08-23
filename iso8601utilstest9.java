package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}, focusing on parsing invalid date-time strings.
 */
public class ISO8601UtilsTest {

    @Test
    public void parse_withInvalidTime_throwsParseException() {
        // This string contains time components that are out of their valid ranges:
        // Hour: 61 (valid: 0-23)
        // Minute: 60 (valid: 0-59)
        // Second: 62 (valid: 0-59)
        String invalidIsoString = "2018-06-25T61:60:62-03:00";

        // The position from which parsing should start.
        ParsePosition position = new ParsePosition(0);

        // Verify that parsing this string throws a ParseException.
        assertThrows(
                "Parsing a string with out-of-range time components should fail",
                ParseException.class,
                () -> ISO8601Utils.parse(invalidIsoString, position));
    }
}