package org.joda.time.chrono;

import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;

/**
 * Tests the isLeap() behavior of various date-time fields within the CopticChronology.
 */
public class CopticChronologyLeapTest extends TestCase {

    /**
     * The Coptic calendar has a leap year every 4 years, similar to the Julian calendar.
     * Year 3 is a leap year in this cycle. This test verifies the leap properties
     * of a date within that leap year.
     */
    public void testIsLeap_forDateInLeapYear() {
        // Test data setup
        final int COPTIC_LEAP_YEAR = 3;
        final int INTERCALARY_MONTH = 13; // The 13th month contains the leap day
        final int DAY_OF_MONTH = 5;

        // In the Coptic calendar, the 13th month has 5 days in a common year
        // and 6 days in a leap year.
        Chronology copticChronology = CopticChronology.getInstance();
        DateTime dateInLeapYear = new DateTime(
            COPTIC_LEAP_YEAR,
            INTERCALARY_MONTH,
            DAY_OF_MONTH,
            0, 0,
            copticChronology
        );

        // Assertions
        assertTrue("Year 3 should be a leap year.",
            dateInLeapYear.year().isLeap());

        assertTrue("The 13th month is considered 'leap' as it contains the extra day in a leap year.",
            dateInLeapYear.monthOfYear().isLeap());

        assertFalse("Day 5 of the 13th month is not a leap day itself (only the 6th day is).",
            dateInLeapYear.dayOfMonth().isLeap());

        assertFalse("The 365th day of the year is not the leap day (the 366th is).",
            dateInLeapYear.dayOfYear().isLeap());
    }
}