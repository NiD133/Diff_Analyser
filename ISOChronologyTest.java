/*
 *  Copyright 2001-2014 Stephen Colebourne
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

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * This class is a Junit unit test for ISOChronology.
 *
 * @author Stephen Colebourne
 */
@SuppressWarnings("deprecation")
public class TestISOChronology extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final DateTimeZone UTC = DateTimeZone.UTC;

    // Total days from year 1 to 2002 (inclusive)
    private static final long TOTAL_DAYS_UP_TO_2002 = 
        365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
        366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
        365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
        366 + 365; // 32 years

    // 2002-06-09 in milliseconds (days from 1970-01-01 to 2002-06-09 minus one day)
    private static final long TEST_TIME_2002_06_09 =
            (TOTAL_DAYS_UP_TO_2002 + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestISOChronology.class);
    }

    public TestISOChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_2002_06_09);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testFactoryUTC() {
        assertEquals(UTC, ISOChronology.getInstanceUTC().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, ISOChronology.getInstance().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, ISOChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, ISOChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, ISOChronology.getInstance((DateTimeZone) null).getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(PARIS));
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC());
        assertSame(ISOChronology.getInstance(), ISOChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(LONDON).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(TOKYO).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC().withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(null));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance().withZone(PARIS));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance(LONDON).toString());
        assertEquals("ISOChronology[Asia/Tokyo]", ISOChronology.getInstance(TOKYO).toString());
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance().toString());
        assertEquals("ISOChronology[UTC]", ISOChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        final ISOChronology iso = ISOChronology.getInstance();
        assertDurationField(iso.eras(), "eras", false, false);
        assertDurationField(iso.centuries(), "centuries", true, false);
        assertDurationField(iso.years(), "years", true, false);
        assertDurationField(iso.weekyears(), "weekyears", true, false);
        assertDurationField(iso.months(), "months", true, false);
        assertDurationField(iso.weeks(), "weeks", true, false);
        assertDurationField(iso.days(), "days", true, false);
        assertDurationField(iso.halfdays(), "halfdays", true, false);
        assertDurationField(iso.hours(), "hours", true, true);
        assertDurationField(iso.minutes(), "minutes", true, true);
        assertDurationField(iso.seconds(), "seconds", true, true);
        assertDurationField(iso.millis(), "millis", true, true);
    }

    public void testDurationFieldsInUTC() {
        final ISOChronology isoUTC = ISOChronology.getInstanceUTC();
        assertDurationField(isoUTC.weeks(), "weeks", true, true);
        assertDurationField(isoUTC.days(), "days", true, true);
        assertDurationField(isoUTC.halfdays(), "halfdays", true, true);
    }

    public void testDurationFieldsInGMT() {
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final ISOChronology isoGMT = ISOChronology.getInstance(gmt);
        assertDurationField(isoGMT.weeks(), "weeks", true, true);
        assertDurationField(isoGMT.days(), "days", true, true);
        assertDurationField(isoGMT.halfdays(), "halfdays", true, true);
    }

    public void testDurationFieldsInOffset() {
        final DateTimeZone offset = DateTimeZone.forOffsetHours(1);
        final ISOChronology isoOffset1 = ISOChronology.getInstance(offset);
        assertDurationField(isoOffset1.weeks(), "weeks", true, true);
        assertDurationField(isoOffset1.days(), "days", true, true);
        assertDurationField(isoOffset1.halfdays(), "halfdays", true, true);
    }

    private void assertDurationField(DurationField field, String name, boolean supported, boolean precise) {
        assertEquals(name, field.getName());
        assertEquals(supported, field.isSupported());
        assertEquals(precise, field.isPrecise());
    }

    //-----------------------------------------------------------------------
    public void testDateFields() {
        final ISOChronology iso = ISOChronology.getInstance();
        assertDateTimeField(iso.era(), "era", true, iso.eras(), null);
        assertDateTimeField(iso.centuryOfEra(), "centuryOfEra", true, iso.centuries(), iso.eras());
        assertDateTimeField(iso.yearOfCentury(), "yearOfCentury", true, iso.years(), iso.centuries());
        assertDateTimeField(iso.yearOfEra(), "yearOfEra", true, iso.years(), iso.eras());
        assertDateTimeField(iso.year(), "year", true, iso.years(), null);
        assertDateTimeField(iso.monthOfYear(), "monthOfYear", true, iso.months(), iso.years());
        assertDateTimeField(iso.weekyearOfCentury(), "weekyearOfCentury", true, iso.weekyears(), iso.centuries());
        assertDateTimeField(iso.weekyear(), "weekyear", true, iso.weekyears(), null);
        assertDateTimeField(iso.weekOfWeekyear(), "weekOfWeekyear", true, iso.weeks(), iso.weekyears());
        assertDateTimeField(iso.dayOfYear(), "dayOfYear", true, iso.days(), iso.years());
        assertDateTimeField(iso.dayOfMonth(), "dayOfMonth", true, iso.days(), iso.months());
        assertDateTimeField(iso.dayOfWeek(), "dayOfWeek", true, iso.days(), iso.weeks());
    }

    private void assertDateTimeField(org.joda.time.DateTimeField field, String name, boolean supported,
                                    DurationField durationField, DurationField rangeDurationField) {
        assertEquals(name, field.getName());
        assertEquals(supported, field.isSupported());
        assertEquals(durationField, field.getDurationField());
        assertEquals(rangeDurationField, field.getRangeDurationField());
    }

    //-----------------------------------------------------------------------
    public void testTimeFields() {
        final ISOChronology iso = ISOChronology.getInstance();
        String[] expectedFieldNames = {
            "halfdayOfDay", "clockhourOfHalfday", "hourOfHalfday", "clockhourOfDay",
            "hourOfDay", "minuteOfDay", "minuteOfHour", "secondOfDay",
            "secondOfMinute", "millisOfDay", "millisOfSecond"
        };

        org.joda.time.DateTimeField[] fields = {
            iso.halfdayOfDay(), iso.clockhourOfHalfday(), iso.hourOfHalfday(), iso.clockhourOfDay(),
            iso.hourOfDay(), iso.minuteOfDay(), iso.minuteOfHour(), iso.secondOfDay(),
            iso.secondOfMinute(), iso.millisOfDay(), iso.millisOfSecond()
        };

        for (int i = 0; i < fields.length; i++) {
            assertTimeFieldSupported(fields[i], expectedFieldNames[i]);
        }
    }

    private void assertTimeFieldSupported(org.joda.time.DateTimeField field, String expectedName) {
        assertEquals(expectedName, field.getName());
        assertTrue(field.isSupported());
    }

    //-----------------------------------------------------------------------
    public void testMaxYear() {
        final ISOChronology chrono = ISOChronology.getInstanceUTC();
        final int maxYear = chrono.year().getMaximumValue();

        DateTime start = new DateTime(maxYear, 1, 1, 0, 0, 0, 0, chrono);
        DateTime end = new DateTime(maxYear, 12, 31, 23, 59, 59, 999, chrono);
        assertValidMaxYearValues(chrono, maxYear, start, end);
        assertMaxYearStringRepresentations(chrono, maxYear, start, end);
        assertMaxYearBoundaryHandling(chrono, start, end, maxYear);
    }

    private void assertValidMaxYearValues(Chronology chrono, int maxYear, DateTime start, DateTime end) {
        assertTrue(start.getMillis() > 0);
        assertTrue(end.getMillis() > start.getMillis());
        assertEquals(maxYear, start.getYear());
        assertEquals(maxYear, end.getYear());

        long delta = end.getMillis() - start.getMillis();
        long expectedDelta = (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals(expectedDelta, delta);
    }

    private void assertMaxYearStringRepresentations(Chronology chrono, int maxYear, DateTime start, DateTime end) {
        assertEquals(start, new DateTime(maxYear + "-01-01T00:00:00.000Z", chrono));
        assertEquals(end, new DateTime(maxYear + "-12-31T23:59:59.999Z", chrono));
    }

    private void assertMaxYearBoundaryHandling(Chronology chrono, DateTime start, DateTime end, int maxYear) {
        try {
            start.plusYears(1);
            fail("Expected IllegalFieldValueException when exceeding max year");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            end.plusYears(1);
            fail("Expected IllegalFieldValueException when exceeding max year");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        assertEquals(maxYear + 1, chrono.year().get(Long.MAX_VALUE));
    }

    //-----------------------------------------------------------------------
    public void testMinYear() {
        final ISOChronology chrono = ISOChronology.getInstanceUTC();
        final int minYear = chrono.year().getMinimumValue();

        DateTime start = new DateTime(minYear, 1, 1, 0, 0, 0, 0, chrono);
        DateTime end = new DateTime(minYear, 12, 31, 23, 59, 59, 999, chrono);
        assertValidMinYearValues(chrono, minYear, start, end);
        assertMinYearStringRepresentations(chrono, minYear, start, end);
        assertMinYearBoundaryHandling(chrono, start, end, minYear);
    }

    private void assertValidMinYearValues(Chronology chrono, int minYear, DateTime start, DateTime end) {
        assertTrue(start.getMillis() < 0);
        assertTrue(end.getMillis() > start.getMillis());
        assertEquals(minYear, start.getYear());
        assertEquals(minYear, end.getYear());

        long delta = end.getMillis() - start.getMillis();
        long expectedDelta = (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals(expectedDelta, delta);
    }

    private void assertMinYearStringRepresentations(Chronology chrono, int minYear, DateTime start, DateTime end) {
        assertEquals(start, new DateTime(minYear + "-01-01T00:00:00.000Z", chrono));
        assertEquals(end, new DateTime(minYear + "-12-31T23:59:59.999Z", chrono));
    }

    private void assertMinYearBoundaryHandling(Chronology chrono, DateTime start, DateTime end, int minYear) {
        try {
            start.minusYears(1);
            fail("Expected IllegalFieldValueException when exceeding min year");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            end.minusYears(1);
            fail("Expected IllegalFieldValueException when exceeding min year");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        assertEquals(minYear - 1, chrono.year().get(Long.MIN_VALUE));
    }

    //-----------------------------------------------------------------------
    // Tests for Gregorian cutover in 1582
    public void testCutoverAddYears() {
        // Tests adding one year around the Gregorian cutover (1582-10-15)
        testAdd("1582-01-01", DurationFieldType.years(), 1, "1583-01-01");
        testAdd("1582-02-15", DurationFieldType.years(), 1, "1583-02-15");
        testAdd("1582-02-28", DurationFieldType.years(), 1, "1583-02-28");
        testAdd("1582-03-01", DurationFieldType.years(), 1, "1583-03-01");
        testAdd("1582-09-30", DurationFieldType.years(), 1, "1583-09-30");
        testAdd("1582-10-01", DurationFieldType.years(), 1, "1583-10-01");
        testAdd("1582-10-04", DurationFieldType.years(), 1, "1583-10-04");
        testAdd("1582-10-15", DurationFieldType.years(), 1, "1583-10-15");
        testAdd("1582-10-16", DurationFieldType.years(), 1, "1583-10-16");
        
        // Leap year tests spanning the cutover
        testAdd("1580-01-01", DurationFieldType.years(), 4, "1584-01-01");
        testAdd("1580-02-29", DurationFieldType.years(), 4, "1584-02-29");
        testAdd("1580-10-01", DurationFieldType.years(), 4, "1584-10-01");
        testAdd("1580-10-10", DurationFieldType.years(), 4, "1584-10-10");
        testAdd("1580-10-15", DurationFieldType.years(), 4, "1584-10-15");
        testAdd("1580-12-31", DurationFieldType.years(), 4, "1584-12-31");
    }

    public void testAddMonths() {
        // Tests adding months around the Gregorian cutover
        testAdd("1582-01-01", DurationFieldType.months(), 1, "1582-02-01");
        testAdd("1582-01-01", DurationFieldType.months(), 6, "1582-07-01");
        testAdd("1582-01-01", DurationFieldType.months(), 12, "1583-01-01");
        testAdd("1582-11-15", DurationFieldType.months(), 1, "1582-12-15");
        
        // Tests crossing the cutover (October 1582)
        testAdd("1582-09-04", DurationFieldType.months(), 2, "1582-11-04");
        testAdd("1582-09-05", DurationFieldType.months(), 2, "1582-11-05");
        testAdd("1582-09-10", DurationFieldType.months(), 2, "1582-11-10");
        testAdd("1582-09-15", DurationFieldType.months(), 2, "1582-11-15");
        
        // Leap year tests spanning the cutover
        testAdd("1580-01-01", DurationFieldType.months(), 48, "1584-01-01");
        testAdd("1580-02-29", DurationFieldType.months(), 48, "1584-02-29");
        testAdd("1580-10-01", DurationFieldType.months(), 48, "1584-10-01");
        testAdd("1580-10-10", DurationFieldType.months(), 48, "1584-10-10");
        testAdd("1580-10-15", DurationFieldType.months(), 48, "1584-10-15");
        testAdd("1580-12-31", DurationFieldType.months(), 48, "1584-12-31");
    }

    private void testAdd(String start, DurationFieldType type, int amt, String end) {
        Chronology isoUTC = ISOChronology.getInstanceUTC();
        DateTime dtStart = new DateTime(start, isoUTC);
        DateTime dtEnd = new DateTime(end, isoUTC);
        
        // Test forward and backward addition
        assertEquals(dtEnd, dtStart.withFieldAdded(type, amt));
        assertEquals(dtStart, dtEnd.withFieldAdded(type, -amt));

        // Test duration field difference
        DurationField field = type.getField(isoUTC);
        int diff = field.getDifference(dtEnd.getMillis(), dtStart.getMillis());
        assertEquals(amt, diff);
        
        // Test with Partial (YearMonthDay) for date-based fields
        if (type == DurationFieldType.years() ||
            type == DurationFieldType.months() ||
            type == DurationFieldType.days()) {
            YearMonthDay ymdStart = new YearMonthDay(start, isoUTC);
            YearMonthDay ymdEnd = new YearMonthDay(end, isoUTC);
            assertEquals(ymdEnd, ymdStart.withFieldAdded(type, amt));
            assertEquals(ymdStart, ymdEnd.withFieldAdded(type, -amt));
        }
    }

    //-----------------------------------------------------------------------
    public void testTimeOfDayAdd() {
        TimeOfDay start = new TimeOfDay(12, 30);
        TimeOfDay end = new TimeOfDay(10, 30);
        
        // Test hour addition across day boundary
        assertEquals(end, start.plusHours(22));
        assertEquals(start, end.minusHours(22));
        
        // Test minute addition across day boundary
        assertEquals(end, start.plusMinutes(22 * 60));
        assertEquals(start, end.minusMinutes(22 * 60));
    }

    public void testPartialDayOfYearAdd() {
        // Represents the last day of a leap year (2000-12-31)
        Partial start = new Partial()
            .with(DateTimeFieldType.year(), 2000)
            .with(DateTimeFieldType.dayOfYear(), 366);
        
        // Represents the last day of a leap year 4 years later (2004-12-31)
        Partial end = new Partial()
            .with(DateTimeFieldType.year(), 2004)
            .with(DateTimeFieldType.dayOfYear(), 366);
        
        // Total days between: 365 (2001) + 365 (2002) + 365 (2003) + 366 (2004)
        int totalDays = 365 + 365 + 365 + 366;
        assertEquals(end, start.withFieldAdded(DurationFieldType.days(), totalDays));
        assertEquals(start, end.withFieldAdded(DurationFieldType.days(), -totalDays));
    }

    //-----------------------------------------------------------------------
    public void testMaximumValue() {
        // Test that maximum values remain consistent across the Gregorian cutover
        DateMidnight dt = new DateMidnight(1570, 1, 1);
        while (dt.getYear() < 1590) {
            dt = dt.plusDays(1);
            YearMonthDay ymd = dt.toYearMonthDay();
            assertEquals(dt.year().getMaximumValue(), ymd.year().getMaximumValue());
            assertEquals(dt.monthOfYear().getMaximumValue(), ymd.monthOfYear().getMaximumValue());
            assertEquals(dt.dayOfMonth().getMaximumValue(), ymd.dayOfMonth().getMaximumValue());
        }
    }

    //-----------------------------------------------------------------------
    public void testLeap_28feb() {
        Chronology chrono = ISOChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 28, 0, 0, chrono);
        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap()); // February in a leap year
        assertEquals(false, dt.dayOfMonth().isLeap());
        assertEquals(false, dt.dayOfYear().isLeap());
    }

    public void testLeap_29feb() {
        Chronology chrono = ISOChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 29, 0, 0, chrono);
        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap()); // February in a leap year
        assertEquals(true, dt.dayOfMonth().isLeap());   // Leap day
        assertEquals(true, dt.dayOfYear().isLeap());    // Day 60 (leap day)
    }

}