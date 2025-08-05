/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for StringConverter.
 *
 * @author Stephen Colebourne
 */
public class TestStringConverter {

    private static final DateTimeZone ONE_HOUR = DateTimeZone.forOffsetHours(1);
    private static final DateTimeZone SIX_HOURS = DateTimeZone.forOffsetHours(6);
    private static final DateTimeZone SEVEN_HOURS = DateTimeZone.forOffsetHours(7);
    private static final DateTimeZone EIGHT_HOURS = DateTimeZone.forOffsetHours(8);
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final Chronology ISO_EIGHT = ISOChronology.getInstance(EIGHT_HOURS);
    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);
    private static final Chronology JULIAN_LONDON = JulianChronology.getInstance(LONDON);
    private static final Chronology BUDDHIST_DEFAULT = BuddhistChronology.getInstance();

    private DateTimeZone originalDefaultZone = null;
    private Locale originalDefaultLocale = null;

    @Before
    public void setUp() throws Exception {
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testSingleton() throws Exception {
        Class<?> cls = StringConverter.class;
        assertFalse(Modifier.isPublic(cls.getModifiers()));
        assertFalse(Modifier.isProtected(cls.getModifiers()));
        assertFalse(Modifier.isPrivate(cls.getModifiers()));

        Constructor<?> constructor = cls.getDeclaredConstructor((Class[]) null);
        assertEquals(1, cls.getDeclaredConstructors().length);
        assertTrue(Modifier.isProtected(constructor.getModifiers()));

        Field instanceField = cls.getDeclaredField("INSTANCE");
        assertFalse(Modifier.isPublic(instanceField.getModifiers()));
        assertFalse(Modifier.isProtected(instanceField.getModifiers()));
        assertFalse(Modifier.isPrivate(instanceField.getModifiers()));
    }

    @Test
    public void testSupportedType() {
        assertEquals(String.class, StringConverter.INSTANCE.getSupportedType());
    }

    //-----------------------------------------------------------------------
    // getInstantMillis
    //-----------------------------------------------------------------------
    @Test
    public void testGetInstantMillis_parsesFullDateTime() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, EIGHT_HOURS);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillis_parsesVariousDateFormats() {
        // Year only
        DateTime expected1 = new DateTime(2004, 1, 1, 0, 0, 0, 0, EIGHT_HOURS);
        assertEquals(expected1.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004T+08:00", ISO_EIGHT));

        // Year and month
        DateTime expected2 = new DateTime(2004, 6, 1, 0, 0, 0, 0, EIGHT_HOURS);
        assertEquals(expected2.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06T+08:00", ISO_EIGHT));

        // Ordinal date
        DateTime expected3 = new DateTime(2004, 6, 9, 0, 0, 0, 0, EIGHT_HOURS); // Day 161
        assertEquals(expected3.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-161T+08:00", ISO_EIGHT));

        // Week date
        DateTime expected4 = new DateTime(2004, 6, 9, 0, 0, 0, 0, EIGHT_HOURS); // Week 24, day 3
        assertEquals(expected4.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-W24-3T+08:00", ISO_EIGHT));
    }

    @Test
    public void testGetInstantMillis_parsesVariousTimeFormats() {
        // Hour only
        DateTime expected1 = new DateTime(2004, 6, 9, 12, 0, 0, 0, EIGHT_HOURS);
        assertEquals(expected1.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12+08:00", ISO_EIGHT));

        // Hour and minute
        DateTime expected2 = new DateTime(2004, 6, 9, 12, 24, 0, 0, EIGHT_HOURS);
        assertEquals(expected2.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24+08:00", ISO_EIGHT));

        // Fractional hour
        DateTime expected3 = new DateTime(2004, 6, 9, 12, 30, 0, 0, EIGHT_HOURS);
        assertEquals(expected3.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12.5+08:00", ISO_EIGHT));

        // Fractional minute
        DateTime expected4 = new DateTime(2004, 6, 9, 12, 24, 30, 0, EIGHT_HOURS);
        assertEquals(expected4.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24.5+08:00", ISO_EIGHT));

        // Fractional second
        DateTime expected5 = new DateTime(2004, 6, 9, 12, 24, 48, 500, EIGHT_HOURS);
        assertEquals(expected5.getMillis(), StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.5+08:00", ISO_EIGHT));
    }

    @Test
    public void testGetInstantMillis_withoutZoneInString_usesChronologyZone() {
        DateTime expectedInParis = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        long actualParis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501", ISO_PARIS);
        assertEquals(expectedInParis.getMillis(), actualParis);

        DateTime expectedInLondon = new DateTime(2004, 6, 9, 12, 24, 48, 501, LONDON);
        long actualLondon = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501", ISO_LONDON);
        assertEquals(expectedInLondon.getMillis(), actualLondon);
    }

    @Test
    public void testGetInstantMillis_withNonIsoChronology() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, JULIAN_LONDON);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+01:00", JULIAN_LONDON);
        assertEquals(expected.getMillis(), actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstantMillis_withEmptyString_throwsException() {
        StringConverter.INSTANCE.getInstantMillis("", (Chronology) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstantMillis_withInvalidString_throwsException() {
        StringConverter.INSTANCE.getInstantMillis("X", (Chronology) null);
    }

    //-----------------------------------------------------------------------
    // getChronology
    //-----------------------------------------------------------------------
    @Test
    public void testGetChronology_fromStringWithOffset_returnsChronologyWithParsedZone() {
        // The string's +01:00 offset should create a LONDON-based chronology, overriding the passed PARIS zone.
        Chronology actual = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", PARIS);
        assertEquals(ISOChronology.getInstance(LONDON), actual);
    }

    @Test
    public void testGetChronology_fromStringWithoutOffset_returnsChronologyWithPassedZone() {
        Chronology actual = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", PARIS);
        assertEquals(ISOChronology.getInstance(PARIS), actual);
    }

    @Test
    public void testGetChronology_withNullZone_usesDefaultZone() {
        // Default zone is LONDON
        Chronology actual = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", (DateTimeZone) null);
        assertEquals(ISOChronology.getInstance(LONDON), actual);
    }

    @Test
    public void testGetChronology_withSpecificChronology_retainsChronologyType() {
        // The Julian chronology type should be preserved, but the zone should be from the string (+01:00 -> LONDON)
        Chronology actual = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", JULIAN_LONDON);
        assertEquals(JulianChronology.getInstance(LONDON), actual);
    }

    //-----------------------------------------------------------------------
    // getPartialValues
    //-----------------------------------------------------------------------
    @Test
    public void testGetPartialValues() {
        ReadablePartial partial = new TimeOfDay(); // Represents hour, minute, second, millis
        String stringToParse = "T03:04:05.006";
        int[] expected = {3, 4, 5, 6}; // 03:04:05.006

        int[] actual = StringConverter.INSTANCE.getPartialValues(partial, stringToParse, ISOChronology.getInstance());
        assertTrue(Arrays.equals(expected, actual));
    }

    //-----------------------------------------------------------------------
    // getDurationMillis
    //-----------------------------------------------------------------------
    @Test
    public void testGetDurationMillis_parsesValidISOPeriod() {
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("PT12.345S"));
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("pt12.345s")); // case-insensitive
        assertEquals(12000, StringConverter.INSTANCE.getDurationMillis("pt12s"));
        assertEquals(-12320, StringConverter.INSTANCE.getDurationMillis("pt-12.32s"));
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("pt12.3456s")); // truncates
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDurationMillis_withInvalidPeriod_throwsException() {
        // This format is for a Period, not a Duration
        StringConverter.INSTANCE.getDurationMillis("P2Y6M9D");
    }

    //-----------------------------------------------------------------------
    // Period
    //-----------------------------------------------------------------------
    @Test
    public void testGetPeriodType_returnsStandardType() {
        assertEquals(PeriodType.standard(), StringConverter.INSTANCE.getPeriodType("P2Y6M9D"));
    }

    @Test
    public void testSetIntoPeriod_parsesAllFields() {
        MutablePeriod period = new MutablePeriod(PeriodType.yearMonthDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y6M9DT12H24M48S", null);

        assertEquals(2, period.getYears());
        assertEquals(6, period.getMonths());
        assertEquals(9, period.getDays());
        assertEquals(12, period.getHours());
        assertEquals(24, period.getMinutes());
        assertEquals(48, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSetIntoPeriod_parsesWeeksAndFractionalSeconds() {
        MutablePeriod period = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3DT12H24M48.034S", null);

        assertEquals(2, period.getYears());
        assertEquals(4, period.getWeeks());
        assertEquals(3, period.getDays());
        assertEquals(12, period.getHours());
        assertEquals(24, period.getMinutes());
        assertEquals(48, period.getSeconds());
        assertEquals(34, period.getMillis());
    }

    @Test
    public void testSetIntoPeriod_withDateOnly_resetsTimeFields() {
        // Start with a non-zero period
        MutablePeriod period = new MutablePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3D", null);

        // Asserts that time fields are cleared
        assertEquals(2, period.getYears());
        assertEquals(4, period.getWeeks());
        assertEquals(3, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntoPeriod_withEmptyString_throwsException() {
        StringConverter.INSTANCE.setInto(new MutablePeriod(), "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntoPeriod_withInvalidString_throwsException() {
        StringConverter.INSTANCE.setInto(new MutablePeriod(), "PXY", null);
    }

    //-----------------------------------------------------------------------
    // Interval
    //-----------------------------------------------------------------------
    @Test
    public void testIsReadableInterval_withEmptyString_returnsFalse() {
        assertFalse(StringConverter.INSTANCE.isReadableInterval("", null));
    }

    @Test
    public void testSetIntoInterval_fromStartAndPeriod() {
        MutableInterval interval = new MutableInterval();
        StringConverter.INSTANCE.setInto(interval, "2004-06-09/P1Y2M", null);

        DateTime expectedStart = new DateTime("2004-06-09T00:00:00.000");
        DateTime expectedEnd = expectedStart.plusYears(1).plusMonths(2);

        assertEquals(expectedStart, interval.getStart());
        assertEquals(expectedEnd, interval.getEnd());
    }

    @Test
    public void testSetIntoInterval_fromPeriodAndEnd() {
        MutableInterval interval = new MutableInterval();
        StringConverter.INSTANCE.setInto(interval, "P1Y2M/2004-06-09", null);

        DateTime expectedEnd = new DateTime("2004-06-09T00:00:00.000");
        DateTime expectedStart = expectedEnd.minusYears(1).minusMonths(2);

        assertEquals(expectedStart, interval.getStart());
        assertEquals(expectedEnd, interval.getEnd());
    }

    @Test
    public void testSetIntoInterval_fromStartAndEnd() {
        MutableInterval interval = new MutableInterval();
        StringConverter.INSTANCE.setInto(interval, "2003-08-09/2004-06-09", null);

        assertEquals(new DateTime("2003-08-09"), interval.getStart());
        assertEquals(new DateTime("2004-06-09"), interval.getEnd());
    }

    @Test
    public void testSetIntoInterval_withZonedEndpoints() {
        MutableInterval interval = new MutableInterval();
        StringConverter.INSTANCE.setInto(interval, "2003-08-09T+06:00/2004-06-09T+07:00", null);

        // The converter should parse the instants correctly using their specified offsets.
        DateTime expectedStart = new DateTime("2003-08-09T00:00:00.000+06:00");
        DateTime expectedEnd = new DateTime("2004-06-09T00:00:00.000+07:00");

        assertEquals(expectedStart.getMillis(), interval.getStartMillis());
        assertEquals(expectedEnd.getMillis(), interval.getEndMillis());
    }

    @Test
    public void testSetIntoInterval_withSpecificChronology() {
        MutableInterval interval = new MutableInterval();
        StringConverter.INSTANCE.setInto(interval, "2003-08-09/2004-06-09", BUDDHIST_DEFAULT);

        DateTime expectedStart = new DateTime("2003-08-09", BUDDHIST_DEFAULT);
        DateTime expectedEnd = new DateTime("2004-06-09", BUDDHIST_DEFAULT);

        assertEquals(expectedStart, interval.getStart());
        assertEquals(expectedEnd, interval.getEnd());
        assertEquals(BUDDHIST_DEFAULT, interval.getChronology());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntoInterval_withTwoPeriods_throwsException() {
        StringConverter.INSTANCE.setInto(new MutableInterval(), "P1Y/P2Y", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntoInterval_withMissingEndpoint_throwsException() {
        StringConverter.INSTANCE.setInto(new MutableInterval(), "/P1Y", null);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToString() {
        assertEquals("Converter[java.lang.String]", StringConverter.INSTANCE.toString());
    }
}