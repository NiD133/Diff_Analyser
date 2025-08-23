package org.joda.time.chrono;

import static org.junit.Assert.*;

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
 * Readable, behavior-focused tests for ISOChronology.
 *
 * Notes:
 * - Tests use a fixed "now" at 2002-06-09T00:00Z to avoid time-dependent behavior.
 * - Defaults are set to Europe/London to exercise DST-sensitive behavior where relevant.
 * - Helper methods (dtUTC, ymdUTC) centralize UTC parsing to keep assertions concise.
 */
@SuppressWarnings("deprecation")
public class TestISOChronology {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Fixed "now": 2002-06-09T00:00:00.000Z
    private static final long FIXED_NOW_UTC =
            new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(FIXED_NOW_UTC);

        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);

        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    // Factories and basic identity

    @Test
    public void getInstanceUTC_returnsUtcSingleton() {
        assertEquals(DateTimeZone.UTC, ISOChronology.getInstanceUTC().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstanceUTC().getClass());
    }

    @Test
    public void getInstance_usesDefaultTimeZone() {
        assertEquals(LONDON, ISOChronology.getInstance().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance().getClass());
    }

    @Test
    public void getInstance_withZoneHonorsInputAndNullMeansDefault() {
        assertEquals(TOKYO, ISOChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, ISOChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, ISOChronology.getInstance(null).getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance(TOKYO).getClass());
    }

    @Test
    public void equality_singletonPerZone() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(PARIS));
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC());
        assertSame(ISOChronology.getInstance(), ISOChronology.getInstance(LONDON));
    }

    @Test
    public void withUTC_returnsUtcInstanceFromAnyZone() {
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(LONDON).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(TOKYO).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC().withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance().withUTC());
    }

    @Test
    public void withZone_changesZoneAndTreatsNullAsDefault() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(null));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance().withZone(PARIS));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void toString_includesZone() {
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance(LONDON).toString());
        assertEquals("ISOChronology[Asia/Tokyo]", ISOChronology.getInstance(TOKYO).toString());
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance().toString());
        assertEquals("ISOChronology[UTC]", ISOChronology.getInstanceUTC().toString());
    }

    // Duration fields

    @Test
    public void durationFields_namesAndSupport() {
        final ISOChronology iso = ISOChronology.getInstance();

        // Names
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

        // Support
        assertFalse(iso.eras().isSupported()); // Not supported in ISO chronology
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
    public void durationFields_precision_variesByZone_dueToDst() {
        final ISOChronology isoLondon = ISOChronology.getInstance(LONDON);
        // In a DST zone, only time-based (sub-day) fields are precise.
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

        final ISOChronology isoUTC = ISOChronology.getInstanceUTC();
        // In UTC, day/halfdays/weeks are precise.
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

        final ISOChronology isoGMT = ISOChronology.getInstance(DateTimeZone.forID("Etc/GMT"));
        assertFalse(isoGMT.centuries().isPrecise());
        assertFalse(isoGMT.years().isPrecise());
        assertFalse(isoGMT.weekyears().isPrecise());
        assertFalse(isoGMT.months().isPrecise());
        assertTrue(isoGMT.weeks().isPrecise());
        assertTrue(isoGMT.days().isPrecise());
        assertTrue(isoGMT.halfdays().isPrecise());
        assertTrue(isoGMT.hours().isPrecise());
        assertTrue(isoGMT.minutes().isPrecise());
        assertTrue(isoGMT.seconds().isPrecise());
        assertTrue(isoGMT.millis().isPrecise());

        final ISOChronology isoOffset1 = ISOChronology.getInstance(DateTimeZone.forOffsetHours(1));
        assertFalse(isoOffset1.centuries().isPrecise());
        assertFalse(isoOffset1.years().isPrecise());
        assertFalse(isoOffset1.weekyears().isPrecise());
        assertFalse(isoOffset1.months().isPrecise());
        assertTrue(isoOffset1.weeks().isPrecise());
        assertTrue(isoOffset1.days().isPrecise());
        assertTrue(isoOffset1.halfdays().isPrecise());
        assertTrue(isoOffset1.hours().isPrecise());
        assertTrue(isoOffset1.minutes().isPrecise());
        assertTrue(isoOffset1.seconds().isPrecise());
        assertTrue(isoOffset1.millis().isPrecise());
    }

    // Date fields

    @Test
    public void dateFields_namesSupportDurationsAndRanges() {
        final ISOChronology iso = ISOChronology.getInstance();

        // Names
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

        // Support
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

        // Duration fields used by date fields
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

        // Range duration fields
        assertNull(iso.era().getRangeDurationField());
        assertEquals(iso.eras(), iso.centuryOfEra().getRangeDurationField());
        assertEquals(iso.centuries(), iso.yearOfCentury().getRangeDurationField());
        assertEquals(iso.eras(), iso.yearOfEra().getRangeDurationField());
        assertNull(iso.year().getRangeDurationField());
        assertEquals(iso.years(), iso.monthOfYear().getRangeDurationField());
        assertEquals(iso.centuries(), iso.weekyearOfCentury().getRangeDurationField());
        assertNull(iso.weekyear().getRangeDurationField());
        assertEquals(iso.weekyears(), iso.weekOfWeekyear().getRangeDurationField());
        assertEquals(iso.years(), iso.dayOfYear().getRangeDurationField());
        assertEquals(iso.months(), iso.dayOfMonth().getRangeDurationField());
        assertEquals(iso.weeks(), iso.dayOfWeek().getRangeDurationField());
    }

    // Time fields

    @Test
    public void timeFields_namesAndSupport() {
        final ISOChronology iso = ISOChronology.getInstance();

        // Names
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

        // Support
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

    // Year bounds

    @Test
    public void maxYear_boundsAndParsing() {
        final ISOChronology chrono = ISOChronology.getInstanceUTC();
        final int maxYear = chrono.year().getMaximumValue();

        DateTime start = new DateTime(maxYear, 1, 1, 0, 0, 0, 0, chrono);
        DateTime end = new DateTime(maxYear, 12, 31, 23, 59, 59, 999, chrono);

        assertTrue(start.getMillis() > 0);
        assertTrue(end.getMillis() > start.getMillis());
        assertEquals(maxYear, start.getYear());
        assertEquals(maxYear, end.getYear());

        long delta = end.getMillis() - start.getMillis();
        long expectedDelta =
                (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals(expectedDelta, delta);

        assertEquals(start, dtUTC(maxYear + "-01-01T00:00:00.000Z"));
        assertEquals(end, dtUTC(maxYear + "-12-31T23:59:59.999Z"));

        try {
            start.plusYears(1);
            fail("Expected IllegalFieldValueException when adding a year past maxYear");
        } catch (IllegalFieldValueException expected) {
            // expected
        }

        try {
            end.plusYears(1);
            fail("Expected IllegalFieldValueException when adding a year past maxYear");
        } catch (IllegalFieldValueException expected) {
            // expected
        }

        assertEquals(maxYear + 1, chrono.year().get(Long.MAX_VALUE));
    }

    @Test
    public void minYear_boundsAndParsing() {
        final ISOChronology chrono = ISOChronology.getInstanceUTC();
        final int minYear = chrono.year().getMinimumValue();

        DateTime start = new DateTime(minYear, 1, 1, 0, 0, 0, 0, chrono);
        DateTime end = new DateTime(minYear, 12, 31, 23, 59, 59, 999, chrono);

        assertTrue(start.getMillis() < 0);
        assertTrue(end.getMillis() > start.getMillis());
        assertEquals(minYear, start.getYear());
        assertEquals(minYear, end.getYear());

        long delta = end.getMillis() - start.getMillis();
        long expectedDelta =
                (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals(expectedDelta, delta);

        assertEquals(start, dtUTC(minYear + "-01-01T00:00:00.000Z"));
        assertEquals(end, dtUTC(minYear + "-12-31T23:59:59.999Z"));

        try {
            start.minusYears(1);
            fail("Expected IllegalFieldValueException when subtracting a year past minYear");
        } catch (IllegalFieldValueException expected) {
            // expected
        }

        try {
            end.minusYears(1);
            fail("Expected IllegalFieldValueException when subtracting a year past minYear");
        } catch (IllegalFieldValueException expected) {
            // expected
        }

        assertEquals(minYear - 1, chrono.year().get(Long.MIN_VALUE));
    }

    // Field addition around Gregorian cutover

    @Test
    public void addYears_aroundGregorianCutover() {
        // Focused checks around 1582-10 cutover, including leap-year scenarios
        assertAdd("1582-01-01", DurationFieldType.years(), 1, "1583-01-01");
        assertAdd("1582-02-15", DurationFieldType.years(), 1, "1583-02-15");
        assertAdd("1582-02-28", DurationFieldType.years(), 1, "1583-02-28");
        assertAdd("1582-03-01", DurationFieldType.years(), 1, "1583-03-01");
        assertAdd("1582-09-30", DurationFieldType.years(), 1, "1583-09-30");
        assertAdd("1582-10-01", DurationFieldType.years(), 1, "1583-10-01");
        assertAdd("1582-10-04", DurationFieldType.years(), 1, "1583-10-04");
        assertAdd("1582-10-15", DurationFieldType.years(), 1, "1583-10-15");
        assertAdd("1582-10-16", DurationFieldType.years(), 1, "1583-10-16");

        // Leap-year continuity
        assertAdd("1580-01-01", DurationFieldType.years(), 4, "1584-01-01");
        assertAdd("1580-02-29", DurationFieldType.years(), 4, "1584-02-29");
        assertAdd("1580-10-01", DurationFieldType.years(), 4, "1584-10-01");
        assertAdd("1580-10-10", DurationFieldType.years(), 4, "1584-10-10");
        assertAdd("1580-10-15", DurationFieldType.years(), 4, "1584-10-15");
        assertAdd("1580-12-31", DurationFieldType.years(), 4, "1584-12-31");
    }

    @Test
    public void addMonths_aroundGregorianCutover() {
        assertAdd("1582-01-01", DurationFieldType.months(), 1, "1582-02-01");
        assertAdd("1582-01-01", DurationFieldType.months(), 6, "1582-07-01");
        assertAdd("1582-01-01", DurationFieldType.months(), 12, "1583-01-01");
        assertAdd("1582-11-15", DurationFieldType.months(), 1, "1582-12-15");
        assertAdd("1582-09-04", DurationFieldType.months(), 2, "1582-11-04");
        assertAdd("1582-09-05", DurationFieldType.months(), 2, "1582-11-05");
        assertAdd("1582-09-10", DurationFieldType.months(), 2, "1582-11-10");
        assertAdd("1582-09-15", DurationFieldType.months(), 2, "1582-11-15");

        // 48 months = 4 years consistency
        assertAdd("1580-01-01", DurationFieldType.months(), 48, "1584-01-01");
        assertAdd("1580-02-29", DurationFieldType.months(), 48, "1584-02-29");
        assertAdd("1580-10-01", DurationFieldType.months(), 48, "1584-10-01");
        assertAdd("1580-10-10", DurationFieldType.months(), 48, "1584-10-10");
        assertAdd("1580-10-15", DurationFieldType.months(), 48, "1584-10-15");
        assertAdd("1580-12-31", DurationFieldType.months(), 48, "1584-12-31");
    }

    private void assertAdd(String startIsoDate, DurationFieldType type, int amount, String expectedIsoDate) {
        DateTime start = dtUTC(startIsoDate);
        DateTime expected = dtUTC(expectedIsoDate);

        assertEquals("forward add", expected, start.withFieldAdded(type, amount));
        assertEquals("reverse add", start, expected.withFieldAdded(type, -amount));

        DurationField field = type.getField(ISOChronology.getInstanceUTC());
        int diff = field.getDifference(expected.getMillis(), start.getMillis());
        assertEquals("difference", amount, diff);

        if (type == DurationFieldType.years()
                || type == DurationFieldType.months()
                || type == DurationFieldType.days()) {
            YearMonthDay ymdStart = ymdUTC(startIsoDate);
            YearMonthDay ymdExpected = ymdUTC(expectedIsoDate);
            assertEquals("YMD forward add", ymdExpected, ymdStart.withFieldAdded(type, amount));
            assertEquals("YMD reverse add", ymdStart, ymdExpected.withFieldAdded(type, -amount));
        }
    }

    // Partial/time-only behavior

    @Test
    public void timeOfDay_addWrapsWithinDay() {
        TimeOfDay start = new TimeOfDay(12, 30);
        TimeOfDay end = new TimeOfDay(10, 30);

        assertEquals(end, start.plusHours(22));
        assertEquals(start, end.minusHours(22));
        assertEquals(end, start.plusMinutes(22 * 60));
        assertEquals(start, end.minusMinutes(22 * 60));
    }

    @Test
    public void partial_dayOfYear_addOverLeapCycle() {
        Partial start = new Partial()
                .with(DateTimeFieldType.year(), 2000)
                .with(DateTimeFieldType.dayOfYear(), 366);
        Partial end = new Partial()
                .with(DateTimeFieldType.year(), 2004)
                .with(DateTimeFieldType.dayOfYear(), 366);

        int days = 365 + 365 + 365 + 366;
        assertEquals(end, start.withFieldAdded(DurationFieldType.days(), days));
        assertEquals(start, end.withFieldAdded(DurationFieldType.days(), -days));
    }

    // Value maxima checks

    @Test
    public void maximumValue_yearMonthDayConsistentWithDateMidnight() {
        DateMidnight dm = new DateMidnight(1570, 1, 1);
        while (dm.getYear() < 1590) {
            dm = dm.plusDays(1);
            YearMonthDay ymd = dm.toYearMonthDay();

            assertEquals(dm.year().getMaximumValue(), ymd.year().getMaximumValue());
            assertEquals(dm.monthOfYear().getMaximumValue(), ymd.monthOfYear().getMaximumValue());
            assertEquals(dm.dayOfMonth().getMaximumValue(), ymd.dayOfMonth().getMaximumValue());
        }
    }

    // Leap flags on interesting dates

    @Test
    public void leapFlags_onFeb28() {
        Chronology chrono = ISOChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 28, 0, 0, chrono);

        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    @Test
    public void leapFlags_onFeb29() {
        Chronology chrono = ISOChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 29, 0, 0, chrono);

        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }

    // Helpers

    private static DateTime dtUTC(String isoDateOrInstant) {
        return new DateTime(isoDateOrInstant, ISOChronology.getInstanceUTC());
    }

    private static YearMonthDay ymdUTC(String isoDate) {
        return new YearMonthDay(isoDate, ISOChronology.getInstanceUTC());
    }
}