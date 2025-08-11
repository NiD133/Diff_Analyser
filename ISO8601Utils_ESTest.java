package com.google.gson.internal.bind.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    private TimeZone originalDefaultTz;

    private static TimeZone utc() {
        return TimeZone.getTimeZone("UTC");
    }

    @Before
    public void setUp() {
        originalDefaultTz = TimeZone.getDefault();
        TimeZone.setDefault(utc()); // Make default timezone deterministic for format(Date)
    }

    @After
    public void tearDown() {
        TimeZone.setDefault(originalDefaultTz);
    }

    // Formatting

    @Test
    public void format_defaultTimezone_noMillis_usesZWhenUTC() {
        Date epoch = new Date(0L);
        String formatted = ISO8601Utils.format(epoch);
        assertEquals("1970-01-01T00:00:00Z", formatted);
    }

    @Test
    public void format_defaultTimezone_withMillis() {
        Date epoch = new Date(0L);
        String formatted = ISO8601Utils.format(epoch, true);
        assertEquals("1970-01-01T00:00:00.000Z", formatted);
    }

    @Test
    public void format_specificTimezone_utcProducesZ() {
        Date someDate = new Date(1_600_000_000_000L); // 2020-09-13T12:26:40Z
        String formatted = ISO8601Utils.format(someDate, true, utc());
        assertEquals("2020-09-13T12:26:40.000Z", formatted);
    }

    @Test
    public void format_specificTimezone_positiveOffsetIsRendered() {
        Date epoch = new Date(0L);
        TimeZone plus0230 = TimeZone.getTimeZone("GMT+02:30");
        String formatted = ISO8601Utils.format(epoch, false, plus0230);
        assertEquals("1970-01-01T02:30:00+02:30", formatted);
    }

    @Test
    public void format_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(null));
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(null, true));
    }

    @Test
    public void format_nullTimezone_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(new Date(), false, null));
    }

    // Parsing

    @Test
    public void parse_zuluTime_updatesPositionAndReturnsCorrectInstant() throws Exception {
        String input = "1970-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);

        Date parsed = ISO8601Utils.parse(input, pos);

        assertEquals(0L, parsed.getTime());
        assertEquals(input.length(), pos.getIndex());
        assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void parse_fractionalSecondsAndOffset_roundTripsToUTC() throws Exception {
        String input = "2020-03-01T12:34:56.789+02:30";
        ParsePosition pos = new ParsePosition(0);

        Date parsed = ISO8601Utils.parse(input, pos);

        // 12:34:56.789 at +02:30 equals 10:04:56.789 UTC
        String reformattedUtc = ISO8601Utils.format(parsed, true, utc());
        assertEquals("2020-03-01T10:04:56.789Z", reformattedUtc);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void parse_minusZeroOffset_treatedAsUTC() throws Exception {
        String input = "2014-02-14T20:21:21-00:00";
        ParsePosition pos = new ParsePosition(0);

        Date parsed = ISO8601Utils.parse(input, pos);

        String reformattedUtc = ISO8601Utils.format(parsed, false, utc());
        assertEquals("2014-02-14T20:21:21Z", reformattedUtc);
    }

    @Test
    public void parse_justBeforeEpoch() throws Exception {
        String input = "1969-12-31T23:59:59.999Z";
        ParsePosition pos = new ParsePosition(0);

        Date parsed = ISO8601Utils.parse(input, pos);

        assertEquals(-1L, parsed.getTime());
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void parse_basicFormatWithoutSeparators_roundTrips() throws Exception {
        // Supported: yyyyMMdd'T'hhmmss'Z'
        String basic = "20230102T030405Z";
        ParsePosition pos = new ParsePosition(0);

        Date parsed = ISO8601Utils.parse(basic, pos);

        String canonical = ISO8601Utils.format(parsed, false, utc());
        assertEquals("2023-01-02T03:04:05Z", canonical);
        assertEquals(basic.length(), pos.getIndex());
    }

    @Test
    public void parse_invalidText_throwsParseException() {
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("not-a-date", pos));
    }

    @Test
    public void parse_invalidTimezoneIndicator_throwsParseException() {
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2014-02-14T20:21:21.000X", pos));
    }

    @Test
    public void parse_nullInput_throwsParseException() {
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(null, pos));
    }

    @Test
    public void parse_nullParsePosition_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ISO8601Utils.parse("2014-02-14T20:21:21Z", null));
    }
}