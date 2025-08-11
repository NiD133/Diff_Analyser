package org.joda.time.chrono;

import org.joda.time.*;
import org.joda.time.DateTime.Property;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 * Unit tests for EthiopicChronology focused on readability and maintainability.
 *
 * Highlights:
 * - Uses explicit constants instead of magic numbers.
 * - Clear setup/teardown of time, locale and zone defaults.
 * - Removes console output and JUnit3 boilerplate.
 * - Provides comments explaining intent behind checks.
 * - Keeps long-running calendar walk test behind an opt-in flag.
 */
public class TestEthiopicChronology {

    // Toggle to run long, exhaustive calendar walk (about ~365k iterations).
    // Enable if you need deep validation across a wide date range.
    private static final boolean RUN_LONG_TESTS = false;

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final long STEP_ONE_DAY = MILLIS_PER_DAY;
    private static final long LEAP_CYCLE_DAYS = 4L * 365L + 1L; // Ethiopic leap every 4 years

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Fixed "now" used by tests: 2002-06-09 ISO UTC.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0, ISO_UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

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

    // -----------------------------------------------------------------------
    @Test
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, EthiopicChronology.getInstanceUTC().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstanceUTC().getClass());
    }

    @Test
    public void testFactoryDefaultZone() {
        assertEquals(LONDON, EthiopicChronology.getInstance().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance().getClass());
    }

    @Test
    public void testFactoryForZone() {
        assertEquals(TOKYO, EthiopicChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, EthiopicChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, EthiopicChronology.getInstance(null).getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance(TOKYO).getClass());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testEqualityAndSingletons() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(LONDON));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(PARIS));
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC());
        assertSame(EthiopicChronology.getInstance(), EthiopicChronology.getInstance(LONDON));
    }

    @Test
    public void testWithUTC() {
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(LONDON).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(TOKYO).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC().withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance().withUTC());
    }

    @Test
    public void testWithZone() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(null));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance().withZone(PARIS));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void testToString() {
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance(LONDON).toString());
        assertEquals("EthiopicChronology[Asia/Tokyo]", EthiopicChronology.getInstance(TOKYO).toString());
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance().toString());
        assertEquals("EthiopicChronology[UTC]", EthiopicChronology.getInstanceUTC().toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testDurationFields() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();

        assertEquals("eras", ethiopic.eras().getName());
        assertEquals("centuries", ethiopic.centuries().getName());
        assertEquals("years", ethiopic.years().getName());
        assertEquals("weekyears", ethiopic.weekyears().getName());
        assertEquals("months", ethiopic.months().getName());
        assertEquals("weeks", ethiopic.weeks().getName());
        assertEquals("days", ethiopic.days().getName());
        assertEquals("halfdays", ethiopic.halfdays().getName());
        assertEquals("hours", ethiopic.hours().getName());
        assertEquals("minutes", ethiopic.minutes().getName());
        assertEquals("seconds", ethiopic.seconds().getName());
        assertEquals("millis", ethiopic.millis().getName());

        assertFalse(ethiopic.eras().isSupported());
        assertTrue(ethiopic.centuries().isSupported());
        assertTrue(ethiopic.years().isSupported());
        assertTrue(ethiopic.weekyears().isSupported());
        assertTrue(ethiopic.months().isSupported());
        assertTrue(ethiopic.weeks().isSupported());
        assertTrue(ethiopic.days().isSupported());
        assertTrue(ethiopic.halfdays().isSupported());
        assertTrue(ethiopic.hours().isSupported());
        assertTrue(ethiopic.minutes().isSupported());
        assertTrue(ethiopic.seconds().isSupported());
        assertTrue(ethiopic.millis().isSupported());

        // In zone with DST, these are not precise due to varying day lengths.
        assertFalse(ethiopic.centuries().isPrecise());
        assertFalse(ethiopic.years().isPrecise());
        assertFalse(ethiopic.weekyears().isPrecise());
        assertFalse(ethiopic.months().isPrecise());
        assertFalse(ethiopic.weeks().isPrecise());
        assertFalse(ethiopic.days().isPrecise());
        assertFalse(ethiopic.halfdays().isPrecise());
        assertTrue(ethiopic.hours().isPrecise());
        assertTrue(ethiopic.minutes().isPrecise());
        assertTrue(ethiopic.seconds().isPrecise());
        assertTrue(ethiopic.millis().isPrecise());

        final EthiopicChronology ethiopicUTC = EthiopicChronology.getInstanceUTC();
        assertFalse(ethiopicUTC.centuries().isPrecise());
        assertFalse(ethiopicUTC.years().isPrecise());
        assertFalse(ethiopicUTC.weekyears().isPrecise());
        assertFalse(ethiopicUTC.months().isPrecise());
        assertTrue(ethiopicUTC.weeks().isPrecise());
        assertTrue(ethiopicUTC.days().isPrecise());
        assertTrue(ethiopicUTC.halfdays().isPrecise());
        assertTrue(ethiopicUTC.hours().isPrecise());
        assertTrue(ethiopicUTC.minutes().isPrecise());
        assertTrue(ethiopicUTC.seconds().isPrecise());
        assertTrue(ethiopicUTC.millis().isPrecise());

        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final EthiopicChronology ethiopicGMT = EthiopicChronology.getInstance(gmt);
        assertFalse(ethiopicGMT.centuries().isPrecise());
        assertFalse(ethiopicGMT.years().isPrecise());
        assertFalse(ethiopicGMT.weekyears().isPrecise());
        assertFalse(ethiopicGMT.months().isPrecise());
        assertTrue(ethiopicGMT.weeks().isPrecise());
        assertTrue(ethiopicGMT.days().isPrecise());
        assertTrue(ethiopicGMT.halfdays().isPrecise());
        assertTrue(ethiopicGMT.hours().isPrecise());
        assertTrue(ethiopicGMT.minutes().isPrecise());
        assertTrue(ethiopicGMT.seconds().isPrecise());
        assertTrue(ethiopicGMT.millis().isPrecise());
    }

    @Test
    public void testDateFields() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();

        assertEquals("era", ethiopic.era().getName());
        assertEquals("centuryOfEra", ethiopic.centuryOfEra().getName());
        assertEquals("yearOfCentury", ethiopic.yearOfCentury().getName());
        assertEquals("yearOfEra", ethiopic.yearOfEra().getName());
        assertEquals("year", ethiopic.year().getName());
        assertEquals("monthOfYear", ethiopic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", ethiopic.weekyearOfCentury().getName());
        assertEquals("weekyear", ethiopic.weekyear().getName());
        assertEquals("weekOfWeekyear", ethiopic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", ethiopic.dayOfYear().getName());
        assertEquals("dayOfMonth", ethiopic.dayOfMonth().getName());
        assertEquals("dayOfWeek", ethiopic.dayOfWeek().getName());

        assertTrue(ethiopic.era().isSupported());
        assertTrue(ethiopic.centuryOfEra().isSupported());
        assertTrue(ethiopic.yearOfCentury().isSupported());
        assertTrue(ethiopic.yearOfEra().isSupported());
        assertTrue(ethiopic.year().isSupported());
        assertTrue(ethiopic.monthOfYear().isSupported());
        assertTrue(ethiopic.weekyearOfCentury().isSupported());
        assertTrue(ethiopic.weekyear().isSupported());
        assertTrue(ethiopic.weekOfWeekyear().isSupported());
        assertTrue(ethiopic.dayOfYear().isSupported());
        assertTrue(ethiopic.dayOfMonth().isSupported());
        assertTrue(ethiopic.dayOfWeek().isSupported());

        assertEquals(ethiopic.eras(), ethiopic.era().getDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.centuryOfEra().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.yearOfCentury().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.yearOfEra().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.year().getDurationField());
        assertEquals(ethiopic.months(), ethiopic.monthOfYear().getDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekyearOfCentury().getDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekyear().getDurationField());
        assertEquals(ethiopic.weeks(), ethiopic.weekOfWeekyear().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfYear().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfMonth().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfWeek().getDurationField());

        assertNull(ethiopic.era().getRangeDurationField());
        assertEquals(ethiopic.eras(), ethiopic.centuryOfEra().getRangeDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.yearOfCentury().getRangeDurationField());
        assertEquals(ethiopic.eras(), ethiopic.yearOfEra().getRangeDurationField());
        assertNull(ethiopic.year().getRangeDurationField());
        assertEquals(ethiopic.years(), ethiopic.monthOfYear().getRangeDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.weekyearOfCentury().getRangeDurationField());
        assertNull(ethiopic.weekyear().getRangeDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekOfWeekyear().getRangeDurationField());
        assertEquals(ethiopic.years(), ethiopic.dayOfYear().getRangeDurationField());
        assertEquals(ethiopic.months(), ethiopic.dayOfMonth().getRangeDurationField());
        assertEquals(ethiopic.weeks(), ethiopic.dayOfWeek().getRangeDurationField());
    }

    @Test
    public void testTimeFields() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();

        assertEquals("halfdayOfDay", ethiopic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", ethiopic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", ethiopic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", ethiopic.clockhourOfDay().getName());
        assertEquals("hourOfDay", ethiopic.hourOfDay().getName());
        assertEquals("minuteOfDay", ethiopic.minuteOfDay().getName());
        assertEquals("minuteOfHour", ethiopic.minuteOfHour().getName());
        assertEquals("secondOfDay", ethiopic.secondOfDay().getName());
        assertEquals("secondOfMinute", ethiopic.secondOfMinute().getName());
        assertEquals("millisOfDay", ethiopic.millisOfDay().getName());
        assertEquals("millisOfSecond", ethiopic.millisOfSecond().getName());

        assertTrue(ethiopic.halfdayOfDay().isSupported());
        assertTrue(ethiopic.clockhourOfHalfday().isSupported());
        assertTrue(ethiopic.hourOfHalfday().isSupported());
        assertTrue(ethiopic.clockhourOfDay().isSupported());
        assertTrue(ethiopic.hourOfDay().isSupported());
        assertTrue(ethiopic.minuteOfDay().isSupported());
        assertTrue(ethiopic.minuteOfHour().isSupported());
        assertTrue(ethiopic.secondOfDay().isSupported());
        assertTrue(ethiopic.secondOfMinute().isSupported());
        assertTrue(ethiopic.millisOfDay().isSupported());
        assertTrue(ethiopic.millisOfSecond().isSupported());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testEpochAlignsWithJulian() {
        // Ethiopic epoch is 1-01-01 Ethiopic (EE), which corresponds to 8-08-29 Julian.
        DateTime ethiopicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime expectedJulian = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC);
        assertEquals(expectedJulian, ethiopicEpoch.withChronology(JULIAN_UTC));
    }

    @Test
    public void testEra() {
        assertEquals(1, EthiopicChronology.EE);
        try {
            // Ethiopic chronology is not proleptic; year 0 and negatives are invalid.
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ETHIOPIC_UTC);
            fail("Expected IllegalArgumentException for negative Ethiopic year");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    /**
     * Exhaustive calendar walk to validate era, year, month, day, day-of-week, and day-of-year.
     * This test runs only when RUN_LONG_TESTS is true to keep the standard suite fast.
     */
    @Test
    public void testCalendar_LongWalk_DisabledByDefault() {
        assumeTrue("Enable RUN_LONG_TESTS to execute this test", RUN_LONG_TESTS);

        DateTime ethiopicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        long millis = ethiopicEpoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();

        DateTimeField dayOfWeek = ETHIOPIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ETHIOPIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ETHIOPIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ETHIOPIC_UTC.monthOfYear();
        DateTimeField year = ETHIOPIC_UTC.year();
        DateTimeField yearOfEra = ETHIOPIC_UTC.yearOfEra();
        DateTimeField era = ETHIOPIC_UTC.era();

        // Day-of-week at Ethiopic epoch aligns with the Julian date 8-08-29.
        int expectedDOW = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;

        while (millis < end) {
            int dowValue = dayOfWeek.get(millis);
            int doyValue = dayOfYear.get(millis);
            int dayValue = dayOfMonth.get(millis);
            int monthValue = monthOfYear.get(millis);
            int yearValue = year.get(millis);
            int yearOfEraValue = yearOfEra.get(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);

            assertTrue("Month out of range: " + monthValue, monthValue >= 1 && monthValue <= 13);

            // era field is always EE (1)
            assertEquals(1, era.get(millis));
            assertEquals("EE", era.getAsText(millis));
            assertEquals("EE", era.getAsShortText(millis));

            // fields match expected date
            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedDOY, doyValue);

            // leap year rule: every 4th year (year % 4 == 3)
            assertEquals(yearValue % 4 == 3, year.isLeap(millis));

            // month 13 has 5 or 6 days depending on leap year
            if (monthValue == 13) {
                assertEquals(yearValue % 4 == 3, monthOfYear.isLeap(millis));
                assertEquals(yearValue % 4 == 3 ? 6 : 5, monthLen);
            } else {
                assertEquals(30, monthLen);
            }

            // Advance expected date by one day.
            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;

            if (expectedDay == 31 && expectedMonth < 13) {
                expectedDay = 1;
                expectedMonth++;
            } else if (expectedMonth == 13) {
                boolean leap = expectedYear % 4 == 3;
                int terminalDay = leap ? 7 : 6; // day after the last day triggers new year
                if (expectedDay == terminalDay) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                }
            }

            millis += STEP_ONE_DAY;
        }
    }

    @Test
    public void testSampleDate_Basics() {
        // ISO 2004-06-09 maps to Ethiopic 1996-10-02 EE in UTC.
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(ETHIOPIC_UTC);

        assertEquals(EthiopicChronology.EE, dt.getEra());
        assertEquals(20, dt.getCenturyOfEra()); // 1996 -> 20th century (1..100=1st, 1901..2000=20th)
        assertEquals(96, dt.getYearOfCentury());
        assertEquals(1996, dt.getYearOfEra());

        assertEquals(1996, dt.getYear());
        Property fld = dt.year();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(13, fld.getMaximumValue());
        assertEquals(13, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1997, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(4));
        assertEquals(new DateTime(1996, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addWrapFieldToCopy(4));

        assertEquals(2, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertNull(fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertNull(fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(9 * 30 + 2, dt.getDayOfYear()); // 9 full 30-day months + day 2
        fld = dt.dayOfYear();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertNull(fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(365, fld.getMaximumValue());
        assertEquals(366, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    @Test
    public void testSampleDateWithZone() {
        // 2004-06-09T12:00 in Paris (UTC+2 in summer) => 10:00 UTC.
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(ETHIOPIC_UTC);

        assertEquals(EthiopicChronology.EE, dt.getEra());
        assertEquals(1996, dt.getYear());
        assertEquals(1996, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay()); // 12:00 +02:00 => 10:00 UTC in Ethiopic chronology
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    @Test
    public void testDurationYear() {
        // Validates length of years across a 4-year cycle (leap year is year%4==3).
        DateTime dt96 = new DateTime(1996, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt97 = new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt98 = new DateTime(1998, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt99 = new DateTime(1999, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt00 = new DateTime(2000, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);

        DurationField fld = dt96.year().getDurationField();
        assertEquals(ETHIOPIC_UTC.years(), fld);

        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1, dt96.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2, dt96.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3, dt96.getMillis()));
        assertEquals(LEAP_CYCLE_DAYS * MILLIS_PER_DAY, fld.getMillis(4, dt96.getMillis()));

        assertEquals((LEAP_CYCLE_DAYS * MILLIS_PER_DAY) / 4, fld.getMillis(1));
        assertEquals((LEAP_CYCLE_DAYS * MILLIS_PER_DAY) / 2, fld.getMillis(2));

        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1L, dt96.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2L, dt96.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3L, dt96.getMillis()));
        assertEquals(LEAP_CYCLE_DAYS * MILLIS_PER_DAY, fld.getMillis(4L, dt96.getMillis()));

        assertEquals((LEAP_CYCLE_DAYS * MILLIS_PER_DAY) / 4, fld.getMillis(1L));
        assertEquals((LEAP_CYCLE_DAYS * MILLIS_PER_DAY) / 2, fld.getMillis(2L));

        assertEquals((LEAP_CYCLE_DAYS * MILLIS_PER_DAY) / 4, fld.getUnitMillis());

        assertEquals(0, fld.getValue(1L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(1, fld.getValue(2L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(2, fld.getValue(3L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(3, fld.getValue(LEAP_CYCLE_DAYS * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(4, fld.getValue(LEAP_CYCLE_DAYS * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(4, fld.getValue(LEAP_CYCLE_DAYS * MILLIS_PER_DAY + 1L, dt96.getMillis()));

        assertEquals(dt97.getMillis(), fld.add(dt96.getMillis(), 1));
        assertEquals(dt98.getMillis(), fld.add(dt96.getMillis(), 2));
        assertEquals(dt99.getMillis(), fld.add(dt96.getMillis(), 3));
        assertEquals(dt00.getMillis(), fld.add(dt96.getMillis(), 4));

        assertEquals(dt97.getMillis(), fld.add(dt96.getMillis(), 1L));
        assertEquals(dt98.getMillis(), fld.add(dt96.getMillis(), 2L));
        assertEquals(dt99.getMillis(), fld.add(dt96.getMillis(), 3L));
        assertEquals(dt00.getMillis(), fld.add(dt96.getMillis(), 4L));
    }

    @Test
    public void testDurationMonth() {
        // For year 1999 (leap year), month 13 has 6 days. Others have 30.
        DateTime dt11 = new DateTime(1999, 11, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt12 = new DateTime(1999, 12, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt13 = new DateTime(1999, 13, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt01 = new DateTime(2000, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);

        DurationField fld = dt11.monthOfYear().getDurationField();
        assertEquals(ETHIOPIC_UTC.months(), fld);

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4, dt11.getMillis()));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3L, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4L, dt11.getMillis()));

        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13L));

        assertEquals(0, fld.getValue(1L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(2L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));

        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3));

        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1L));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2L));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3L));
    }

    @Test
    public void testLeap_5_13() {
        Chronology chrono = EthiopicChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 5, 0, 0, chrono);

        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    @Test
    public void testLeap_6_13() {
        Chronology chrono = EthiopicChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 6, 0, 0, chrono);

        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }
}