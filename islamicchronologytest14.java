package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the properties of IslamicChronology by iterating through a range of dates.
 */
public class IslamicChronologyTest {

    private static final long ONE_DAY_IN_MILLIS = DateTimeConstants.MILLIS_PER_DAY;

    // Chronology instance under test. The default is the 16-based leap year pattern.
    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // A fixed "now" for test consistency, created in a readable way.
    private static final long TEST_TIME_NOW =
            new DateTime(2002, 6, 9, 0, 0, 0, 0, ISO_UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    /**
     * A simple data class to hold the components of an expected date,
     * making state management cleaner than using multiple variables.
     */
    private static class ExpectedDate {
        int year;
        int month;
        int dayOfMonth;
        int dayOfYear;
        int dayOfWeek; // Joda-Time standard: 1=Monday, 7=Sunday
    }

    /**
     * This test comprehensively validates the Islamic calendar implementation by iterating
     * day-by-day from the epoch. It verifies that all date fields, leap year calculations,
     * and month/year lengths behave according to the calendar's rules.
     */
    @Test
    public void testChronologyPropertiesByIteration() {
        // NOTE: The original test iterated up to ISO year 3000, which is extremely slow.
        // This version iterates for the first 400 Islamic years, which is sufficient
        // to cover many 30-year leap cycles and verify the chronology's integrity.
        DateTime start = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime end = new DateTime(400, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);

        long currentMillis = start.getMillis();
        ExpectedDate expectedDate = initializeExpectedDateAtEpoch();

        while (currentMillis < end.getMillis()) {
            verifyEra(currentMillis);

            int actualYear = ISLAMIC_UTC.year().get(currentMillis);
            verifyDateFields(currentMillis, expectedDate, actualYear);

            boolean isLeap = isLeapYearAccordingTo16BasedRule(actualYear);
            assertEquals("Leap year check for year " + actualYear, isLeap, ISLAMIC_UTC.year().isLeap(currentMillis));

            int monthLength = ISLAMIC_UTC.dayOfMonth().getMaximumValue(currentMillis);
            int actualMonth = ISLAMIC_UTC.monthOfYear().get(currentMillis);
            verifyMonthAndYearLength(currentMillis, actualMonth, isLeap, monthLength);

            expectedDate = calculateNextExpectedDate(expectedDate, monthLength);
            currentMillis += ONE_DAY_IN_MILLIS;
        }
    }

    private ExpectedDate initializeExpectedDateAtEpoch() {
        ExpectedDate expected = new ExpectedDate();
        // The Islamic epoch (1 AH, 1 Muharram) corresponds to Friday, July 16, 622 CE (Julian).
        expected.year = 1;
        expected.month = 1;
        expected.dayOfMonth = 1;
        expected.dayOfYear = 1;
        expected.dayOfWeek = DateTimeConstants.FRIDAY;
        return expected;
    }

    private void verifyEra(long instant) {
        assertEquals("Era should always be AH", IslamicChronology.AH, ISLAMIC_UTC.era().get(instant));
        assertEquals("Era text should be AH", "AH", ISLAMIC_UTC.era().getAsText(instant));
        assertEquals("Era short text should be AH", "AH", ISLAMIC_UTC.era().getAsShortText(instant));
    }

    private void verifyDateFields(long instant, ExpectedDate expected, int actualYear) {
        String dateForFailMsg = " on " + new DateTime(instant, ISLAMIC_UTC).toString("YYYY-MM-dd");
        assertEquals("Year" + dateForFailMsg, expected.year, actualYear);
        assertEquals("Year of Era" + dateForFailMsg, expected.year, ISLAMIC_UTC.yearOfEra().get(instant));
        assertEquals("Month of Year" + dateForFailMsg, expected.month, ISLAMIC_UTC.monthOfYear().get(instant));
        assertEquals("Day of Month" + dateForFailMsg, expected.dayOfMonth, ISLAMIC_UTC.dayOfMonth().get(instant));
        assertEquals("Day of Year" + dateForFailMsg, expected.dayOfYear, ISLAMIC_UTC.dayOfYear().get(instant));
        assertEquals("Day of Week" + dateForFailMsg, expected.dayOfWeek, ISLAMIC_UTC.dayOfWeek().get(instant));
    }

    /**
     * The default IslamicChronology uses the 16-based leap year pattern.
     * This method implements a well-known arithmetic rule for that pattern:
     * "A year is a leap year if (11 * year + 14) % 30 < 11".
     * We test the chronology's implementation against this independent rule.
     */
    private boolean isLeapYearAccordingTo16BasedRule(int year) {
        return ((11 * year + 14) % 30) < 11;
    }

    private void verifyMonthAndYearLength(long instant, int month, boolean isLeap, int monthLength) {
        // Test month length based on Islamic calendar rules (odd months have 30 days, even have 29)
        switch (month) {
            case 1: case 3: case 5: case 7: case 9: case 11:
                assertEquals(30, monthLength);
                break;
            case 2: case 4: case 6: case 8: case 10:
                assertEquals(29, monthLength);
                break;
            case 12: // Final month has 30 days in a leap year, 29 otherwise
                assertEquals(isLeap ? 30 : 29, monthLength);
                break;
            default:
                fail("Invalid month: " + month);
        }

        // Test year length
        int expectedYearLength = isLeap ? 355 : 354;
        assertEquals(expectedYearLength, ISLAMIC_UTC.dayOfYear().getMaximumValue(instant));
    }

    private ExpectedDate calculateNextExpectedDate(ExpectedDate current, int monthLength) {
        ExpectedDate next = new ExpectedDate();
        
        // Increment day of week, cycling from 7 (Sunday) back to 1 (Monday)
        next.dayOfWeek = (current.dayOfWeek % 7) + 1;

        next.dayOfYear = current.dayOfYear + 1;
        next.dayOfMonth = current.dayOfMonth + 1;
        next.month = current.month;
        next.year = current.year;

        // Handle month and year rollovers
        if (next.dayOfMonth > monthLength) {
            next.dayOfMonth = 1;
            next.month++;
            if (next.month > 12) {
                next.month = 1;
                next.dayOfYear = 1; // Reset day of year
                next.year++;
            }
        }
        return next;
    }
}