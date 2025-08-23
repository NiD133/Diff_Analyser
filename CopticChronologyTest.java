package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * Unit tests for CopticChronology.
 * The tests cover factory methods, timezone/UTC handling, supported fields, precision,
 * epoch alignment with Julian calendar, leap-year/month logic, and duration field behavior.
 */
public class TestCopticChronology extends TestCase {

    // Common constants and helpers -------------------------------------------------------------

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final long ONE_DAY = MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Fixed "now" for deterministic tests: 2002-06-09T00:00:00.000Z
    private static final long FIXED_TEST_TIME_NOW_UTC =
            new DateTime(2002, 6, 9, 0, 0, 0, 0, ISO_UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCopticChronology.class);
    }

    public TestCopticChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(FIXED_TEST_TIME_NOW_UTC);
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

    // Helpers ---------------------------------------------------------------------------------

    private static DateTime copticUTC(int year, int month, int day) {
        return new DateTime(year, month, day, 0, 0, 0, 0, COPTIC_UTC);
    }

    private static boolean isCopticLeapYear(int year) {
        // In Coptic, every 4th year is leap, with year % 4 == 3
        return year % 4 == 3;
    }

    // Factory and zone behavior ----------------------------------------------------------------

    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, CopticChronology.getInstanceUTC().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, CopticChronology.getInstance().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, CopticChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, CopticChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, CopticChronology.getInstance(null).getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance(TOKYO).getClass());
    }

    public void testEquality() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(LONDON));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance(PARIS));
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC());
        assertSame(CopticChronology.getInstance(), CopticChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(LONDON).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(TOKYO).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC().withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(null));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance().withZone(PARIS));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance(LONDON).toString());
        assertEquals("CopticChronology[Asia/Tokyo]", CopticChronology.getInstance(TOKYO).toString());
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance().toString());
        assertEquals("CopticChronology[UTC]", CopticChronology.getInstanceUTC().toString());
    }

    // Fields: Duration and Date/Time -----------------------------------------------------------

    public void testDurationFields() {
        final CopticChronology coptic = CopticChronology.getInstance();

        assertEquals("eras", coptic.eras().getName());
        assertEquals("centuries", coptic.centuries().getName());
        assertEquals("years", coptic.years().getName());
        assertEquals("weekyears", coptic.weekyears().getName());
        assertEquals("months", coptic.months().getName());
        assertEquals("weeks", coptic.weeks().getName());
        assertEquals("days", coptic.days().getName());
        assertEquals("halfdays", coptic.halfdays().getName());
        assertEquals("hours", coptic.hours().getName());
        assertEquals("minutes", coptic.minutes().getName());
        assertEquals("seconds", coptic.seconds().getName());
        assertEquals("millis", coptic.millis().getName());

        assertFalse(coptic.eras().isSupported());
        assertTrue(coptic.centuries().isSupported());
        assertTrue(coptic.years().isSupported());
        assertTrue(coptic.weekyears().isSupported());
        assertTrue(coptic.months().isSupported());
        assertTrue(coptic.weeks().isSupported());
        assertTrue(coptic.days().isSupported());
        assertTrue(coptic.halfdays().isSupported());
        assertTrue(coptic.hours().isSupported());
        assertTrue(coptic.minutes().isSupported());
        assertTrue(coptic.seconds().isSupported());
        assertTrue(coptic.millis().isSupported());

        assertFalse(coptic.centuries().isPrecise());
        assertFalse(coptic.years().isPrecise());
        assertFalse(coptic.weekyears().isPrecise());
        assertFalse(coptic.months().isPrecise());
        assertFalse(coptic.weeks().isPrecise());
        assertFalse(coptic.days().isPrecise());
        assertFalse(coptic.halfdays().isPrecise());
        assertTrue(coptic.hours().isPrecise());
        assertTrue(coptic.minutes().isPrecise());
        assertTrue(coptic.seconds().isPrecise());
        assertTrue(coptic.millis().isPrecise());

        final CopticChronology copticUTC = CopticChronology.getInstanceUTC();
        assertFalse(copticUTC.centuries().isPrecise());
        assertFalse(copticUTC.years().isPrecise());
        assertFalse(copticUTC.weekyears().isPrecise());
        assertFalse(copticUTC.months().isPrecise());
        assertTrue(copticUTC.weeks().isPrecise());
        assertTrue(copticUTC.days().isPrecise());
        assertTrue(copticUTC.halfdays().isPrecise());
        assertTrue(copticUTC.hours().isPrecise());
        assertTrue(copticUTC.minutes().isPrecise());
        assertTrue(copticUTC.seconds().isPrecise());
        assertTrue(copticUTC.millis().isPrecise());

        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final CopticChronology copticGMT = CopticChronology.getInstance(gmt);
        assertFalse(copticGMT.centuries().isPrecise());
        assertFalse(copticGMT.years().isPrecise());
        assertFalse(copticGMT.weekyears().isPrecise());
        assertFalse(copticGMT.months().isPrecise());
        assertTrue(copticGMT.weeks().isPrecise());
        assertTrue(copticGMT.days().isPrecise());
        assertTrue(copticGMT.halfdays().isPrecise());
        assertTrue(copticGMT.hours().isPrecise());
        assertTrue(copticGMT.minutes().isPrecise());
        assertTrue(copticGMT.seconds().isPrecise());
        assertTrue(copticGMT.millis().isPrecise());
    }

    public void testDateFields() {
        final CopticChronology coptic = CopticChronology.getInstance();

        assertEquals("era", coptic.era().getName());
        assertEquals("centuryOfEra", coptic.centuryOfEra().getName());
        assertEquals("yearOfCentury", coptic.yearOfCentury().getName());
        assertEquals("yearOfEra", coptic.yearOfEra().getName());
        assertEquals("year", coptic.year().getName());
        assertEquals("monthOfYear", coptic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", coptic.weekyearOfCentury().getName());
        assertEquals("weekyear", coptic.weekyear().getName());
        assertEquals("weekOfWeekyear", coptic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", coptic.dayOfYear().getName());
        assertEquals("dayOfMonth", coptic.dayOfMonth().getName());
        assertEquals("dayOfWeek", coptic.dayOfWeek().getName());

        assertTrue(coptic.era().isSupported());
        assertTrue(coptic.centuryOfEra().isSupported());
        assertTrue(coptic.yearOfCentury().isSupported());
        assertTrue(coptic.yearOfEra().isSupported());
        assertTrue(coptic.year().isSupported());
        assertTrue(coptic.monthOfYear().isSupported());
        assertTrue(coptic.weekyearOfCentury().isSupported());
        assertTrue(coptic.weekyear().isSupported());
        assertTrue(coptic.weekOfWeekyear().isSupported());
        assertTrue(coptic.dayOfYear().isSupported());
        assertTrue(coptic.dayOfMonth().isSupported());
        assertTrue(coptic.dayOfWeek().isSupported());

        assertEquals(coptic.eras(), coptic.era().getDurationField());
        assertEquals(coptic.centuries(), coptic.centuryOfEra().getDurationField());
        assertEquals(coptic.years(), coptic.yearOfCentury().getDurationField());
        assertEquals(coptic.years(), coptic.yearOfEra().getDurationField());
        assertEquals(coptic.years(), coptic.year().getDurationField());
        assertEquals(coptic.months(), coptic.monthOfYear().getDurationField());
        assertEquals(coptic.weekyears(), coptic.weekyearOfCentury().getDurationField());
        assertEquals(coptic.weekyears(), coptic.weekyear().getDurationField());
        assertEquals(coptic.weeks(), coptic.weekOfWeekyear().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfYear().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfMonth().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfWeek().getDurationField());

        assertNull(coptic.era().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.centuryOfEra().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.yearOfCentury().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.yearOfEra().getRangeDurationField());
        assertNull(coptic.year().getRangeDurationField());
        assertEquals(coptic.years(), coptic.monthOfYear().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.weekyearOfCentury().getRangeDurationField());
        assertNull(coptic.weekyear().getRangeDurationField());
        assertEquals(coptic.weekyears(), coptic.weekOfWeekyear().getRangeDurationField());
        assertEquals(coptic.years(), coptic.dayOfYear().getRangeDurationField());
        assertEquals(coptic.months(), coptic.dayOfMonth().getRangeDurationField());
        assertEquals(coptic.weeks(), coptic.dayOfWeek().getRangeDurationField());
    }

    public void testTimeFields() {
        final CopticChronology coptic = CopticChronology.getInstance();

        assertEquals("halfdayOfDay", coptic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", coptic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", coptic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", coptic.clockhourOfDay().getName());
        assertEquals("hourOfDay", coptic.hourOfDay().getName());
        assertEquals("minuteOfDay", coptic.minuteOfDay().getName());
        assertEquals("minuteOfHour", coptic.minuteOfHour().getName());
        assertEquals("secondOfDay", coptic.secondOfDay().getName());
        assertEquals("secondOfMinute", coptic.secondOfMinute().getName());
        assertEquals("millisOfDay", coptic.millisOfDay().getName());
        assertEquals("millisOfSecond", coptic.millisOfSecond().getName());

        assertTrue(coptic.halfdayOfDay().isSupported());
        assertTrue(coptic.clockhourOfHalfday().isSupported());
        assertTrue(coptic.hourOfHalfday().isSupported());
        assertTrue(coptic.clockhourOfDay().isSupported());
        assertTrue(coptic.hourOfDay().isSupported());
        assertTrue(coptic.minuteOfDay().isSupported());
        assertTrue(coptic.minuteOfHour().isSupported());
        assertTrue(coptic.secondOfDay().isSupported());
        assertTrue(coptic.secondOfMinute().isSupported());
        assertTrue(coptic.millisOfDay().isSupported());
        assertTrue(coptic.millisOfSecond().isSupported());
    }

    // Epoch and era ----------------------------------------------------------------------------

    public void testEpoch() {
        // Coptic 0001-01-01 (UTC) aligns to Julian 0284-08-29 (UTC)
        DateTime copticEpoch = copticUTC(1, 1, 1);
        assertEquals(new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC), copticEpoch.withChronology(JULIAN_UTC));
    }

    public void testEra() {
        assertEquals(1, CopticChronology.AM);
        try {
            // Negative year not allowed by CopticChronology (not proleptic)
            new DateTime(-1, 13, 5, 0, 0, 0, 0, COPTIC_UTC);
            fail("Expected IllegalArgumentException for proleptic negative year");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    // Long-running calendar sweep ---------------------------------------------------------------

    /**
     * Sweeps from Coptic epoch to ISO year 3000 (UTC) one day at a time,
     * validating:
     * - era is always AM
     * - date components match the expected rolling counters
     * - dayOfWeek increments correctly
     * - leap year rule: year % 4 == 3
     * - month lengths: months 1-12 have 30 days; month 13 has 5 or 6 days
     */
    public void testCalendar() {
        if (TestAll.FAST) {
            return;
        }

        final DateTime copticEpoch = copticUTC(1, 1, 1);
        long millis = copticEpoch.getMillis();
        final long endExclusive = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();

        final DateTimeField dayOfWeek = COPTIC_UTC.dayOfWeek();
        final DateTimeField dayOfYear = COPTIC_UTC.dayOfYear();
        final DateTimeField dayOfMonth = COPTIC_UTC.dayOfMonth();
        final DateTimeField monthOfYear = COPTIC_UTC.monthOfYear();
        final DateTimeField year = COPTIC_UTC.year();
        final DateTimeField yearOfEra = COPTIC_UTC.yearOfEra();
        final DateTimeField era = COPTIC_UTC.era();

        int expectedDOW = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;

        while (millis < endExclusive) {
            final int dow = dayOfWeek.get(millis);
            final int doy = dayOfYear.get(millis);
            final int dom = dayOfMonth.get(millis);
            final int moy = monthOfYear.get(millis);
            final int yr = year.get(millis);
            final int yoe = yearOfEra.get(millis);
            final int currentMonthLength = dayOfMonth.getMaximumValue(millis);

            assertTrue("Month out of range: " + moy, moy >= 1 && moy <= 13);

            // Era
            assertEquals(1, era.get(millis));
            assertEquals("AM", era.getAsText(millis));
            assertEquals("AM", era.getAsShortText(millis));

            // Date components
            assertEquals(expectedYear, yr);
            assertEquals(expectedYear, yoe);
            assertEquals(expectedMonth, moy);
            assertEquals(expectedDay, dom);
            assertEquals(expectedDOW, dow);
            assertEquals(expectedDOY, doy);

            // Leap year rule
            assertEquals(isCopticLeapYear(yr), year.isLeap(millis));

            // Month lengths
            if (moy == 13) {
                assertEquals(isCopticLeapYear(yr), monthOfYear.isLeap(millis));
                assertEquals(isCopticLeapYear(yr) ? 6 : 5, currentMonthLength);
            } else {
                assertEquals(30, currentMonthLength);
            }

            // Advance expected counters one day
            expectedDOW = ((expectedDOW) % 7) + 1;
            expectedDay++;
            expectedDOY++;

            // Month/Year rollovers
            if (expectedDay == 31 && expectedMonth < 13) {
                // next month (months 1-12 are 30 days)
                expectedDay = 1;
                expectedMonth++;
            } else if (expectedMonth == 13) {
                // intercalary period at year end (5 or 6 days)
                final int maxIntercalary = isCopticLeapYear(expectedYear) ? 6 : 5;
                if (expectedDay == maxIntercalary + 1) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                }
            }

            millis += ONE_DAY;
        }
    }

    // Sample dates -----------------------------------------------------------------------------

    public void testSampleDate() {
        // ISO: 2004-06-09T00:00Z -> Coptic
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);

        assertEquals(CopticChronology.AM, dt.getEra());
        assertEquals(18, dt.getCenturyOfEra());   // TODO confirm
        assertEquals(20, dt.getYearOfCentury());
        assertEquals(1720, dt.getYearOfEra());

        assertEquals(1720, dt.getYear());
        Property fld = dt.year();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(copticUTC(1721, 10, 2), fld.addToCopy(1));

        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(13, fld.getMaximumValue());
        assertEquals(13, fld.getMaximumValueOverall());
        assertEquals(copticUTC(1721, 1, 2), fld.addToCopy(4));
        assertEquals(copticUTC(1720, 1, 2), fld.addWrapFieldToCopy(4));

        assertEquals(2, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertNull(fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(copticUTC(1720, 10, 3), fld.addToCopy(1));

        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertNull(fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(copticUTC(1720, 10, 3), fld.addToCopy(1));

        assertEquals(9 * 30 + 2, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertNull(fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(365, fld.getMaximumValue());
        assertEquals(366, fld.getMaximumValueOverall());
        assertEquals(copticUTC(1720, 10, 3), fld.addToCopy(1));

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDateWithZone() {
        // Noon in Paris (UTC+2 in summer) corresponds to 10:00 UTC
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(COPTIC_UTC);
        assertEquals(CopticChronology.AM, dt.getEra());
        assertEquals(1720, dt.getYear());
        assertEquals(1720, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay());  // 12:00 in Paris is 10:00 UTC in summer
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    // Duration fields: Year and Month -----------------------------------------------------------

    public void testDurationYear() {
        // Leap year sequence around 1723 (leap)
        DateTime dt20 = copticUTC(1720, 10, 2);
        DateTime dt21 = copticUTC(1721, 10, 2);
        DateTime dt22 = copticUTC(1722, 10, 2);
        DateTime dt23 = copticUTC(1723, 10, 2);
        DateTime dt24 = copticUTC(1724, 10, 2);

        DurationField fld = dt20.year().getDurationField();
        assertEquals(COPTIC_UTC.years(), fld);

        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1, dt20.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2, dt20.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3, dt20.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4, dt20.getMillis()));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2));

        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1L, dt20.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2L, dt20.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3L, dt20.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4L, dt20.getMillis()));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1L));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2L));

        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getUnitMillis());

        assertEquals(0, fld.getValue(1L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(1, fld.getValue(2L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(2, fld.getValue(3L * 365L * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY + 1L, dt20.getMillis()));
        assertEquals(3, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY - 1L, dt20.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY, dt20.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY + 1L, dt20.getMillis()));

        assertEquals(dt21.getMillis(), fld.add(dt20.getMillis(), 1));
        assertEquals(dt22.getMillis(), fld.add(dt20.getMillis(), 2));
        assertEquals(dt23.getMillis(), fld.add(dt20.getMillis(), 3));
        assertEquals(dt24.getMillis(), fld.add(dt20.getMillis(), 4));

        assertEquals(dt21.getMillis(), fld.add(dt20.getMillis(), 1L));
        assertEquals(dt22.getMillis(), fld.add(dt20.getMillis(), 2L));
        assertEquals(dt23.getMillis(), fld.add(dt20.getMillis(), 3L));
        assertEquals(dt24.getMillis(), fld.add(dt20.getMillis(), 4L));
    }

    public void testDurationMonth() {
        // Leap 1723 -> Month 13 has 6 days
        DateTime dt11 = copticUTC(1723, 11, 2);
        DateTime dt12 = copticUTC(1723, 12, 2);
        DateTime dt13 = copticUTC(1723, 13, 2);
        DateTime dt01 = copticUTC(1724, 1, 2);

        DurationField fld = dt11.monthOfYear().getDurationField();
        assertEquals(COPTIC_UTC.months(), fld);

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

    // Leap specifics on month 13 ---------------------------------------------------------------

    public void testLeap_5_13() {
        Chronology chrono = CopticChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 5, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    public void testLeap_6_13() {
        Chronology chrono = CopticChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 6, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }
}