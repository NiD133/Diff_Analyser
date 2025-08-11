package org.joda.time.chrono;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for IslamicChronology.
 * 
 * Notes:
 * - Uses JUnit 4.
 * - Keeps semantics consistent with original tests.
 * - Introduces helpers and clearer structure for readability.
 * - The slow "calendar sweep" test is opt-in via -DrunSlowTests=true.
 */
public class TestIslamicChronology {

    private static final DateTimeZone ZONE_PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone ZONE_LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone ZONE_TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final DateTimeZone ZONE_GMT = DateTimeZone.forID("Etc/GMT");

    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    private static final long ONE_DAY_MILLIS = DateTimeConstants.MILLIS_PER_DAY;
    private static final boolean RUN_SLOW_TESTS = Boolean.getBoolean("runSlowTests");

    // Use direct construction for clarity, rather than manual day counting.
    private static final long FIXED_NOW_MILLIS = new DateTime(2002, 6, 9, 0, 0, 0, 0, ISO_UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(FIXED_NOW_MILLIS);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        DateTimeZone.setDefault(ZONE_LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    // ---------------------------------------------------------------------
    // Factories and identity
    // ---------------------------------------------------------------------

    @Test
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, IslamicChronology.getInstanceUTC().getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstanceUTC().getClass());
    }

    @Test
    public void testFactoryDefaultZone() {
        assertEquals(ZONE_LONDON, IslamicChronology.getInstance().getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstance().getClass());
    }

    @Test
    public void testFactoryWithZone() {
        assertEquals(ZONE_TOKYO, IslamicChronology.getInstance(ZONE_TOKYO).getZone());
        assertEquals(ZONE_PARIS, IslamicChronology.getInstance(ZONE_PARIS).getZone());
        assertEquals(ZONE_LONDON, IslamicChronology.getInstance(null).getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstance(ZONE_TOKYO).getClass());
    }

