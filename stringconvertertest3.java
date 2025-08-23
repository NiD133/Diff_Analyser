package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link StringConverter} focusing on getInstantMillis.
 * This suite tests the conversion of various ISO 8601 string formats to milliseconds.
 */
public class StringConverterTest {

    private static final DateTimeZone ZONE_PLUS_8 = DateTimeZone.forOffsetHours(8);
    private static final Chronology ISO_CHRONOLOGY_PLUS_8 = ISOChronology.getInstance(ZONE_PLUS_8);
    private static final Chronology ISO_CHRONOLOGY_DEFAULT_ZONE = ISOChronology.getInstance();

    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // Isolate tests from system defaults
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore system defaults
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    public void getInstantMillis_parsesFullDateTimeWithMillis() {
        String input = "2004-06-09T12:24:48.501+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesYearOnlyString() {
        String input = "2004T+08:00";
        DateTime expected = new DateTime(2004, 1, 1, 0, 0, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesYearMonthString() {
        String input = "2004-06T+08:00";
        DateTime expected = new DateTime(2004, 6, 1, 0, 0, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesDateOnlyString() {
        String input = "2004-06-09T+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 0, 0, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesOrdinalDateString() {
        // An ordinal date represents the year and the day of the year (1-366).
        // The 161st day of 2004 is June 9th.
        String input = "2004-161T+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 0, 0, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesWeekDateString() {
        // A week date represents the year, the week of the year, and the day of the week (1-7).
        // 2004-W24-3 is the 3rd day (Wednesday) of the 24th week of 2004, which is June 9th.
        String input = "2004-W24-3T+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 0, 0, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesYearAndWeekString() {
        // If the day of the week is omitted, it defaults to the first day (Monday).
        // The 24th week of 2004 starts on Monday, June 7th.
        String input = "2004-W24T+08:00";
        DateTime expected = new DateTime(2004, 6, 7, 0, 0, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesDateTimeWithHour() {
        String input = "2004-06-09T12+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 0, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesDateTimeWithMinute() {
        String input = "2004-06-09T12:24+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesDateTimeWithSecond() {
        String input = "2004-06-09T12:24:48+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesDateTimeWithFractionalHour() {
        // 12.5 hours is 12 hours and 30 minutes.
        String input = "2004-06-09T12.5+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 30, 0, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesDateTimeWithFractionalMinute() {
        // 24.5 minutes is 24 minutes and 30 seconds.
        String input = "2004-06-09T12:24.5+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 30, 0, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesDateTimeWithFractionalSecond() {
        // 48.5 seconds is 48 seconds and 500 milliseconds.
        String input = "2004-06-09T12:24:48.5+08:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 500, ZONE_PLUS_8);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_PLUS_8);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_parsesStringWithDefaultChronology() {
        // The input string has no time zone, so it's parsed using the default zone (set to London in setUp).
        String input = "2004-06-09T12:24:48.501";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, DateTimeZone.forID("Europe/London"));

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(input, ISO_CHRONOLOGY_DEFAULT_ZONE);

        assertEquals(expected.getMillis(), actualMillis);
    }
}