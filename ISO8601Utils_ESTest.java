package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ISO8601Utils_ESTest extends ISO8601Utils_ESTest_scaffolding {

    private static final String VALID_DATE_STRING = "2014-02-14T20:21:21Z";
    private static final String EXPECTED_DATE_STRING = "Fri Feb 14 20:21:21 GMT 2014";

    @Test(timeout = 4000)
    public void testParseValidDateString() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = ISO8601Utils.parse(VALID_DATE_STRING, parsePosition);
        assertEquals(EXPECTED_DATE_STRING, date.toString());
    }

    @Test(timeout = 4000)
    public void testFormatDateWithMockDate() throws Throwable {
        MockDate mockDate = new MockDate(-2147483646, -2147483646, -2147483646, -2147483646, -2147483646);
        String formattedDate = ISO8601Utils.format(mockDate);
        assertEquals("208737754-05-17T20:36:25Z", formattedDate);
    }

    @Test(timeout = 4000)
    public void testParseInvalidDateStringThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(1);
        try {
            ISO8601Utils.parse("p;72<&YyPko{%", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithNullParsePositionThrowsNullPointerException() throws Throwable {
        try {
            ISO8601Utils.parse("H0tU&_9';1S)", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testFormatWithNullTimeZoneThrowsNullPointerException() throws Throwable {
        MockDate mockDate = new MockDate();
        try {
            ISO8601Utils.format(mockDate, false, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFormatWithNullDateThrowsNullPointerException() throws Throwable {
        try {
            ISO8601Utils.format((Date) null, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Calendar", e);
        }
    }

    @Test(timeout = 4000)
    public void testFormatWithNullDateSimpleThrowsNullPointerException() throws Throwable {
        try {
            ISO8601Utils.format((Date) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Calendar", e);
        }
    }

    @Test(timeout = 4000)
    public void testFormatDateWithTimeZone() throws Throwable {
        MockDate mockDate = new MockDate(0L);
        TimeZone timeZone = TimeZone.getTimeZone("MU/^fUMQ");
        String formattedDate = ISO8601Utils.format(mockDate, false, timeZone);
        assertEquals("1970-01-01T00:00:00Z", formattedDate);
    }

    @Test(timeout = 4000)
    public void testFormatDateWithSimpleTimeZone() throws Throwable {
        MockDate mockDate = new MockDate(0);
        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(1255, "DWGL1k");
        String formattedDate = ISO8601Utils.format(mockDate, false, simpleTimeZone);
        assertEquals("1970-01-01T00:00:01+00:00", formattedDate);
    }

    @Test(timeout = 4000)
    public void testParseInvalidFormatThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            ISO8601Utils.parse("9>({Cf/Td,\";U)Ji*", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseInvalidNumberThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            ISO8601Utils.parse("&R[&", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithLargeOffsetThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(2147483645);
        try {
            ISO8601Utils.parse("+0000", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithOffsetOneThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(1);
        try {
            ISO8601Utils.parse("+0000", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseEmptyStringThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(-844);
        try {
            ISO8601Utils.parse("", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullStringThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(-2016);
        try {
            ISO8601Utils.parse(null, parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseDateWithOffset() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = ISO8601Utils.parse("2014-02-14T20:21:21-00:00", parsePosition);
        assertEquals(EXPECTED_DATE_STRING, date.toString());
    }

    @Test(timeout = 4000)
    public void testParseDateWithMilliseconds() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = ISO8601Utils.parse("2014-02-14T20:21:22.575+00:00", parsePosition);
        assertEquals(EXPECTED_DATE_STRING, date.toString());
    }

    @Test(timeout = 4000)
    public void testParseDateWithZTimezone() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = ISO8601Utils.parse(VALID_DATE_STRING, parsePosition);
        assertEquals(EXPECTED_DATE_STRING, date.toString());
    }

    @Test(timeout = 4000)
    public void testParseInvalidTimeZoneIndicatorThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            ISO8601Utils.parse("190690348-12-04T21:42:26Z", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseDateWithMillisecondsPrecision() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = ISO8601Utils.parse("2014-02-14T20:21:21.320Z", parsePosition);
        assertEquals(EXPECTED_DATE_STRING, date.toString());
    }

    @Test(timeout = 4000)
    public void testParseMismatchingTimeZoneThrowsParseException() throws Throwable {
        ParsePosition parsePosition = new ParsePosition(3);
        try {
            ISO8601Utils.parse("208737754-05-17T20:36:25Z", parsePosition);
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.gson.internal.bind.util.ISO8601Utils", e);
        }
    }

    @Test(timeout = 4000)
    public void testFormatDateWithCustomTimeZone() throws Throwable {
        MockDate mockDate = new MockDate(1194, 1194, 2550);
        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(2550, "!#[5.iqQz1B2Sn'");
        simpleTimeZone.setRawOffset(-141);
        String formattedDate = ISO8601Utils.format(mockDate, true, simpleTimeZone);
        assertEquals("3200-06-22T23:59:59.859-00:00", formattedDate);
    }

    @Test(timeout = 4000)
    public void testFormatCurrentDateWithMilliseconds() throws Throwable {
        MockDate mockDate = new MockDate();
        String formattedDate = ISO8601Utils.format(mockDate, true);
        assertEquals("2014-02-14T20:21:21.320Z", formattedDate);
    }
}