package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests for CopticChronology.
 * This test focuses on the core calendar logic by iterating through a long range of dates.
 */
public class CopticChronologyTest {

    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    /**
     * This test validates the CopticChronology by iterating day-by-day
     * from the Coptic epoch (Year 1, Month 1, Day 1) up to the
     * equivalent of ISO year 3000. On each day, it asserts that the
     * chronology's date fields match a set of manually calculated expected values.
     * This approach, while complex, thoroughly verifies the calendar's internal logic.
     */
    @Test
    public void testCopticChronologyByIteratingThroughDays() {
        // Define Coptic calendar rules as constants for clarity.
        final int DAYS_IN_REGULAR_MONTH = 30;
        final int DAYS_IN_LEAP_INTERCALARY_MONTH = 6;
        final int DAYS_IN_NORMAL_INTERCALARY_MONTH = 5;
        final int INTERCALARY_MONTH = 13;

        // Get the date fields from the chronology once for efficiency.
        final DateTimeField era = COPTIC_UTC.era();
        final DateTimeField year = COPTIC_UTC.year();
        final DateTimeField yearOfEra = COPTIC_UTC.yearOfEra();
        final DateTimeField monthOfYear = COPTIC_UTC.monthOfYear();
        final DateTimeField dayOfMonth = COPTIC_UTC.dayOfMonth();
        final DateTimeField dayOfYear = COPTIC_UTC.dayOfYear();
        final DateTimeField dayOfWeek = COPTIC_UTC.dayOfWeek();

        // Set the start and end points for the test iteration.
        long startMillis = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC).getMillis();
        long endMillis = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();

        // Initialize expected date values for the start of the Coptic epoch.
        int expectedYear = 1;
        int expectedMonth = 1;
        int expectedDay = 1;
        int expectedDayOfYear = 1;
        // Coptic year 1, day 1 corresponds to August 29, 284 CE in the Julian calendar.
        // We use the Julian calendar to establish the correct starting day of the week.
        int expectedDayOfWeek = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();

        long currentMillis = startMillis;
        while (currentMillis < endMillis) {
            // 1. VERIFY the current date using the chronology's fields.
            assertCopticDate(currentMillis, era, year, yearOfEra, monthOfYear, dayOfMonth, dayOfYear, dayOfWeek,
                    expectedYear, expectedMonth, expectedDay, expectedDayOfYear, expectedDayOfWeek);

            // Verify leap year and month length logic.
            boolean isCopticLeapYear = (expectedYear % 4 == 3);
            assertEquals("isLeapYear", isCopticLeapYear, year.isLeap(currentMillis));

            if (expectedMonth == INTERCALARY_MONTH) {
                assertEquals("isLeapMonth", isCopticLeapYear, monthOfYear.isLeap(currentMillis));
                int expectedMonthLength = isCopticLeapYear ? DAYS_IN_LEAP_INTERCALARY_MONTH : DAYS_IN_NORMAL_INTERCALARY_MONTH;
                assertEquals("monthLength", expectedMonthLength, dayOfMonth.getMaximumValue(currentMillis));
            } else {
                assertEquals("monthLength", DAYS_IN_REGULAR_MONTH, dayOfMonth.getMaximumValue(currentMillis));
            }

            // 2. ADVANCE to the next day by recalculating expected values.
            expectedDayOfWeek = (expectedDayOfWeek % 7) + 1; // Cycle from 1-7
            expectedDay++;
            expectedDayOfYear++;

            // Check for month/year rollovers.
            boolean isLastDayOfMonth = false;
            if (expectedMonth < INTERCALARY_MONTH) {
                if (expectedDay > DAYS_IN_REGULAR_MONTH) {
                    isLastDayOfMonth = true;
                }
            } else { // We are in the 13th (intercalary) month.
                int daysInThisMonth = isCopticLeapYear ? DAYS_IN_LEAP_INTERCALARY_MONTH : DAYS_IN_NORMAL_INTERCALARY_MONTH;
                if (expectedDay > daysInThisMonth) {
                    isLastDayOfMonth = true;
                }
            }

            if (isLastDayOfMonth) {
                expectedDay = 1;
                expectedMonth++;
                if (expectedMonth > INTERCALARY_MONTH) {
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDayOfYear = 1;
                }
            }

            // 3. INCREMENT the millisecond timestamp to the next day.
            currentMillis += DateTimeConstants.MILLIS_PER_DAY;
        }
    }

    private void assertCopticDate(long currentMillis, DateTimeField era, DateTimeField year,
                                  DateTimeField yearOfEra, DateTimeField monthOfYear, DateTimeField dayOfMonth,
                                  DateTimeField dayOfYear, DateTimeField dayOfWeek, int expectedYear,
                                  int expectedMonth, int expectedDay, int expectedDayOfYear, int expectedDayOfWeek) {

        // Era is always 1 (AM - Anno Martyrum).
        assertEquals("era", 1, era.get(currentMillis));
        assertEquals("eraAsText", "AM", era.getAsText(currentMillis));

        // Verify year, month, and day fields.
        int actualMonth = monthOfYear.get(currentMillis);
        assertTrue("Month " + actualMonth + " is out of range (1-13)", actualMonth >= 1 && actualMonth <= 13);

        assertEquals("year", expectedYear, year.get(currentMillis));
        assertEquals("yearOfEra", expectedYear, yearOfEra.get(currentMillis));
        assertEquals("monthOfYear", expectedMonth, actualMonth);
        assertEquals("dayOfMonth", expectedDay, dayOfMonth.get(currentMillis));
        assertEquals("dayOfYear", expectedDayOfYear, dayOfYear.get(currentMillis));
        assertEquals("dayOfWeek", expectedDayOfWeek, dayOfWeek.get(currentMillis));
    }
}