    @Test
    public void testEqualityAndCaching() {
        assertSame(IslamicChronology.getInstance(ZONE_TOKYO), IslamicChronology.getInstance(ZONE_TOKYO));
        assertSame(IslamicChronology.getInstance(ZONE_LONDON), IslamicChronology.getInstance(ZONE_LONDON));
        assertSame(IslamicChronology.getInstance(ZONE_PARIS), IslamicChronology.getInstance(ZONE_PARIS));
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());
        assertSame(IslamicChronology.getInstance(), IslamicChronology.getInstance(ZONE_LONDON));
    }

    @Test
    public void testWithUTC() {
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(ZONE_LONDON).withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(ZONE_TOKYO).withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC().withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance().withUTC());
    }

    @Test
    public void testWithZone() {
        assertSame(IslamicChronology.getInstance(ZONE_TOKYO), IslamicChronology.getInstance(ZONE_TOKYO).withZone(ZONE_TOKYO));
        assertSame(IslamicChronology.getInstance(ZONE_LONDON), IslamicChronology.getInstance(ZONE_TOKYO).withZone(ZONE_LONDON));
        assertSame(IslamicChronology.getInstance(ZONE_PARIS), IslamicChronology.getInstance(ZONE_TOKYO).withZone(ZONE_PARIS));
        assertSame(IslamicChronology.getInstance(ZONE_LONDON), IslamicChronology.getInstance(ZONE_TOKYO).withZone(null));
        assertSame(IslamicChronology.getInstance(ZONE_PARIS), IslamicChronology.getInstance().withZone(ZONE_PARIS));
        assertSame(IslamicChronology.getInstance(ZONE_PARIS), IslamicChronology.getInstanceUTC().withZone(ZONE_PARIS));
    }

    @Test
    public void testToString() {
        assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance(ZONE_LONDON).toString());
        assertEquals("IslamicChronology[Asia/Tokyo]", IslamicChronology.getInstance(ZONE_TOKYO).toString());
        assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance().toString());
        assertEquals("IslamicChronology[UTC]", IslamicChronology.getInstanceUTC().toString());
    }

    // ---------------------------------------------------------------------
    // Fields: duration, date, time
    // ---------------------------------------------------------------------

    @Test
    public void testDurationFields() {
        final IslamicChronology c = IslamicChronology.getInstance();

        // Names are as expected
        assertEquals("eras", c.eras().getName());
        assertEquals("centuries", c.centuries().getName());
        assertEquals("years", c.years().getName());
        assertEquals("weekyears", c.weekyears().getName());
        assertEquals("months", c.months().getName());
        assertEquals("weeks", c.weeks().getName());
        assertEquals("days", c.days().getName());
        assertEquals("halfdays", c.halfdays().getName());
        assertEquals("hours", c.hours().getName());
        assertEquals("minutes", c.minutes().getName());
        assertEquals("seconds", c.seconds().getName());
        assertEquals("millis", c.millis().getName());

        // Support
        assertFalse(c.eras().isSupported());
        assertTrue(c.centuries().isSupported());
        assertTrue(c.years().isSupported());
        assertTrue(c.weekyears().isSupported());
        assertTrue(c.months().isSupported());
        assertTrue(c.weeks().isSupported());
        assertTrue(c.days().isSupported());
        assertTrue(c.halfdays().isSupported());
        assertTrue(c.hours().isSupported());
        assertTrue(c.minutes().isSupported());
        assertTrue(c.seconds().isSupported());
        assertTrue(c.millis().isSupported());

        // Precision in default zone (non-UTC)
        assertFalse(c.centuries().isPrecise());
        assertFalse(c.years().isPrecise());
        assertFalse(c.weekyears().isPrecise());
        assertFalse(c.months().isPrecise());
        assertFalse(c.weeks().isPrecise());
        assertFalse(c.days().isPrecise());
        assertFalse(c.halfdays().isPrecise());
        assertTrue(c.hours().isPrecise());
        assertTrue(c.minutes().isPrecise());
        assertTrue(c.seconds().isPrecise());
        assertTrue(c.millis().isPrecise());

        // Precision in UTC/GMT
        final IslamicChronology cUTC = IslamicChronology.getInstanceUTC();
        final IslamicChronology cGMT = IslamicChronology.getInstance(ZONE_GMT);

        for (IslamicChronology chrono : new IslamicChronology[] {cUTC, cGMT}) {
            assertFalse(chrono.centuries().isPrecise());
            assertFalse(chrono.years().isPrecise());
            assertFalse(chrono.weekyears().isPrecise());
            assertFalse(chrono.months().isPrecise());
            assertTrue(chrono.weeks().isPrecise());
            assertTrue(chrono.days().isPrecise());
            assertTrue(chrono.halfdays().isPrecise());
            assertTrue(chrono.hours().isPrecise());
            assertTrue(chrono.minutes().isPrecise());
            assertTrue(chrono.seconds().isPrecise());
            assertTrue(chrono.millis().isPrecise());
        }
    }

    @Test
    public void testDateFields() {
        final IslamicChronology c = IslamicChronology.getInstance();

        // Names
        assertEquals("era", c.era().getName());
        assertEquals("centuryOfEra", c.centuryOfEra().getName());
        assertEquals("yearOfCentury", c.yearOfCentury().getName());
        assertEquals("yearOfEra", c.yearOfEra().getName());
        assertEquals("year", c.year().getName());
        assertEquals("monthOfYear", c.monthOfYear().getName());
        assertEquals("weekyearOfCentury", c.weekyearOfCentury().getName());
        assertEquals("weekyear", c.weekyear().getName());
        assertEquals("weekOfWeekyear", c.weekOfWeekyear().getName());
        assertEquals("dayOfYear", c.dayOfYear().getName());
        assertEquals("dayOfMonth", c.dayOfMonth().getName());
        assertEquals("dayOfWeek", c.dayOfWeek().getName());

        // Support
        assertTrue(c.era().isSupported());
        assertTrue(c.centuryOfEra().isSupported());
        assertTrue(c.yearOfCentury().isSupported());
        assertTrue(c.yearOfEra().isSupported());
        assertTrue(c.year().isSupported());
        assertTrue(c.monthOfYear().isSupported());
        assertTrue(c.weekyearOfCentury().isSupported());
        assertTrue(c.weekyear().isSupported());
        assertTrue(c.weekOfWeekyear().isSupported());
        assertTrue(c.dayOfYear().isSupported());
        assertTrue(c.dayOfMonth().isSupported());
        assertTrue(c.dayOfWeek().isSupported());

        // Duration fields
        assertSame(c.eras(), c.era().getDurationField());
        assertSame(c.centuries(), c.centuryOfEra().getDurationField());
        assertSame(c.years(), c.yearOfCentury().getDurationField());
        assertSame(c.years(), c.yearOfEra().getDurationField());
        assertSame(c.years(), c.year().getDurationField());
        assertSame(c.months(), c.monthOfYear().getDurationField());
        assertSame(c.weekyears(), c.weekyearOfCentury().getDurationField());
        assertSame(c.weekyears(), c.weekyear().getDurationField());
        assertSame(c.weeks(), c.weekOfWeekyear().getDurationField());
        assertSame(c.days(), c.dayOfYear().getDurationField());
        assertSame(c.days(), c.dayOfMonth().getDurationField());
        assertSame(c.days(), c.dayOfWeek().getDurationField());

        // Range duration fields
        assertNull(c.era().getRangeDurationField());
        assertSame(c.eras(), c.centuryOfEra().getRangeDurationField());
        assertSame(c.centuries(), c.yearOfCentury().getRangeDurationField());
        assertSame(c.eras(), c.yearOfEra().getRangeDurationField());
        assertNull(c.year().getRangeDurationField());
        assertSame(c.years(), c.monthOfYear().getRangeDurationField());
        assertSame(c.centuries(), c.weekyearOfCentury().getRangeDurationField());
        assertNull(c.weekyear().getRangeDurationField());
        assertSame(c.weekyears(), c.weekOfWeekyear().getRangeDurationField());
        assertSame(c.years(), c.dayOfYear().getRangeDurationField());
        assertSame(c.months(), c.dayOfMonth().getRangeDurationField());
        assertSame(c.weeks(), c.dayOfWeek().getRangeDurationField());
    }

    @Test
    public void testTimeFields() {
        final IslamicChronology c = IslamicChronology.getInstance();

        // Names
        assertEquals("halfdayOfDay", c.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", c.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", c.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", c.clockhourOfDay().getName());
        assertEquals("hourOfDay", c.hourOfDay().getName());
        assertEquals("minuteOfDay", c.minuteOfDay().getName());
        assertEquals("minuteOfHour", c.minuteOfHour().getName());
        assertEquals("secondOfDay", c.secondOfDay().getName());
        assertEquals("secondOfMinute", c.secondOfMinute().getName());
        assertEquals("millisOfDay", c.millisOfDay().getName());
        assertEquals("millisOfSecond", c.millisOfSecond().getName());

        // Support
        assertTrue(c.halfdayOfDay().isSupported());
        assertTrue(c.clockhourOfHalfday().isSupported());
        assertTrue(c.hourOfHalfday().isSupported());
        assertTrue(c.clockhourOfDay().isSupported());
        assertTrue(c.hourOfDay().isSupported());
        assertTrue(c.minuteOfDay().isSupported());
        assertTrue(c.minuteOfHour().isSupported());
        assertTrue(c.secondOfDay().isSupported());
        assertTrue(c.secondOfMinute().isSupported());
        assertTrue(c.millisOfDay().isSupported());
        assertTrue(c.millisOfSecond().isSupported());
    }

    // ---------------------------------------------------------------------
    // Era and epoch alignment
    // ---------------------------------------------------------------------

    @Test
    public void testEpoch() {
        DateTime islamicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime expectedJulian = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC);
        assertEquals(expectedJulian.getMillis(), islamicEpoch.getMillis());
    }

    @Test
    public void testEra() {
        assertEquals(1, IslamicChronology.AH);
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ISLAMIC_UTC);
            fail("Expected IllegalArgumentException for invalid Islamic date");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    // ---------------------------------------------------------------------
    // Conversions and sample dates
    // ---------------------------------------------------------------------

    @Test
    public void testFieldConstructor() {
        // 1364-12-06 AH == 1945-11-12 ISO (UTC)
        DateTime islamic = new DateTime(1364, 12, 6, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime expectedISO = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
        assertEquals(expectedISO.getMillis(), islamic.getMillis());
    }

    @Test
    public void testSampleDate1_ISO1945_11_12_to_Islamic() {
        // ISO -> Islamic
        DateTime dt = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC).withChronology(ISLAMIC_UTC);

        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(14, dt.getCenturyOfEra()); // per original test
        assertEquals(64, dt.getYearOfCentury());
        assertEquals(1364, dt.getYearOfEra());
        assertEquals(1364, dt.getYear());

        Property year = dt.year();
        assertFalse(year.isLeap());
        assertEquals(0, year.getLeapAmount());
        assertEquals(DurationFieldType.days(), getType(year.getLeapDurationField()));
        assertEquals(new DateTime(1365, 12, 6, 0, 0, 0, 0, ISLAMIC_UTC), year.addToCopy(1));

        assertEquals(12, dt.getMonthOfYear());
        Property month = dt.monthOfYear();
        assertFalse(month.isLeap());
        assertEquals(0, month.getLeapAmount());
        assertEquals(DurationFieldType.days(), getType(month.getLeapDurationField()));
        assertEquals(1, month.getMinimumValue());
        assertEquals(1, month.getMinimumValueOverall());
        assertEquals(12, month.getMaximumValue());
        assertEquals(12, month.getMaximumValueOverall());
        assertEquals(new DateTime(1365, 1, 6, 0, 0, 0, 0, ISLAMIC_UTC), month.addToCopy(1));
        assertEquals(new DateTime(1364, 1, 6, 0, 0, 0, 0, ISLAMIC_UTC), month.addWrapFieldToCopy(1));

        assertEquals(6, dt.getDayOfMonth());
        Property day = dt.dayOfMonth();
        assertFalse(day.isLeap());
        assertEquals(0, day.getLeapAmount());
        assertNull(day.getLeapDurationField());
        assertEquals(1, day.getMinimumValue());
        assertEquals(1, day.getMinimumValueOverall());
        assertEquals(29, day.getMaximumValue());
        assertEquals(30, day.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), day.addToCopy(1));

        assertEquals(DateTimeConstants.MONDAY, dt.getDayOfWeek());
        Property dow = dt.dayOfWeek();
        assertFalse(dow.isLeap());
        assertEquals(0, dow.getLeapAmount());
        assertNull(dow.getLeapDurationField());
        assertEquals(1, dow.getMinimumValue());
        assertEquals(1, dow.getMinimumValueOverall());
        assertEquals(7, dow.getMaximumValue());
        assertEquals(7, dow.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), dow.addToCopy(1));

        assertEquals(6 * 30 + 5 * 29 + 6, dt.getDayOfYear());
        Property doy = dt.dayOfYear();
        assertFalse(doy.isLeap());
        assertEquals(0, doy.getLeapAmount());
        assertNull(doy.getLeapDurationField());
        assertEquals(1, doy.getMinimumValue());
        assertEquals(1, doy.getMinimumValueOverall());
        assertEquals(354, doy.getMaximumValue());
        assertEquals(355, doy.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), doy.addToCopy(1));

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    @Test
    public void testSampleDate2_ISO2005_11_26_to_Islamic() {
        DateTime dt = new DateTime(2005, 11, 26, 0, 0, 0, 0, ISO_UTC).withChronology(ISLAMIC_UTC);

        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(15, dt.getCenturyOfEra());
        assertEquals(26, dt.getYearOfCentury());
        assertEquals(1426, dt.getYearOfEra());

        assertEquals(1426, dt.getYear());
        Property year = dt.year();
        assertTrue(year.isLeap());
        assertEquals(1, year.getLeapAmount());
        assertEquals(DurationFieldType.days(), getType(year.getLeapDurationField()));

        assertEquals(10, dt.getMonthOfYear());
        Property month = dt.monthOfYear();
        assertFalse(month.isLeap());
        assertEquals(0, month.getLeapAmount());
        assertEquals(DurationFieldType.days(), getType(month.getLeapDurationField()));
        assertEquals(1, month.getMinimumValue());
        assertEquals(1, month.getMinimumValueOverall());
        assertEquals(12, month.getMaximumValue());
        assertEquals(12, month.getMaximumValueOverall());

        assertEquals(24, dt.getDayOfMonth());
        Property day = dt.dayOfMonth();
        assertFalse(day.isLeap());
        assertEquals(0, day.getLeapAmount());
        assertNull(day.getLeapDurationField());
        assertEquals(1, day.getMinimumValue());
        assertEquals(1, day.getMinimumValueOverall());
        assertEquals(29, day.getMaximumValue());
        assertEquals(30, day.getMaximumValueOverall());

        assertEquals(DateTimeConstants.SATURDAY, dt.getDayOfWeek());
        Property dow = dt.dayOfWeek();
        assertFalse(dow.isLeap());
        assertEquals(0, dow.getLeapAmount());
        assertNull(dow.getLeapDurationField());
        assertEquals(1, dow.getMinimumValue());
        assertEquals(1, dow.getMinimumValueOverall());
        assertEquals(7, dow.getMaximumValue());
        assertEquals(7, dow.getMaximumValueOverall());

        assertEquals(5 * 30 + 4 * 29 + 24, dt.getDayOfYear());
        Property doy = dt.dayOfYear();
        assertFalse(doy.isLeap());
        assertEquals(0, doy.getLeapAmount());
        assertNull(doy.getLeapDurationField());
        assertEquals(1, doy.getMinimumValue());
        assertEquals(1, doy.getMinimumValueOverall());
        assertEquals(355, doy.getMaximumValue());
        assertEquals(355, doy.getMaximumValueOverall());

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    @Test
    public void testSampleDate3_Islamic1426_12_24() {
        DateTime dt = new DateTime(1426, 12, 24, 0, 0, 0, 0, ISLAMIC_UTC);
        assertEquals(IslamicChronology.AH, dt.getEra());

        assertEquals(1426, dt.getYear());
        Property year = dt.year();
        assertTrue(year.isLeap());
        assertEquals(1, year.getLeapAmount());
        assertEquals(DurationFieldType.days(), getType(year.getLeapDurationField()));

        assertEquals(12, dt.getMonthOfYear());
        Property month = dt.monthOfYear();
        assertTrue(month.isLeap());
        assertEquals(1, month.getLeapAmount());
        assertEquals(DurationFieldType.days(), getType(month.getLeapDurationField()));
        assertEquals(1, month.getMinimumValue());
        assertEquals(1, month.getMinimumValueOverall());
        assertEquals(12, month.getMaximumValue());
        assertEquals(12, month.getMaximumValueOverall());

        assertEquals(24, dt.getDayOfMonth());
        Property day = dt.dayOfMonth();
        assertFalse(day.isLeap());
        assertEquals(0, day.getLeapAmount());
        assertNull(day.getLeapDurationField());
        assertEquals(1, day.getMinimumValue());
        assertEquals(1, day.getMinimumValueOverall());
        assertEquals(30, day.getMaximumValue());
        assertEquals(30, day.getMaximumValueOverall());

        assertEquals(DateTimeConstants.TUESDAY, dt.getDayOfWeek());
        Property dow = dt.dayOfWeek();
        assertFalse(dow.isLeap());
        assertEquals(0, dow.getLeapAmount());
        assertNull(dow.getLeapDurationField());
        assertEquals(1, dow.getMinimumValue());
        assertEquals(1, dow.getMinimumValueOverall());
        assertEquals(7, dow.getMaximumValue());
        assertEquals(7, dow.getMaximumValueOverall());

        assertEquals(6 * 30 + 5 * 29 + 24, dt.getDayOfYear());
        Property doy = dt.dayOfYear();
        assertFalse(doy.isLeap());
        assertEquals(0, doy.getLeapAmount());
        assertNull(doy.getLeapDurationField());
        assertEquals(1, doy.getMinimumValue());
        assertEquals(1, doy.getMinimumValueOverall());
        assertEquals(355, doy.getMaximumValue());
        assertEquals(355, doy.getMaximumValueOverall());

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    @Test
    public void testSampleDateWithZone() {
        // 12:00 in Paris in late November is 11:00 UTC (CET is UTC+1 in winter)
        DateTime dt = new DateTime(2005, 11, 26, 12, 0, 0, 0, ZONE_PARIS).withChronology(ISLAMIC_UTC);
        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(1426, dt.getYear());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(24, dt.getDayOfMonth());
        assertEquals(11, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    // ---------------------------------------------------------------------
    // Leap year patterns
    // ---------------------------------------------------------------------

    @Test
    public void testLeapYearPattern_15Based() {
        verifyLeapYearPattern("15-based", IslamicChronology.LEAP_YEAR_15_BASED,
                2, 5, 7, 10, 13, 15, 18, 21, 24, 26, 29);
    }

    @Test
    public void testLeapYearPattern_16Based() {
        verifyLeapYearPattern("16-based", IslamicChronology.LEAP_YEAR_16_BASED,
                2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29);
    }

    @Test
    public void testLeapYearPattern_Indian() {
        verifyLeapYearPattern("Indian", IslamicChronology.LEAP_YEAR_INDIAN,
                2, 5, 8, 10, 13, 16, 19, 21, 24, 27, 29);
    }

    @Test
    public void testLeapYearPattern_HabashAlHasib() {
        verifyLeapYearPattern("Habash al-Hasib", IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB,
                2, 5, 8, 11, 13, 16, 19, 21, 24, 27, 30);
    }

    // ---------------------------------------------------------------------
    // Calendar sweep (slow)
    // ---------------------------------------------------------------------

    /**
     * Sweeps dates from Islamic epoch to ISO year 3000, validating:
     * - Era name/value
     * - Day/year/month alignment and lengths
     * - Leap year behavior
     * - Day-of-week progression
     * 
     * Enable with: -DrunSlowTests=true
     */
    @Test
    public void testCalendarSweep() {
        Assume.assumeTrue("Slow test disabled. Enable with -DrunSlowTests=true", RUN_SLOW_TESTS);

        final DateTime epochIslamic = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        long millis = epochIslamic.getMillis();
        final long endMillis = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();

        final DateTimeField dayOfWeek = ISLAMIC_UTC.dayOfWeek();
        final DateTimeField dayOfYear = ISLAMIC_UTC.dayOfYear();
        final DateTimeField dayOfMonth = ISLAMIC_UTC.dayOfMonth();
        final DateTimeField monthOfYear = ISLAMIC_UTC.monthOfYear();
        final DateTimeField year = ISLAMIC_UTC.year();
        final DateTimeField yearOfEra = ISLAMIC_UTC.yearOfEra();
        final DateTimeField era = ISLAMIC_UTC.era();

        int expectedDOW = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;

        while (millis < endMillis) {
            int dowValue = dayOfWeek.get(millis);
            int doyValue = dayOfYear.get(millis);
            int dayValue = dayOfMonth.get(millis);
            int monthValue = monthOfYear.get(millis);
            int yearValue = year.get(millis);
            int yearOfEraValue = yearOfEra.get(millis);

            int doyLen = dayOfYear.getMaximumValue(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);

            assertTrue("Month out of range: " + monthValue, monthValue >= 1 && monthValue <= 12);

            // Era
            assertEquals(1, era.get(millis));
            assertEquals("AH", era.getAsText(millis));
            assertEquals("AH", era.getAsShortText(millis));

            // Date components
            assertEquals(expectedDOY, doyValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);

            // Leap year
            boolean leap = ((11 * yearValue + 14) % 30) < 11;
            assertEquals(leap, year.isLeap(millis));

            // Month length
            switch (monthValue) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                    assertEquals(30, monthLen);
                    break;
                case 2:
                case 4:
                case 6:
                case 8:
                case 10:
                    assertEquals(29, monthLen);
                    break;
                case 12:
                    assertEquals(leap ? 30 : 29, monthLen);
                    break;
                default:
                    fail("Unexpected month value: " + monthValue);
            }

            // Year length
            assertEquals(leap ? 355 : 354, doyLen);

            // Advance expected values by one day
            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;
            if (expectedDay > monthLen) {
                expectedDay = 1;
                expectedMonth++;
                if (expectedMonth == 13) {
                    expectedMonth = 1;
                    expectedDOY = 1;
                    expectedYear++;
                }
            }

            millis += ONE_DAY_MILLIS;
        }
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static DurationFieldType getType(DurationField field) {
        return field == null ? null : field.getType();
    }

    /**
     * Verifies that the provided leap year pattern marks exactly the given
     * cycle years (1..30) as leap, and all others as common.
     */
    private static void verifyLeapYearPattern(String name,
                                              IslamicChronology.LeapYearPatternType pattern,
                                              int... leapYearsInCycle) {
        boolean[] expected = new boolean[31]; // index 0 unused, 1..30 used
        for (int y : leapYearsInCycle) {
            expected[y] = true;
        }

        for (int y = 1; y <= 30; y++) {
            boolean actual = pattern.isLeapYear(y);
            assertEquals("Leap year mismatch for " + name + " at cycle year " + y, expected[y], actual);
        }
    }
}