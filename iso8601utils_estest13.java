package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that parsing an incomplete date string, which is too short to be a valid
     * ISO 8601 date, correctly throws a ParseException.
     */
    @Test
    public void parse_withIncompleteDateString_throwsParseException() {
        // The full string is "+0000", but we start parsing from index 1.
        String dateString = "+0000";
        ParsePosition parsePosition = new ParsePosition(1);

        try {
            // The effective string being parsed is "0000", which is an invalid format.
            ISO8601Utils.parse(dateString, parsePosition);
            fail("Expected a ParseException for an incomplete date string, but none was thrown.");
        } catch (ParseException e) {
            // The parser expects a format like "yyyy-MM-dd" or "yyyyMMdd".
            // The string "0000" is too short, so parsing should fail.
            String expectedMessage = "Failed to parse date [\"+0000\"]: +0000";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}