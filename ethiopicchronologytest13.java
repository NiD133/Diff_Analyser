package org.joda.time.chrono;

import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;

/**
 * Contains tests for the properties and behavior of EthiopicChronology.
 * This class replaces a single, large and complex test method with a collection
 * of small, focused tests, each verifying a specific rule of the calendar system.
 */
public class EthiopicChronologyTest extends TestCase {

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();

    /**
     * The Ethiopic calendar has one era: EE (Ethiopic Era), which is equivalent to CE.
     */
    public void testEra() {
        DateTime date = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals("Era should be EE", EthiopicChronology.EE, date.getEra());
        assertEquals("Era text should be 'EE'", "EE", date.era().getAsText());
    }

    /**
     * An Ethiopic year is a leap year if (year % 4) == 3.
     */
    public void testLeapYearRule() {
        // Year 3 is a leap year in the Ethiopic calendar
        assertTrue("Year 3 should be a leap year", isLeapYear(3));
        
        // Years 1, 2, and 4 are not leap years
        assertFalse("Year 1 should not be a leap year", isLeapYear(1));
        assertFalse("Year 2 should not be a leap year", isLeapYear(2));
        assertFalse("Year 4 should not be a leap year", isLeapYear(4));
    }

    /**
     * The first 12 months of an Ethiopic year must always have 30 days.
     */
    public void testStandardMonthLength() {
        // Test in a non-leap year to be sure
        int commonYear = 1990; // 1990 % 4 = 2
        for (int month = 1; month <= 12; month++) {
            DateTime date = new DateTime(commonYear, month, 1, 0, 0, ETHIOPIC_UTC);
            assertEquals("Month " + month + " should have 30 days", 30, date.dayOfMonth().getMaximumValue());
        }
    }

    /**
     * The 13th month of a common (non-leap) Ethiopic year has 5 days.
     */
    public void testLastMonthLength_inCommonYear() {
        int commonYear = 1990; // 1990 % 4 = 2
        DateTime dateInLastMonth = new DateTime(commonYear, 13, 1, 0, 0, ETHIOPIC_UTC);
        
        assertFalse("Year " + commonYear + " should not be a leap year", isLeapYear(commonYear));
        assertEquals("The 13th month of a common year should have 5 days", 5, dateInLastMonth.dayOfMonth().getMaximumValue());
    }

    /**
     * The 13th month of a leap Ethiopic year has 6 days.
     */
    public void testLastMonthLength_inLeapYear() {
        int leapYear = 1991; // 1991 % 4 = 3
        DateTime dateInLastMonth = new DateTime(leapYear, 13, 1, 0, 0, ETHIOPIC_UTC);
        
        assertTrue("Year " + leapYear + " should be a leap year", isLeapYear(leapYear));
        assertEquals("The 13th month of a leap year should have 6 days", 6, dateInLastMonth.dayOfMonth().getMaximumValue());
    }

    /**
     * Verifies the date components for the Ethiopic epoch (Year 1, Month 1, Day 1).
     */
    public void testDateFields_atEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(1, epoch.getYear());
        assertEquals(1, epoch.getMonthOfYear());
        assertEquals(1, epoch.getDayOfMonth());
        assertEquals(1, epoch.getDayOfYear());
        assertEquals(DateTimeConstants.SUNDAY, epoch.getDayOfWeek());
    }

    /**
     * Tests that adding one day correctly rolls over month boundaries.
     */
    public void testDateProgression_acrossMonthBoundary() {
        // From the last day of a standard 30-day month
        DateTime lastDayOfMonth = new DateTime(1990, 5, 30, 10, 20, 30, 0, ETHIOPIC_UTC);
        DateTime firstDayOfNextMonth = lastDayOfMonth.plusDays(1);

        assertEquals(1990, firstDayOfNextMonth.getYear());
        assertEquals(6, firstDayOfNextMonth.getMonthOfYear());
        assertEquals(1, firstDayOfNextMonth.getDayOfMonth());
        
        // Time components should be unchanged
        assertEquals(10, firstDayOfNextMonth.getHourOfDay());
    }

    /**
     * Tests that adding one day correctly rolls over the year boundary for a common year.
     */
    public void testDateProgression_acrossCommonYearBoundary() {
        int commonYear = 1990; // Last day is 13-05
        DateTime lastDayOfYear = new DateTime(commonYear, 13, 5, 10, 20, 30, 0, ETHIOPIC_UTC);
        DateTime firstDayOfNextYear = lastDayOfYear.plusDays(1);

        assertEquals(commonYear + 1, firstDayOfNextYear.getYear());
        assertEquals(1, firstDayOfNextYear.getMonthOfYear());
        assertEquals(1, firstDayOfNextYear.getDayOfMonth());
    }

    /**
     * Tests that adding one day correctly rolls over the year boundary for a leap year.
     */
    public void testDateProgression_acrossLeapYearBoundary() {
        int leapYear = 1991; // Last day is 13-06
        DateTime lastDayOfYear = new DateTime(leapYear, 13, 6, 10, 20, 30, 0, ETHIOPIC_UTC);
        DateTime firstDayOfNextYear = lastDayOfYear.plusDays(1);

        assertEquals(leapYear + 1, firstDayOfNextYear.getYear());
        assertEquals(1, firstDayOfNextYear.getMonthOfYear());
        assertEquals(1, firstDayOfNextYear.getDayOfMonth());
    }

    private boolean isLeapYear(int year) {
        // Helper to check leap year status based on a DateTime object for that year.
        DateTime aDateInTheYear = new DateTime(year, 1, 1, 0, 0, ETHIOPIC_UTC);
        return ETHIOPIC_UTC.year().isLeap(aDateInTheYear.getMillis());
    }
}