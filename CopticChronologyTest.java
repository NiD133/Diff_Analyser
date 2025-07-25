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
import org.joda.time.DurationFieldType;

/**
 * Unit tests for CopticChronology.
 */
public class TestCopticChronology extends TestCase {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Number of days from 1970-01-01 to 2002-06-09
    private static final long TEST_TIME_NOW = calculateTestTimeNow();

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
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
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
    }

    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, CopticChronology.getInstanceUTC().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, CopticChronology.getInstance().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance().getClass());
    }

    public void testFactoryWithZone() {
        assertEquals(TOKYO, CopticChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, CopticChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, CopticChronology.getInstance(null).getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance(TOKYO).getClass());
    }

    public void testChronologyEquality() {
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

    public void testDurationFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertDurationFieldNames(coptic);
        assertDurationFieldSupport(coptic);
        assertDurationFieldPrecision(coptic);
    }

    public void testDateFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertDateFieldNames(coptic);
        assertDateFieldSupport(coptic);
        assertDateFieldDurations(coptic);
    }

    public void testTimeFields() {
        final CopticChronology coptic = CopticChronology.getInstance();
        assertTimeFieldNames(coptic);
        assertTimeFieldSupport(coptic);
    }

    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals(new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC), epoch.withChronology(JULIAN_UTC));
    }

    public void testEra() {
        assertEquals(1, CopticChronology.AM);
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, COPTIC_UTC);
            fail("Expected IllegalArgumentException for invalid date");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    public void testCalendar() {
        if (TestAll.FAST) {
            return;
        }
        System.out.println("\nTestCopticChronology.testCalendar");
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        validateCalendarFields(millis, end);
    }

    public void testSampleDate() {
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        validateSampleDate(dt);
    }

    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(COPTIC_UTC);
        validateSampleDateWithZone(dt);
    }

    public void testDurationYear() {
        // Leap 1723
        DateTime dt20 = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt21 = new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt22 = new DateTime(1722, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt23 = new DateTime(1723, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt24 = new DateTime(1724, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        validateDurationYear(dt20, dt21, dt22, dt23, dt24);
    }

    public void testDurationMonth() {
        // Leap 1723
        DateTime dt11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt12 = new DateTime(1723, 12, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt13 = new DateTime(1723, 13, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime dt01 = new DateTime(1724, 1, 2, 0, 0, 0, 0, COPTIC_UTC);
        validateDurationMonth(dt11, dt12, dt13, dt01);
    }

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

    // Helper methods for test setup and validation

    private static long calculateTestTimeNow() {
        long y2002days = 365 * 8 + 366 * 4; // Days from 1970 to 2002
        return (y2002days + 31 + 28 + 31 + 30 + 31 + 9 - 1) * MILLIS_PER_DAY;
    }

    private void assertDurationFieldNames(CopticChronology coptic) {
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
    }

    private void assertDurationFieldSupport(CopticChronology coptic) {
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
    }

    private void assertDurationFieldPrecision(CopticChronology coptic) {
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
    }

    private void assertDateFieldNames(CopticChronology coptic) {
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
    }

    private void assertDateFieldSupport(CopticChronology coptic) {
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
    }

    private void assertDateFieldDurations(CopticChronology coptic) {
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
    }

    private void assertTimeFieldNames(CopticChronology coptic) {
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
    }

    private void assertTimeFieldSupport(CopticChronology coptic) {
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

    private void validateCalendarFields(long millis, long end) {
        DateTimeField dayOfWeek = COPTIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = COPTIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = COPTIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = COPTIC_UTC.monthOfYear();
        DateTimeField year = COPTIC_UTC.year();
        DateTimeField yearOfEra = COPTIC_UTC.yearOfEra();
        DateTimeField era = COPTIC_UTC.era();

        int expectedDOW = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
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

            assertEquals(1, era.get(millis));
            assertEquals("AM", era.getAsText(millis));
            assertEquals("AM", era.getAsShortText(millis));

            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedDOY, doyValue);

            assertEquals(yearValue % 4 == 3, year.isLeap(millis));

            if (monthValue == 13) {
                assertEquals(yearValue % 4 == 3, monthOfYear.isLeap(millis));
                if (yearValue % 4 == 3) {
                    assertEquals(6, monthLen);
                } else {
                    assertEquals(5, monthLen);
                }
            } else {
                assertEquals(30, monthLen);
            }

            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;
            if (expectedDay == 31 && expectedMonth < 13) {
                expectedDay = 1;
                expectedMonth++;
            } else if (expectedMonth == 13) {
                if (expectedYear % 4 == 3 && expectedDay == 7) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                } else if (expectedYear % 4 != 3 && expectedDay == 6) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                }
            }
            millis += MILLIS_PER_DAY;
        }
    }

    private void validateSampleDate(DateTime dt) {
        assertEquals(CopticChronology.AM, dt.getEra());
        assertEquals(18, dt.getCenturyOfEra());
        assertEquals(20, dt.getYearOfCentury());
        assertEquals(1720, dt.getYearOfEra());
        assertEquals(1720, dt.getYear());

        Property fld = dt.year();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));

        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(13, fld.getMaximumValue());
        assertEquals(13, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1721, 1, 2, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(4));
        assertEquals(new DateTime(1720, 1, 2, 0, 0, 0, 0, COPTIC_UTC), fld.addWrapFieldToCopy(4));

        assertEquals(2, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));

        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));

        assertEquals(9 * 30 + 2, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertFalse(fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(365, fld.getMaximumValue());
        assertEquals(366, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC), fld.addToCopy(1));

        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    private void validateSampleDateWithZone(DateTime dt) {
        assertEquals(CopticChronology.AM, dt.getEra());
        assertEquals(1720, dt.getYear());
        assertEquals(1720, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay());  // PARIS is UTC+2 in summer (12-2=10)
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    private void validateDurationYear(DateTime dt20, DateTime dt21, DateTime dt22, DateTime dt23, DateTime dt24) {
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

    private void validateDurationMonth(DateTime dt11, DateTime dt12, DateTime dt13, DateTime dt01) {
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
}