package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the month-related DurationField in CopticChronology.
 * This test focuses on behavior during a Coptic leap year, where the 13th month has 6 days instead of 5.
 * The Coptic year 1723 is a leap year.
 */
public class CopticChronologyTest {

    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    // A standard Coptic month has 30 days.
    private static final long STANDARD_MONTH_MILLIS = 30L * DateTimeConstants.MILLIS_PER_DAY;
    // The 13th month in a Coptic leap year has 6 days.
    private static final long LEAP_YEAR_LAST_MONTH_MILLIS = 6L * DateTimeConstants.MILLIS_PER_DAY;

    // A fixed point in time for tests that rely on the current time.
    // June 9, 2002
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0).getMillis();

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    // Test data for a Coptic leap year (1723)
    private DateTime dateIn11thMonth;
    private DateTime dateIn12thMonth;
    private DateTime dateIn13thMonth;
    private DateTime dateIn1stMonthOfNextYear;
    private DurationField monthDurationField;

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);

        // Initialize test dates for the Coptic leap year 1723.
        dateIn11thMonth = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
        dateIn12thMonth = new DateTime(1723, 12, 2, 0, 0, 0, 0, COPTIC_UTC);
        dateIn13thMonth = new DateTime(1723, 13, 2, 0, 0, 0, 0, COPTIC_UTC);
        dateIn1stMonthOfNextYear = new DateTime(1724, 1, 2, 0, 0, 0, 0, COPTIC_UTC);
        monthDurationField = dateIn11thMonth.monthOfYear().getDurationField();
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

    @Test
    public void testMonthDuration_isCorrectField() {
        assertEquals("The duration field for monthOfYear should be the chronology's months field",
                COPTIC_UTC.months(), monthDurationField);
    }

    @Test
    public void testMonthDuration_getMillis_inLeapYear() {
        // --- Test getMillis(int, long) and getMillis(long, long) with a reference instant ---
        // These tests show that the duration calculation accounts for the short 13th month in a leap year.

        // Duration of 1 month (month 11) = 30 days
        assertEquals(STANDARD_MONTH_MILLIS, monthDurationField.getMillis(1, dateIn11thMonth.getMillis()));
        assertEquals(STANDARD_MONTH_MILLIS, monthDurationField.getMillis(1L, dateIn11thMonth.getMillis()));

        // Duration of 2 months (11, 12) = 30 + 30 days
        assertEquals(2 * STANDARD_MONTH_MILLIS, monthDurationField.getMillis(2, dateIn11thMonth.getMillis()));
        assertEquals(2 * STANDARD_MONTH_MILLIS, monthDurationField.getMillis(2L, dateIn11thMonth.getMillis()));

        // Duration of 3 months (11, 12, 13) = 30 + 30 + 6 days (13th month is a leap month)
        long threeMonthsDuration = 2 * STANDARD_MONTH_MILLIS + LEAP_YEAR_LAST_MONTH_MILLIS;
        assertEquals(threeMonthsDuration, monthDurationField.getMillis(3, dateIn11thMonth.getMillis()));
        assertEquals(threeMonthsDuration, monthDurationField.getMillis(3L, dateIn11thMonth.getMillis()));

        // Duration of 4 months (11, 12, 13 of 1723; 1 of 1724) = 30 + 30 + 6 + 30 days
        long fourMonthsDuration = 3 * STANDARD_MONTH_MILLIS + LEAP_YEAR_LAST_MONTH_MILLIS;
        assertEquals(fourMonthsDuration, monthDurationField.getMillis(4, dateIn11thMonth.getMillis()));
        assertEquals(fourMonthsDuration, monthDurationField.getMillis(4L, dateIn11thMonth.getMillis()));

        // --- Test getMillis(int) and getMillis(long) without a reference instant ---
        // These should return the standard, average duration of a month.
        assertEquals(STANDARD_MONTH_MILLIS, monthDurationField.getMillis(1));
        assertEquals(2 * STANDARD_MONTH_MILLIS, monthDurationField.getMillis(2));
        assertEquals(13 * STANDARD_MONTH_MILLIS, monthDurationField.getMillis(13));
        assertEquals(STANDARD_MONTH_MILLIS, monthDurationField.getMillis(1L));
        assertEquals(2 * STANDARD_MONTH_MILLIS, monthDurationField.getMillis(2L));
        assertEquals(13 * STANDARD_MONTH_MILLIS, monthDurationField.getMillis(13L));
    }

    @Test
    public void testMonthDuration_getValue_inLeapYear() {
        // Tests the inverse of getMillis: how many full months fit into a given duration.
        long referenceMillis = dateIn11thMonth.getMillis();

        // One standard month
        assertEquals(0, monthDurationField.getValue(STANDARD_MONTH_MILLIS - 1, referenceMillis));
        assertEquals(1, monthDurationField.getValue(STANDARD_MONTH_MILLIS, referenceMillis));
        assertEquals(1, monthDurationField.getValue(STANDARD_MONTH_MILLIS + 1, referenceMillis));

        // Two standard months
        assertEquals(1, monthDurationField.getValue(2 * STANDARD_MONTH_MILLIS - 1, referenceMillis));
        assertEquals(2, monthDurationField.getValue(2 * STANDARD_MONTH_MILLIS, referenceMillis));
        assertEquals(2, monthDurationField.getValue(2 * STANDARD_MONTH_MILLIS + 1, referenceMillis));

        // Three months, including the leap 13th month (30 + 30 + 6 days)
        long threeMonthsDuration = 2 * STANDARD_MONTH_MILLIS + LEAP_YEAR_LAST_MONTH_MILLIS;
        assertEquals(2, monthDurationField.getValue(threeMonthsDuration - 1, referenceMillis));
        assertEquals(3, monthDurationField.getValue(threeMonthsDuration, referenceMillis));
        assertEquals(3, monthDurationField.getValue(threeMonthsDuration + 1, referenceMillis));

        // Four months, including the leap 13th month (30 + 30 + 6 + 30 days)
        long fourMonthsDuration = 3 * STANDARD_MONTH_MILLIS + LEAP_YEAR_LAST_MONTH_MILLIS;
        assertEquals(3, monthDurationField.getValue(fourMonthsDuration - 1, referenceMillis));
        assertEquals(4, monthDurationField.getValue(fourMonthsDuration, referenceMillis));
        assertEquals(4, monthDurationField.getValue(fourMonthsDuration + 1, referenceMillis));
    }

    @Test
    public void testMonthDuration_add_inLeapYear() {
        // Tests adding months to a date, crossing over the short 13th month of a leap year.
        long startMillis = dateIn11thMonth.getMillis();

        // Add 1 month (11 -> 12)
        assertEquals(dateIn12thMonth.getMillis(), monthDurationField.add(startMillis, 1));
        assertEquals(dateIn12thMonth.getMillis(), monthDurationField.add(startMillis, 1L));

        // Add 2 months (11 -> 13)
        assertEquals(dateIn13thMonth.getMillis(), monthDurationField.add(startMillis, 2));
        assertEquals(dateIn13thMonth.getMillis(), monthDurationField.add(startMillis, 2L));

        // Add 3 months (11 -> 1 of next year), crossing the 6-day 13th month
        assertEquals(dateIn1stMonthOfNextYear.getMillis(), monthDurationField.add(startMillis, 3));
        assertEquals(dateIn1stMonthOfNextYear.getMillis(), monthDurationField.add(startMillis, 3L));
    }
}