package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Test suite for ISO8601Utils class.
 * Tests parsing and formatting of ISO 8601 date strings.
 */
public class ISO8601UtilsTest {

    // ========== Successful Parsing Tests ==========
    
    @Test
    public void shouldParseValidISO8601DateWithMillisecondsAndZuluTime() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        Date parsedDate = ISO8601Utils.parse("1969-12-31T23:59:59.999Z", position);
        
        assertNotNull("Parsed date should not be null", parsedDate);
        assertEquals("Fri Feb 14 20:21:21 GMT 2014", parsedDate.toString());
    }

    @Test
    public void shouldParseValidISO8601DateWithNegativeTimezone() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        Date parsedDate = ISO8601Utils.parse("2014-02-14T20:21:21-00:00", position);
        
        assertNotNull("Parsed date should not be null", parsedDate);
        assertEquals("Fri Feb 14 20:21:21 GMT 2014", parsedDate.toString());
    }

    @Test
    public void shouldParseValidISO8601DateWithPositiveTimezone() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        Date parsedDate = ISO8601Utils.parse("2014-02-14T20:21:22.575+00:00", position);
        
        assertNotNull("Parsed date should not be null", parsedDate);
        assertEquals("Fri Feb 14 20:21:21 GMT 2014", parsedDate.toString());
    }

    @Test
    public void shouldParseValidISO8601DateWithZuluTimezone() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        Date parsedDate = ISO8601Utils.parse("2014-02-14T20:21:21Z", position);
        
        assertNotNull("Parsed date should not be null", parsedDate);
        assertEquals("Fri Feb 14 20:21:21 GMT 2014", parsedDate.toString());
    }

    @Test
    public void shouldParseValidISO8601DateWithMilliseconds() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        Date parsedDate = ISO8601Utils.parse("2014-02-14T20:21:21.320Z", position);
        
        assertNotNull("Parsed date should not be null", parsedDate);
        assertEquals("Fri Feb 14 20:21:21 GMT 2014", parsedDate.toString());
    }

    // ========== Parsing Error Tests ==========

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForInvalidDateString() throws Throwable {
        ParsePosition position = new ParsePosition(1);
        
        ISO8601Utils.parse("p;72<&YyPko{%", position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForGarbageInput() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        ISO8601Utils.parse("9>({Cf/Td,\";U)Ji*", position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForShortInvalidString() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        ISO8601Utils.parse("&R[&", position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionWhenParsePositionBeyondStringLength() throws Throwable {
        ParsePosition position = new ParsePosition(2147483645);
        
        ISO8601Utils.parse("+0000", position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForInvalidParsePosition() throws Throwable {
        ParsePosition position = new ParsePosition(1);
        
        ISO8601Utils.parse("+0000", position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForEmptyString() throws Throwable {
        ParsePosition position = new ParsePosition(-844);
        
        ISO8601Utils.parse("", position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForNullString() throws Throwable {
        ParsePosition position = new ParsePosition(-2016);
        
        ISO8601Utils.parse(null, position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForInvalidTimezoneIndicator() throws Throwable {
        ParsePosition position = new ParsePosition(0);
        
        ISO8601Utils.parse("190690348-12-04T21:42:26Z", position);
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseExceptionForMismatchingTimezone() throws Throwable {
        ParsePosition position = new ParsePosition(3);
        
        ISO8601Utils.parse("208737754-05-17T20:36:25Z", position);
    }

    // ========== Null Parameter Tests ==========

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenParsePositionIsNull() throws Throwable {
        ISO8601Utils.parse("H0tU&_9';1S)", null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenTimezoneIsNull() throws Throwable {
        Date testDate = new Date();
        
        ISO8601Utils.format(testDate, false, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenDateIsNullInFormatWithMillis() throws Throwable {
        ISO8601Utils.format(null, true);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenDateIsNullInBasicFormat() throws Throwable {
        ISO8601Utils.format(null);
    }

    // ========== Successful Formatting Tests ==========

    @Test
    public void shouldFormatDateWithExtremeValues() throws Throwable {
        Date extremeDate = new Date(-2147483646, -2147483646, -2147483646, -2147483646, -2147483646);
        
        String formattedDate = ISO8601Utils.format(extremeDate);
        
        assertEquals("208737754-05-17T20:36:25Z", formattedDate);
    }

    @Test
    public void shouldFormatEpochDateWithDefaultTimezone() throws Throwable {
        Date epochDate = new Date(0L);
        TimeZone customTimezone = TimeZone.getTimeZone("MU/^fUMQ");
        
        String formattedDate = ISO8601Utils.format(epochDate, false, customTimezone);
        
        assertEquals("1970-01-01T00:00:00Z", formattedDate);
    }

    @Test
    public void shouldFormatDateWithCustomTimezone() throws Throwable {
        Date testDate = new Date(0);
        SimpleTimeZone customTimezone = new SimpleTimeZone(1255, "DWGL1k");
        
        String formattedDate = ISO8601Utils.format(testDate, false, customTimezone);
        
        assertEquals("1970-01-01T00:00:01+00:00", formattedDate);
    }

    @Test
    public void shouldFormatDateWithNegativeTimezoneOffset() throws Throwable {
        Date testDate = new Date(1194, 1194, 2550);
        SimpleTimeZone negativeTimezone = new SimpleTimeZone(2550, "!#[5.iqQz1B2Sn'");
        negativeTimezone.setRawOffset(-141);
        
        String formattedDate = ISO8601Utils.format(testDate, true, negativeTimezone);
        
        assertEquals("3200-06-22T23:59:59.859-00:00", formattedDate);
    }

    @Test
    public void shouldFormatCurrentDateWithMilliseconds() throws Throwable {
        Date currentDate = new Date();
        
        String formattedDate = ISO8601Utils.format(currentDate, true);
        
        assertEquals("2014-02-14T20:21:21.320Z", formattedDate);
    }
}