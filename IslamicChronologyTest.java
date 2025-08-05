/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class is a Junit unit test for IslamicChronology.
 * The modifications aim to improve readability and maintainability.
 *
 * @author Stephen Colebourne
 */
public class TestIslamicChronology {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Represents 2002-06-09 in the ISO calendar.
    private static final long TEST_TIME_NOW;

    static {
        long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
                365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                366 + 365;
        TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;
    }

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    // -- Factory and Singleton Tests --

    @Test
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, IslamicChronology.getInstanceUTC().getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstanceUTC().getClass());
    }

    @Test
    public void testFactory() {
        assertEquals(LONDON, IslamicChronology.getInstance().getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstance().getClass());
    }

    @Test
    public void testFactory_Zone() {
        assertEquals(TOKYO, IslamicChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, IslamicChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, IslamicChronology.getInstance(null).getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstance(TOKYO).getClass());
    }

    @Test
    public void testEquality() {
        assertSame(IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO));
        assertSame(IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(LONDON));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance(PARIS));
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());
        assertSame(IslamicChronology.getInstance(), IslamicChronology.getInstance(LONDON));
    }

    @Test
    public void testWithUTC() {
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(LONDON).withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(TOKYO).withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC().withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance().withUTC());
    }

    @Test
    public void testWithZone() {
        assertSame(IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(TOKYO).withZone(null));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance().withZone(PARIS));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void testToString() {
        assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance(LONDON).toString());
        assertEquals("IslamicChronology[Asia/Tokyo]", IslamicChronology.getInstance(TOKYO).toString());
        assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance().toString());
        assertEquals("IslamicChronology[UTC]", IslamicChronology.getInstanceUTC().toString());
    }

    // -- Field Definition Tests --

    @Test
    public void testDurationFields() {
        final Chronology islamic = IslamicChronology.getInstance();
        assertEquals("eras", islamic.eras().getName());
        assertEquals("centuries", islamic.centuries().getName());
        assertEquals("years", islamic.years().getName());
        assertEquals("weekyears", islamic.weekyears().getName());
        assertEquals("months", islamic.months().getName());
        assertEquals("weeks", islamic.weeks().getName());
        assertEquals("days", islamic.days().getName());
        assertEquals("halfdays", islamic.halfdays().getName());
        assertEquals("hours", islamic.hours().getName());
        assertEquals("minutes", islamic.minutes().getName());
        assertEquals("seconds", islamic.seconds().getName());
        assertEquals("millis", islamic.millis().getName());
    }

    @Test
    public void testDurationFieldSupported() {
        final Chronology islamic = IslamicChronology.getInstance();
        assertFalse(islamic.eras().isSupported());
        assertTrue(islamic.centuries().isSupported());
        assertTrue(islamic.years().isSupported());
        assertTrue(islamic.weekyears().isSupported());
        assertTrue(islamic.months().isSupported());
        assertTrue(islamic.weeks().isSupported());
        assertTrue(islamic.days().isSupported());
        assertTrue(islamic.halfdays().isSupported());
        assertTrue(islamic.hours().isSupported());
        assertTrue(islamic.minutes().isSupported());
        assertTrue(islamic.seconds().isSupported());
        assertTrue(islamic.millis().isSupported());
    }

    @Test
    public void testDurationFieldPrecision() {
        final Chronology islamic = IslamicChronology.getInstance();
        assertFalse(islamic.centuries().isPrecise());
        assertFalse(islamic.years().isPrecise());
        assertFalse(islamic.weekyears().isPrecise());
        assertFalse(islamic.months().isPrecise());
        assertFalse(islamic.weeks().isPrecise());
        assertFalse(islamic.days().isPrecise());
        assertFalse(islamic.halfdays().isPrecise());
        assertTrue(islamic.hours().isPrecise());
        assertTrue(islamic.minutes().isPrecise());
        assertTrue(islamic.seconds().isPrecise());
        assertTrue(islamic.millis().isPrecise());
    }

    @Test
    public void testDateFields() {
        final Chronology islamic = IslamicChronology.getInstance();
        assertEquals("era", islamic.era().getName());
        assertEquals("centuryOfEra", islamic.centuryOfEra().getName());
        assertEquals("yearOfCentury", islamic.yearOfCentury().getName());
        assertEquals("yearOfEra", islamic.yearOfEra().getName());
        assertEquals("year", islamic.year().getName());
        assertEquals("monthOfYear", islamic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", islamic.weekyearOfCentury().getName());
        assertEquals("weekyear", islamic.weekyear().getName());
        assertEquals("weekOfWeekyear", islamic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", islamic.dayOfYear().getName());
        assertEquals("dayOfMonth", islamic.dayOfMonth().getName());
        assertEquals("dayOfWeek", islamic.dayOfWeek().getName());
    }

    @Test
    public void testTimeFields() {
        final Chronology islamic = IslamicChronology.getInstance();
        assertEquals("halfdayOfDay", islamic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", islamic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", islamic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", islamic.clockhourOfDay().getName());
        assertEquals("hourOfDay", islamic.hourOfDay().getName());
        assertEquals("minuteOfDay", islamic.minuteOfDay().getName());
        assertEquals("minuteOfHour", islamic.minuteOfHour().getName());
        assertEquals("secondOfDay", islamic.secondOfDay().getName());
        assertEquals("secondOfMinute", islamic.secondOfMinute().getName());
        assertEquals("millisOfDay", islamic.millisOfDay().getName());
        assertEquals("millisOfSecond", islamic.millisOfSecond().getName());
    }

    // -- Core Chronology Tests --

    @Test
    public void testEpoch() {
        Chronology islamicUTC = IslamicChronology.getInstanceUTC();
        Chronology julianUTC = JulianChronology.getInstanceUTC();
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, islamicUTC);
        DateTime expectedEpoch = new DateTime(622, 7, 16, 0, 0, 0, 0, julianUTC);
        assertEquals(expectedEpoch.getMillis(), epoch.getMillis());
    }

    @Test
    public void testEra() {
        assertEquals(1, IslamicChronology.AH);
        try {
            new DateTime(-1, 1, 1, 0, 0, 0, 0, IslamicChronology.getInstanceUTC());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testFieldConstructor() {
        Chronology islamicUTC = IslamicChronology.getInstanceUTC();
        Chronology isoUTC = ISOChronology.getInstanceUTC();
        DateTime date = new DateTime(1364, 12, 6, 0, 0, 0, 0, islamicUTC);
        DateTime expectedDate = new DateTime(1945, 11, 12, 0, 0, 0, 0, isoUTC);
        assertEquals(expectedDate.getMillis(), date.getMillis());
    }

    // -- Sample Date Tests --

    @Test
    public void testSampleDate_fromIso_1945_11_12() {
        // This date is equivalent to 1364-12-06 in the Islamic calendar.
        DateTime dt_iso = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        DateTime dt = dt_iso.withChronology(IslamicChronology.getInstanceUTC());

        // Era and Year
        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(14, dt.getCenturyOfEra());
        assertEquals(64, dt.getYearOfCentury());
        assertEquals(1364, dt.getYearOfEra());
        assertEquals(1364, dt.getYear());
        assertFalse(dt.year().isLeap());

        // Month
        assertEquals(12, dt.getMonthOfYear());
        assertEquals(29, dt.dayOfMonth().getMaximumValue()); // Max days in this month

        // Day
        assertEquals(6, dt.getDayOfMonth());
        assertEquals(DateTimeConstants.MONDAY, dt.getDayOfWeek());
        assertEquals(330, dt.getDayOfYear()); // 6*30 + 5*29 + 6
    }

    @Test
    public void testSampleDate_fromIso_2005_11_26() {
        // This date is equivalent to 1426-10-24 in the Islamic calendar.
        // 1426 is a leap year in the 16-based pattern.
        DateTime dt_iso = new DateTime(2005, 11, 26, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        DateTime dt = dt_iso.withChronology(IslamicChronology.getInstanceUTC());

        // Era and Year
        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(15, dt.getCenturyOfEra());
        assertEquals(26, dt.getYearOfCentury());
        assertEquals(1426, dt.getYearOfEra());
        assertEquals(1426, dt.getYear());
        assertTrue(dt.year().isLeap());
        assertEquals(355, dt.year().getMaximumValue()); // days in a leap year

        // Month
        assertEquals(10, dt.getMonthOfYear());

        // Day
        assertEquals(24, dt.getDayOfMonth());
        assertEquals(DateTimeConstants.SATURDAY, dt.getDayOfWeek());
        assertEquals(290, dt.getDayOfYear()); // 5*30 + 4*29 + 24
    }

    @Test
    public void testSampleDate_direct_1426_12_24() {
        // 1426 is a leap year, so the 12th month (Dhu al-Hijjah) has 30 days.
        DateTime dt = new DateTime(1426, 12, 24, 0, 0, 0, 0, IslamicChronology.getInstanceUTC());

        // Era and Year
        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(1426, dt.getYear());
        assertTrue(dt.year().isLeap());

        // Month
        assertEquals(12, dt.getMonthOfYear());
        assertTrue(dt.monthOfYear().isLeap()); // The 12th month is the leap month
        assertEquals(30, dt.dayOfMonth().getMaximumValue()); // Max days in this month

        // Day
        assertEquals(24, dt.getDayOfMonth());
        assertEquals(DateTimeConstants.TUESDAY, dt.getDayOfWeek());
        assertEquals(348, dt.getDayOfYear()); // 6*30 + 5*29 + 24
    }

    @Test
    public void testSampleDateWithZone() {
        DateTime dt_paris = new DateTime(2005, 11, 26, 12, 0, 0, 0, PARIS);
        DateTime dt_utc = dt_paris.withChronology(IslamicChronology.getInstanceUTC());

        assertEquals(1426, dt_utc.getYear());
        assertEquals(10, dt_utc.getMonthOfYear());
        assertEquals(24, dt_utc.getDayOfMonth());
        // In November, Paris is UTC+1, so 12:00 in Paris is 11:00 in UTC.
        assertEquals(11, dt_utc.getHourOfDay());
    }

    // -- Leap Year Pattern Tests --

    @Test
    public void testLeapYear_15Based() {
        IslamicChronology.LeapYearPatternType pattern = IslamicChronology.LEAP_YEAR_15_BASED;
        assertTrue(pattern.isLeapYear(2));
        assertTrue(pattern.isLeapYear(5));
        assertTrue(pattern.isLeapYear(7));
        assertTrue(pattern.isLeapYear(10));
        assertTrue(pattern.isLeapYear(13));
        assertTrue(pattern.isLeapYear(15));
        assertTrue(pattern.isLeapYear(18));
        assertTrue(pattern.isLeapYear(21));
        assertTrue(pattern.isLeapYear(24));
        assertTrue(pattern.isLeapYear(26));
        assertTrue(pattern.isLeapYear(29));

        assertFalse(pattern.isLeapYear(16)); // Different from 16-based
    }

    @Test
    public void testLeapYear_16Based() {
        IslamicChronology.LeapYearPatternType pattern = IslamicChronology.LEAP_YEAR_16_BASED;
        assertTrue(pattern.isLeapYear(2));
        assertTrue(pattern.isLeapYear(5));
        assertTrue(pattern.isLeapYear(7));
        assertTrue(pattern.isLeapYear(10));
        assertTrue(pattern.isLeapYear(13));
        assertTrue(pattern.isLeapYear(16));
        assertTrue(pattern.isLeapYear(18));
        assertTrue(pattern.isLeapYear(21));
        assertTrue(pattern.isLeapYear(24));
        assertTrue(pattern.isLeapYear(26));
        assertTrue(pattern.isLeapYear(29));

        assertFalse(pattern.isLeapYear(15)); // Different from 15-based
    }

    @Test
    public void testLeapYear_Indian() {
        IslamicChronology.LeapYearPatternType pattern = IslamicChronology.LEAP_YEAR_INDIAN;
        assertTrue(pattern.isLeapYear(2));
        assertTrue(pattern.isLeapYear(5));
        assertTrue(pattern.isLeapYear(8));
        assertTrue(pattern.isLeapYear(10));
        assertTrue(pattern.isLeapYear(13));
        assertTrue(pattern.isLeapYear(16));
        assertTrue(pattern.isLeapYear(19));
        assertTrue(pattern.isLeapYear(21));
        assertTrue(pattern.isLeapYear(24));
        assertTrue(pattern.isLeapYear(27));
        assertTrue(pattern.isLeapYear(29));

        assertFalse(pattern.isLeapYear(7)); // Different from 16-based
    }

    @Test
    public void testLeapYear_HabashAlHasib() {
        IslamicChronology.LeapYearPatternType pattern = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;
        assertTrue(pattern.isLeapYear(2));
        assertTrue(pattern.isLeapYear(5));
        assertTrue(pattern.isLeapYear(8));
        assertTrue(pattern.isLeapYear(11));
        assertTrue(pattern.isLeapYear(13));
        assertTrue(pattern.isLeapYear(16));
        assertTrue(pattern.isLeapYear(19));
        assertTrue(pattern.isLeapYear(21));
        assertTrue(pattern.isLeapYear(24));
        assertTrue(pattern.isLeapYear(27));
        assertTrue(pattern.isLeapYear(30));

        assertFalse(pattern.isLeapYear(29)); // Different from 16-based
    }
}