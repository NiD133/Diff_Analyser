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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.TimeZone;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is a Junit unit test for ISOChronology.
 *
 * @author Stephen Colebourne
 */
@SuppressWarnings("deprecation")
public class TestISOChronology {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Represents the instant 2002-06-09T00:00:00Z
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

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

    //-----------------------------------------------------------------------
    @Test
    public void getInstanceUTC_shouldReturnChronologyInUTC() {
        assertEquals(DateTimeZone.UTC, ISOChronology.getInstanceUTC().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstanceUTC().getClass());
    }

    @Test
    public void getInstance_shouldReturnChronologyInDefaultZone() {
        assertEquals(LONDON, ISOChronology.getInstance().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance().getClass());
    }

    @Test
    public void getInstance_withZone_shouldReturnChronologyInSpecifiedZone() {
        assertEquals(TOKYO, ISOChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, ISOChronology.getInstance(PARIS).getZone());
        // Should return default zone if null is passed
        assertEquals(LONDON, ISOChronology.getInstance(null).getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    @Test
    public void getInstance_forSameZone_shouldReturnSameInstance() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(PARIS));
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC());
        assertSame(ISOChronology.getInstance(), ISOChronology.getInstance(LONDON));
    }

    @Test
    public void withUTC_shouldReturnUTCChronology() {
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(LONDON).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(TOKYO).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC().withUTC());
        assertSame(ISOChronology.getInstance(), ISOChronology.getInstance(LONDON));
    }

    @Test
    public void withZone_shouldReturnChronologyInSpecifiedZone() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(TOKYO).withZone(PARIS));
        // Should return default zone if null is passed
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(null));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance().withZone(PARIS));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void toString_shouldReturnChronologyNameAndZone() {
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance(LONDON).toString());
        assertEquals("ISOChronology[Asia/Tokyo]", ISOChronology.getInstance(TOKYO).toString());
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance().toString());
        assertEquals("ISOChronology[UTC]", ISOChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    @Test
    public void durationFields_shouldHaveCorrectNamesAndSupport() {
        final Chronology iso = ISOChronology.getInstance();
        assertEquals("eras", iso.eras().getName());
        assertEquals("centuries", iso.centuries().getName());
        assertEquals("years", iso.years().getName());
        assertEquals("weekyears", iso.weekyears().getName());
        assertEquals("months", iso.months().getName());
        assertEquals("weeks", iso.weeks().getName());
        assertEquals("days", iso.days().getName());
        assertEquals("halfdays", iso.halfdays().getName());
        assertEquals("hours", iso.hours().getName());
        assertEquals("minutes", iso.minutes().getName());
        assertEquals("seconds", iso.seconds().getName());
        assertEquals("millis", iso.millis().getName());

        assertFalse(iso.eras().isSupported());
        assertTrue(iso.centuries().isSupported());
        assertTrue(iso.years().isSupported());
        assertTrue(iso.weekyears().isSupported());
        assertTrue(iso.months().isSupported());
        assertTrue(iso.weeks().isSupported());
        assertTrue(iso.days().isSupported());
        assertTrue(iso.halfdays().isSupported());
        assertTrue(iso.hours().isSupported());
        assertTrue(iso.minutes().isSupported());
        assertTrue(iso.seconds().isSupported());
        assertTrue(iso.millis().isSupported());
    }

    @Test
    public void durationFields_shouldHaveCorrectPrecision() {
        // For a zone with DST, larger fields are imprecise
        final Chronology isoLondon = ISOChronology.getInstance(LONDON);
        assertFalse(isoLondon.centuries().isPrecise());
        assertFalse(isoLondon.years().isPrecise());
        assertFalse(isoLondon.weekyears().isPrecise());
        assertFalse(isoLondon.months().isPrecise());
        assertFalse(isoLondon.weeks().isPrecise());
        assertFalse(isoLondon.days().isPrecise());
        assertFalse(isoLondon.halfdays().isPrecise());
        assertTrue(isoLondon.hours().isPrecise());
        assertTrue(isoLondon.minutes().isPrecise());
        assertTrue(isoLondon.seconds().isPrecise());
        assertTrue(isoLondon.millis().isPrecise());

        // For a fixed-offset zone (UTC), date-based fields are precise
        final Chronology isoUTC = ISOChronology.getInstanceUTC();
        assertFalse(isoUTC.centuries().isPrecise());
        assertFalse(isoUTC.years().isPrecise());
        assertFalse(isoUTC.weekyears().isPrecise());
        assertFalse(isoUTC.months().isPrecise());
        assertTrue(isoUTC.weeks().isPrecise());
        assertTrue(isoUTC.days().isPrecise());
        assertTrue(isoUTC.halfdays().isPrecise());
        assertTrue(isoUTC.hours().isPrecise());
        assertTrue(isoUTC.minutes().isPrecise());
        assertTrue(isoUTC.seconds().isPrecise());
        assertTrue(isoUTC.millis().isPrecise());
    }

    @Test
    public void dateFields_shouldHaveCorrectNamesAndSupport() {
        final Chronology iso = ISOChronology.getInstance();
        assertEquals("era", iso.era().getName());
        assertEquals("centuryOfEra", iso.centuryOfEra().getName());
        assertEquals("yearOfCentury", iso.yearOfCentury().getName());
        assertEquals("yearOfEra", iso.yearOfEra().getName());
        assertEquals("year", iso.year().getName());
        assertEquals("monthOfYear", iso.monthOfYear().getName());
        assertEquals("weekyearOfCentury", iso.weekyearOfCentury().getName());
        assertEquals("weekyear", iso.weekyear().getName());
        assertEquals("weekOfWeekyear", iso.weekOfWeekyear().getName());
        assertEquals("dayOfYear", iso.dayOfYear().getName());
        assertEquals("dayOfMonth", iso.dayOfMonth().getName());
        assertEquals("dayOfWeek", iso.dayOfWeek().getName());

        assertTrue(iso.era().isSupported());
        assertTrue(iso.centuryOfEra().isSupported());
        assertTrue(iso.yearOfCentury().isSupported());
        assertTrue(iso.yearOfEra().isSupported());
        assertTrue(iso.year().isSupported());
        assertTrue(iso.monthOfYear().isSupported());
        assertTrue(iso.weekyearOfCentury().isSupported());
        assertTrue(iso.weekyear().isSupported());
        assertTrue(iso.weekOfWeekyear().isSupported());
        assertTrue(iso.dayOfYear().isSupported());
        assertTrue(iso.dayOfMonth().isSupported());
        assertTrue(iso.dayOfWeek().isSupported());
    }

    @Test
    public void dateFields_shouldHaveCorrectDurationAndRangeFields() {
        final Chronology iso = ISOChronology.getInstance();
        assertEquals(iso.eras(), iso.era().getDurationField());
        assertEquals(iso.centuries(), iso.centuryOfEra().getDurationField());
        assertEquals(iso.years(), iso.yearOfCentury().getDurationField());
        assertEquals(iso.years(), iso.yearOfEra().getDurationField());
        assertEquals(iso.years(), iso.year().getDurationField());
        assertEquals(iso.months(), iso.monthOfYear().getDurationField());
        assertEquals(iso.weekyears(), iso.weekyearOfCentury().getDurationField());
        assertEquals(iso.weekyears(), iso.weekyear().getDurationField());
        assertEquals(iso.weeks(), iso.weekOfWeekyear().getDurationField());
        assertEquals(iso.days(), iso.dayOfYear().getDurationField());
        assertEquals(iso.days(), iso.dayOfMonth().getDurationField());
        assertEquals(iso.days(), iso.dayOfWeek().getDurationField());

        assertEquals(null, iso.era().getRangeDurationField());
        assertEquals(iso.eras(), iso.centuryOfEra().getRangeDurationField());
        assertEquals(iso.centuries(), iso.yearOfCentury().getRangeDurationField());
        assertEquals(iso.eras(), iso.yearOfEra().getRangeDurationField());
        assertEquals(null, iso.year().getRangeDurationField());
        assertEquals(iso.years(), iso.monthOfYear().getRangeDurationField());
        assertEquals(iso.centuries(), iso.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, iso.weekyear().getRangeDurationField());
        assertEquals(iso.weekyears(), iso.weekOfWeekyear().getRangeDurationField());
        assertEquals(iso.years(), iso.dayOfYear().getRangeDurationField());
        assertEquals(iso.months(), iso.dayOfMonth().getRangeDurationField());
        assertEquals(iso.weeks(), iso.dayOfWeek().getRangeDurationField());
    }

    @Test
    public void timeFields_shouldHaveCorrectNamesAndSupport() {
        final Chronology iso = ISOChronology.getInstance();
        assertEquals("halfdayOfDay", iso.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", iso.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", iso.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", iso.clockhourOfDay().getName());
        assertEquals("hourOfDay", iso.hourOfDay().getName());
        assertEquals("minuteOfDay", iso.minuteOfDay().getName());
        assertEquals("minuteOfHour", iso.minuteOfHour().getName());
        assertEquals("secondOfDay", iso.secondOfDay().getName());
        assertEquals("secondOfMinute", iso.secondOfMinute().getName());
        assertEquals("millisOfDay", iso.millisOfDay().getName());
        assertEquals("millisOfSecond", iso.millisOfSecond().getName());

        assertTrue(iso.halfdayOfDay().isSupported());
        assertTrue(iso.clockhourOfHalfday().isSupported());
        assertTrue(iso.hourOfHalfday().isSupported());
        assertTrue(iso.clockhourOfDay().isSupported());
        assertTrue(iso.hourOfDay().isSupported());
        assertTrue(iso.minuteOfDay().isSupported());
        assertTrue(iso.minuteOfHour().isSupported());
        assertTrue(iso.secondOfDay().isSupported());
        assertTrue(iso.secondOfMinute().isSupported());
        assertTrue(iso.millisOfDay().isSupported());
        assertTrue(iso.millisOfSecond().isSupported());
    }

    @Test
    public void maxYear_shouldBeHandledCorrectly() {
        final Chronology chrono = ISOChronology.getInstanceUTC();
        final int maxYear = chrono.year().getMaximumValue();

        DateTime start = new DateTime(maxYear, 1, 1, 0, 0, 0, 0, chrono);
        DateTime end = new DateTime(maxYear, 12, 31, 23, 59, 59, 999, chrono);

        assertTrue("Start millis should be positive", start.getMillis() > 0);
        assertTrue("End millis should be after start millis", end.getMillis() > start.getMillis());
        assertEquals(maxYear, start.getYear());
        assertEquals(maxYear, end.getYear());

        long expectedDelta = (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals(expectedDelta, end.getMillis() - start.getMillis());

        assertEquals(start, new DateTime(maxYear + "-01-01T00:00:00.000Z", chrono));
        assertEquals(end, new DateTime(maxYear + "-12-31T23:59:59.999Z", chrono));

        try {
            start.plusYears(1);
            fail("Adding a year to the maximum year should fail");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void minYear_shouldBeHandledCorrectly() {
        final Chronology chrono = ISOChronology.getInstanceUTC();
        final int minYear = chrono.year().getMinimumValue();

        DateTime start = new DateTime(minYear, 1, 1, 0, 0, 0, 0, chrono);
        DateTime end = new DateTime(minYear, 12, 31, 23, 59, 59, 999, chrono);

        assertTrue("Start millis should be negative", start.getMillis() < 0);
        assertTrue("End millis should be after start millis", end.getMillis() > start.getMillis());
        assertEquals(minYear, start.getYear());
        assertEquals(minYear, end.getYear());

        long expectedDelta = (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals(expectedDelta, end.getMillis() - start.getMillis());

        assertEquals(start, new DateTime(minYear + "-01-01T00:00:00.000Z", chrono));
        assertEquals(end, new DateTime(minYear + "-12-31T23:59:59.999Z", chrono));

        try {
            start.minusYears(1);
            fail("Subtracting a year from the minimum year should fail");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void addYears_across1582_shouldBehaveLinearly() {
        assertAddition("1582-01-01", DurationFieldType.years(), 1, "1583-01-01");
        assertAddition("1582-02-15", DurationFieldType.years(), 1, "1583-02-15");
        assertAddition("1582-10-04", DurationFieldType.years(), 1, "1583-10-04");
        assertAddition("1582-10-15", DurationFieldType.years(), 1, "1583-10-15");
        assertAddition("1580-02-29", DurationFieldType.years(), 4, "1584-02-29");
        assertAddition("1580-12-31", DurationFieldType.years(), 4, "1584-12-31");
    }

    @Test
    public void addMonths_across1582_shouldBehaveLinearly() {
        assertAddition("1582-01-01", DurationFieldType.months(), 6, "1582-07-01");
        assertAddition("1582-09-04", DurationFieldType.months(), 2, "1582-11-04");
        assertAddition("1582-09-15", DurationFieldType.months(), 2, "1582-11-15");
        assertAddition("1580-02-29", DurationFieldType.months(), 48, "1584-02-29");
    }

    private void assertAddition(String start, DurationFieldType type, int amount, String end) {
        DateTime dtStart = new DateTime(start, ISOChronology.getInstanceUTC());
        DateTime dtEnd = new DateTime(end, ISOChronology.getInstanceUTC());
        assertEquals(dtEnd, dtStart.withFieldAdded(type, amount));
        assertEquals(dtStart, dtEnd.withFieldAdded(type, -amount));

        DurationField field = type.getField(ISOChronology.getInstanceUTC());
        assertEquals(amount, field.getDifference(dtEnd.getMillis(), dtStart.getMillis()));

        if (type == DurationFieldType.years() || type == DurationFieldType.months() || type == DurationFieldType.days()) {
            YearMonthDay ymdStart = new YearMonthDay(start, ISOChronology.getInstanceUTC());
            YearMonthDay ymdEnd = new YearMonthDay(end, ISOChronology.getInstanceUTC());
            assertEquals(ymdEnd, ymdStart.withFieldAdded(type, amount));
            assertEquals(ymdStart, ymdEnd.withFieldAdded(type, -amount));
        }
    }

    @Test
    public void timeOfDay_add_shouldWrapAround() {
        TimeOfDay start = new TimeOfDay(12, 30);
        TimeOfDay end = new TimeOfDay(10, 30);
        assertEquals(end, start.plusHours(22));
        assertEquals(start, end.minusHours(22));
        assertEquals(end, start.plusMinutes(22 * 60));
        assertEquals(start, end.minusMinutes(22 * 60));
    }

    @Test
    public void partial_dayOfYear_add_shouldHandleLeapYears() {
        Partial start = new Partial().with(DateTimeFieldType.year(), 2000).with(DateTimeFieldType.dayOfYear(), 366);
        Partial end = new Partial().with(DateTimeFieldType.year(), 2004).with(DateTimeFieldType.dayOfYear(), 366);
        // Add days for 2001, 2002, 2003, 2004 (leap)
        assertEquals(end, start.withFieldAdded(DurationFieldType.days(), 365 + 365 + 365 + 366));
        assertEquals(start, end.withFieldAdded(DurationFieldType.days(), -(365 + 365 + 365 + 366)));
    }

    @Test
    public void maximumValue_forDateFields_shouldBeConsistent() {
        DateMidnight dt = new DateMidnight(1582, 1, 1);
        // Check dates around the Gregorian cutover year to ensure consistency
        while (dt.getYear() < 1584) {
            dt = dt.plusDays(1);
            YearMonthDay ymd = dt.toYearMonthDay();
            assertEquals(dt.year().getMaximumValue(), ymd.year().getMaximumValue());
            assertEquals(dt.monthOfYear().getMaximumValue(), ymd.monthOfYear().getMaximumValue());
            assertEquals(dt.dayOfMonth().getMaximumValue(), ymd.dayOfMonth().getMaximumValue());
        }
    }

    @Test
    public void isLeap_onFeb28_inLeapYear() {
        Chronology chrono = ISOChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 28, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    @Test
    public void isLeap_onFeb29_inLeapYear() {
        Chronology chrono = ISOChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 29, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }
}