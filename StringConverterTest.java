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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 */
public class TestStringConverter {

    private static final DateTimeZone ONE_HOUR = DateTimeZone.forOffsetHours(1);
    private static final DateTimeZone SIX = DateTimeZone.forOffsetHours(6);
    private static final DateTimeZone SEVEN = DateTimeZone.forOffsetHours(7);
    private static final DateTimeZone EIGHT = DateTimeZone.forOffsetHours(8);
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    
    private static final Chronology ISO_EIGHT = ISOChronology.getInstance(EIGHT);
    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);
    
    private static Chronology ISO;
    private static Chronology JULIAN;
    
    private DateTimeZone originalZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        originalZone = DateTimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
        
        JULIAN = JulianChronology.getInstance();
        ISO = ISOChronology.getInstance();
    }

    @After
    public void tearDown() {
        DateTimeZone.setDefault(originalZone);
        Locale.setDefault(originalLocale);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testSingletonPattern() throws Exception {
        Class<?> cls = StringConverter.class;
        assertFalse("Class should not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("Class should not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("Class should not be private", Modifier.isPrivate(cls.getModifiers()));
        
        Constructor<?> con = cls.getDeclaredConstructor();
        assertEquals("Should have exactly one constructor", 1, cls.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", Modifier.isProtected(con.getModifiers()));
        
        Field instanceField = cls.getDeclaredField("INSTANCE");
        assertFalse("INSTANCE field should not be public", Modifier.isPublic(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be protected", Modifier.isProtected(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be private", Modifier.isPrivate(instanceField.getModifiers()));
    }

    @Test
    public void testSupportedType() {
        assertEquals(String.class, StringConverter.INSTANCE.getSupportedType());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetInstantMillisWithFullDateTime() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithYearOnly() {
        DateTime expected = new DateTime(2004, 1, 1, 0, 0, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004T+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithYearMonth() {
        DateTime expected = new DateTime(2004, 6, 1, 0, 0, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06T+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithDate() {
        DateTime expected = new DateTime(2004, 6, 9, 0, 0, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithOrdinalDate() {
        DateTime expected = new DateTime(2004, 6, 9, 0, 0, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-161T+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithWeekDate() {
        DateTime expected = new DateTime(2004, 6, 9, 0, 0, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-W24-3T+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithWeek() {
        DateTime expected = new DateTime(2004, 6, 7, 0, 0, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-W24T+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithHour() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 0, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithMinute() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithSecond() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithDecimalHour() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 30, 0, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12.5+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithDecimalMinute() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 30, 0, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24.5+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithDecimalSecond() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 500, EIGHT);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.5+08:00", ISO_EIGHT);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithDefaultTimeZone() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501", ISO);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithExplicitTimeZone() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+02:00", ISO_PARIS);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithImplicitTimeZone() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501", ISO_PARIS);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithJulianChronology() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, JulianChronology.getInstance(LONDON));
        long actual = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+01:00", JULIAN);
        assertEquals(expected.getMillis(), actual);
    }

    @Test
    public void testGetInstantMillisWithEmptyString() {
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getInstantMillis("", (Chronology) null));
    }

    @Test
    public void testGetInstantMillisWithInvalidString() {
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getInstantMillis("X", (Chronology) null));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetChronologyWithExplicitTimeZone() {
        Chronology chrono = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", PARIS);
        assertEquals(ISOChronology.getInstance(PARIS), chrono);
    }

    @Test
    public void testGetChronologyWithImplicitTimeZone() {
        Chronology chrono = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", PARIS);
        assertEquals(ISOChronology.getInstance(PARIS), chrono);
    }

    @Test
    public void testGetChronologyWithNullTimeZone() {
        Chronology chrono = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", (DateTimeZone) null);
        assertEquals(ISOChronology.getInstance(LONDON), chrono);
    }

    @Test
    public void testGetChronologyWithJulianChronology() {
        Chronology chrono = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", JULIAN);
        assertEquals(JulianChronology.getInstance(LONDON), chrono);
    }

    @Test
    public void testGetChronologyWithNullChronology() {
        Chronology chrono = StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", (Chronology) null);
        assertEquals(ISOChronology.getInstance(LONDON), chrono);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetPartialValues() {
        TimeOfDay tod = new TimeOfDay();
        int[] expected = {3, 4, 5, 6};
        int[] actual = StringConverter.INSTANCE.getPartialValues(tod, "T03:04:05.006", ISOChronology.getInstance());
        assertArrayEquals(expected, actual);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testDateTimeConstructorWithDefaultZone() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501+01:00");
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, LONDON);
    }

    @Test
    public void testDateTimeConstructorWithImplicitZone() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501");
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, LONDON);
    }

    @Test
    public void testDateTimeConstructorWithExplicitZone() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501+02:00", PARIS);
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    @Test
    public void testDateTimeConstructorWithZoneAndChronology() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501+02:00", JulianChronology.getInstance(PARIS));
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    private void assertDateTimeFields(DateTime dt, int year, int month, int day, 
                                     int hour, int minute, int second, int millis, DateTimeZone zone) {
        assertEquals(year, dt.getYear());
        assertEquals(month, dt.getMonthOfYear());
        assertEquals(day, dt.getDayOfMonth());
        assertEquals(hour, dt.getHourOfDay());
        assertEquals(minute, dt.getMinuteOfHour());
        assertEquals(second, dt.getSecondOfMinute());
        assertEquals(millis, dt.getMillisOfSecond());
        assertEquals(zone, dt.getZone());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetDurationMillisWithSeconds() {
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("PT12.345S"));
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("pt12.345s"));
        assertEquals(12000, StringConverter.INSTANCE.getDurationMillis("pt12s"));
        assertEquals(12000, StringConverter.INSTANCE.getDurationMillis("pt12.s"));
        assertEquals(-12320, StringConverter.INSTANCE.getDurationMillis("pt-12.32s"));
        assertEquals(-320, StringConverter.INSTANCE.getDurationMillis("pt-0.32s"));
        assertEquals(0, StringConverter.INSTANCE.getDurationMillis("pt-0.0s"));
        assertEquals(0, StringConverter.INSTANCE.getDurationMillis("pt0.0s"));
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("pt12.3456s"));
    }

    @Test
    public void testGetDurationMillisWithInvalidFormats() {
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("P2Y6M9DXYZ"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("PTS"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("XT0S"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("PX0S"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("PT0X"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("PTXS"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("PT0.0.0S"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("PT0-00S"));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.getDurationMillis("PT-.001S"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), 
            StringConverter.INSTANCE.getPeriodType("P2Y6M9D"));
    }

    @Test
    public void testSetIntoPeriodWithYearMonthDayTime() {
        MutablePeriod m = new MutablePeriod(PeriodType.yearMonthDayTime());
        StringConverter.INSTANCE.setInto(m, "P2Y6M9DT12H24M48S", null);
        assertPeriodValues(m, 2, 6, 9, 12, 24, 48, 0);
    }

    @Test
    public void testSetIntoPeriodWithYearWeekDayTime() {
        MutablePeriod m = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(m, "P2Y4W3DT12H24M48S", null);
        assertPeriodValues(m, 2, 0, 0, 12, 24, 48, 0);
        assertEquals(4, m.getWeeks());
        assertEquals(3, m.getDays());
    }

    @Test
    public void testSetIntoPeriodWithFractionalSeconds() {
        MutablePeriod m = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(m, "P2Y4W3DT12H24M48.034S", null);
        assertPeriodValues(m, 2, 0, 0, 12, 24, 48, 34);
    }

    @Test
    public void testSetIntoPeriodWithFractionalMinutes() {
        MutablePeriod m = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(m, "P2Y4W3DT12H24M.056S", null);
        assertPeriodValues(m, 2, 0, 0, 12, 24, 0, 56);
    }

    @Test
    public void testSetIntoPeriodWithWholeSeconds() {
        MutablePeriod m = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(m, "P2Y4W3DT12H24M56.S", null);
        assertPeriodValues(m, 2, 0, 0, 12, 24, 56, 0);
    }

    @Test
    public void testSetIntoPeriodWithMillisecondPrecision() {
        MutablePeriod m = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(m, "P2Y4W3DT12H24M56.1234567S", null);
        assertPeriodValues(m, 2, 0, 0, 12, 24, 56, 123);
    }

    @Test
    public void testSetIntoPeriodWithDateOnly() {
        MutablePeriod m = new MutablePeriod(1, 0, 1, 1, 1, 1, 1, 1, PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(m, "P2Y4W3D", null);
        assertPeriodValues(m, 2, 0, 0, 0, 0, 0, 0);
        assertEquals(4, m.getWeeks());
        assertEquals(3, m.getDays());
    }

    @Test
    public void testSetIntoPeriodWithInvalidFormats() {
        MutablePeriod m = new MutablePeriod();
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "", null));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "PXY", null));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "PT0SXY", null));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "P2Y4W3DT12H24M48SX", null));
    }

    private void assertPeriodValues(MutablePeriod period, 
                                   int years, int months, int days, 
                                   int hours, int minutes, int seconds, int millis) {
        assertEquals(years, period.getYears());
        assertEquals(months, period.getMonths());
        assertEquals(days, period.getDays());
        assertEquals(hours, period.getHours());
        assertEquals(minutes, period.getMinutes());
        assertEquals(seconds, period.getSeconds());
        assertEquals(millis, period.getMillis());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testIsReadableInterval() {
        assertFalse(StringConverter.INSTANCE.isReadableInterval("", null));
    }

    @Test
    public void testSetIntoIntervalWithStartDateAndDuration() {
        MutableInterval m = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(m, "2004-06-09/P1Y2M", null);
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0), m.getStart());
        assertEquals(new DateTime(2005, 8, 9, 0, 0, 0, 0), m.getEnd());
        assertEquals(ISOChronology.getInstance(), m.getChronology());
    }

    @Test
    public void testSetIntoIntervalWithDurationAndEndDate() {
        MutableInterval m = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(m, "P1Y2M/2004-06-09", null);
        assertEquals(new DateTime(2003, 4, 9, 0, 0, 0, 0), m.getStart());
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0), m.getEnd());
        assertEquals(ISOChronology.getInstance(), m.getChronology());
    }

    @Test
    public void testSetIntoIntervalWithStartAndEndDate() {
        MutableInterval m = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(m, "2003-08-09/2004-06-09", null);
        assertEquals(new DateTime(2003, 8, 9, 0, 0, 0, 0), m.getStart());
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0), m.getEnd());
        assertEquals(ISOChronology.getInstance(), m.getChronology());
    }

    @Test
    public void testSetIntoIntervalWithStartDateAndDurationInTimeZone() {
        MutableInterval m = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(m, "2004-06-09T+06:00/P1Y2M", null);
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0, SIX), m.getStart());
        assertEquals(new DateTime(2005, 8, 9, 0, 0, 0, 0, SIX), m.getEnd());
        assertEquals(ISOChronology.getInstance(), m.getChronology());
    }

    @Test
    public void testSetIntoIntervalWithBuddhistChronology() {
        MutableInterval m = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(m, "2003-08-09/2004-06-09", BuddhistChronology.getInstance());
        assertEquals(new DateTime(2003, 8, 9, 0, 0, 0, 0, BuddhistChronology.getInstance()), m.getStart());
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0, BuddhistChronology.getInstance()), m.getEnd());
        assertEquals(BuddhistChronology.getInstance(), m.getChronology());
    }

    @Test
    public void testSetIntoIntervalWithInvalidFormats() {
        MutableInterval m = new MutableInterval(-1000L, 1000L);
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "", null));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "/", null));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "P1Y/", null));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "/P1Y", null));
        
        assertThrows(IllegalArgumentException.class,
            () -> StringConverter.INSTANCE.setInto(m, "P1Y/P2Y", null));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToString() {
        assertEquals("Converter[java.lang.String]", StringConverter.INSTANCE.toString());
    }

}