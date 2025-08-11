package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    // A helper method to create a UTC date for consistent testing.
    private Date createUtcDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar cal = new GregorianCalendar(UTC);
        cal.set(year, month - 1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, millisecond);
        return cal.getTime();
    }

    // =================================================================================================
    // Formatting Tests (Date -> String)
    // =================================================================================================

    @Test
    public void format_withNullDate_shouldThrowNullPointerException() {
        try {
            ISO8601Utils.format(null);
            fail("NullPointerException was expected for null date.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void format_epochDateWithUtc_shouldReturnUtcString() {
        // Arrange
        Date epoch = new Date(0L);
        String expected = "1970-01-01T00:00:00Z";

        // Act
        String formattedDate = ISO8601Utils.format(epoch, false, UTC);

        // Assert
        assertEquals(expected, formattedDate);
    }

    @Test
    public void format_specificDateWithMilliseconds_shouldReturnUtcStringWithMillis() {
        // Arrange
        Date date = createUtcDate(2014, 2, 14, 20, 21, 21, 320);
        String expected = "2014-02-14T20:21:21.320Z";

        // Act
        // The format(Date, boolean) overload defaults to the UTC time zone.
        String formattedDate = ISO8601Utils.format(date, true);

        // Assert
        assertEquals(expected, formattedDate);
    }

    @Test
    public void format_epochDateWithOffsetTimeZone_shouldReturnStringWithOffset() {
        // Arrange
        Date epoch = new Date(0L);
        // Time zone with a small positive offset of 1255 milliseconds.
        // The formatter should round this down to the nearest minute for the offset string.
        TimeZone smallOffsetTz = new SimpleTimeZone(1255, "TestZone");
        String expected = "1970-01-01T00:00:01+00:00";

        // Act
        String formattedDate = ISO8601Utils.format(epoch, false, smallOffsetTz);

        // Assert
        assertEquals(expected, formattedDate);
    }

    @Test
    public void format_dateWithRolloverAndNegativeOffset_shouldHandleNormalization() {
        // Arrange
        // Using the deprecated Date constructor with large values to test calendar rollover.
        // The values will be normalized by the Calendar instance used internally.
        @SuppressWarnings("deprecation")
        Date date = new Date(1194, 1194, 2550);
        // Time zone with a small negative offset of -141 milliseconds.
        TimeZone smallNegativeOffsetTz = new SimpleTimeZone(-141, "TestZone");
        String expected = "3200-06-22T23:59:59.859-00:00";

        // Act
        String formattedDate = ISO8601Utils.format(date, true, smallNegativeOffsetTz);

        // Assert
        assertEquals(expected, formattedDate);
    }

    // =================================================================================================
    // Parsing Tests (String -> Date)
    // =================================================================================================

    @Test
    public void parse_withUtcZuluDesignator_shouldSucceed() throws ParseException {
        // Arrange
        String dateString = "2014-02-14T20:21:21Z";
        Date expectedDate = createUtcDate(2014, 2, 14, 20, 21, 21, 0);

        // Act
        Date result = ISO8601Utils.parse(dateString, new ParsePosition(0));

        // Assert
        assertNotNull(result);
        assertEquals(expectedDate.getTime(), result.getTime());
    }

    @Test
    public void parse_withUtcZuluDesignatorAndMillis_shouldSucceed() throws ParseException {
        // Arrange
        String dateString = "1969-12-31T23:59:59.999Z"; // One millisecond before epoch
        Date expectedDate = new Date(-1L);

        // Act
        Date result = ISO8601Utils.parse(dateString, new ParsePosition(0));

        // Assert
        assertNotNull(result);
        assertEquals(expectedDate.getTime(), result.getTime());
    }

    @Test
    public void parse_withTimezoneOffset_shouldSucceed() throws ParseException {
        // Arrange
        String dateString = "2014-02-14T15:21:21-05:00"; // 20:21:21 UTC
        Date expectedDate = createUtcDate(2014, 2, 14, 20, 21, 21, 0);

        // Act
        Date result = ISO8601Utils.parse(dateString, new ParsePosition(0));

        // Assert
        assertNotNull(result);
        assertEquals(expectedDate.getTime(), result.getTime());
    }

    @Test
    public void parse_withPositiveTimezoneOffsetAndMillis_shouldSucceed() throws ParseException {
        // Arrange
        String dateString = "2014-02-14T20:21:22.575+00:00";
        Date expectedDate = createUtcDate(2014, 2, 14, 20, 21, 22, 575);

        // Act
        Date result = ISO8601Utils.parse(dateString, new ParsePosition(0));

        // Assert
        assertNotNull(result);
        assertEquals(expectedDate.getTime(), result.getTime());
    }

    // =================================================================================================
    // Parsing Failure Tests
    // =================================================================================================

    @Test(expected = NullPointerException.class)
    public void parse_withNullPosition_shouldThrowNullPointerException() throws ParseException {
        // Act
        ISO8601Utils.parse("some-date", null);
    }

    @Test
    public void parse_withNullString_shouldThrowParseException() {
        // Arrange
        ParsePosition position = new ParsePosition(0);

        // Act & Assert
        try {
            ISO8601Utils.parse(null, position);
            fail("ParseException was expected for null input string.");
        } catch (ParseException e) {
            // The internal NumberFormatException for a null string is wrapped in a ParseException.
            assertTrue(e.getMessage().contains("(java.lang.NumberFormatException)"));
        }
    }

    @Test
    public void parse_invalidNumberInDateString_shouldThrowParseException() {
        // Arrange
        String dateString = "not-a-date";
        ParsePosition position = new ParsePosition(0);

        // Act & Assert
        try {
            ISO8601Utils.parse(dateString, position);
            fail("ParseException was expected for invalid date format.");
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"not-a-date\"]: Invalid number: not-", e.getMessage());
        }
    }

    @Test
    public void parse_dateStringWithLongYear_shouldThrowParseException() {
        // Arrange
        // The parser seems to expect a 4-digit year and gets confused.
        String dateStringWithLongYear = "190690348-12-04T21:42:26Z";
        ParsePosition position = new ParsePosition(0);

        // Act & Assert
        try {
            ISO8601Utils.parse(dateStringWithLongYear, position);
            fail("ParseException was expected");
        } catch (ParseException e) {
            assertTrue("Exception message should indicate a parsing failure",
                e.getMessage().startsWith("Failed to parse date"));
            assertTrue("Exception message should indicate the specific parsing error",
                e.getMessage().endsWith("Invalid time zone indicator '8'"));
        }
    }

    @Test
    public void parse_mismatchedTimezone_shouldThrowParseException() {
        // Arrange
        // This is an unusual case where parsing starts mid-string, leading to a timezone mismatch error.
        String dateString = "208737754-05-17T20:36:25Z";
        ParsePosition position = new ParsePosition(3); // Start parsing at "7377..."

        // Act & Assert
        try {
            ISO8601Utils.parse(dateString, position);
            fail("ParseException was expected");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Mismatching time zone indicator"));
        }
    }
}