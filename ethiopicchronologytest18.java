package org.joda.time.chrono;

import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;

/**
 * Tests the leap year properties of the EthiopicChronology.
 */
public class EthiopicChronologyLeapYearTest extends TestCase {

    /**
     * Tests the isLeap() method on different date fields for a date that falls within a leap year.
     * In the Ethiopic calendar, a year is a leap year if it is divisible by 4, and year 3 is
     * one such leap year. The 13th month is an intercalary month that contains the leap day.
     */
    public void testLeapPropertiesOnDateInLeapYear() {
        // Arrange: Define a date within a known leap year in the Ethiopic calendar.
        final int ETHIOPIC_LEAP_YEAR = 3;
        final int INTERCALARY_MONTH_OF_YEAR = 13; // The 13th month contains the leap day.
        final int DAY_OF_MONTH = 5;

        Chronology ethiopicChronology = EthiopicChronology.getInstance();
        DateTime date = new DateTime(ETHIOPIC_LEAP_YEAR, INTERCALARY_MONTH_OF_YEAR, DAY_OF_MONTH, 0, 0, ethiopicChronology);

        // Act & Assert: Verify the leap properties of the date's fields.

        // The year property should be leap because Ethiopic year 3 is a leap year.
        assertTrue("Year 3 should be identified as a leap year", date.year().isLeap());

        // The month property should be leap because the 13th month is where the leap day is added.
        assertTrue("Month 13 should be identified as a leap month", date.monthOfYear().isLeap());

        // The day of month is just a position within the month and is never considered leap itself.
        assertFalse("The day of the month field should not be leap", date.dayOfMonth().isLeap());

        // The day of year is just a position within the year and is never considered leap itself.
        assertFalse("The day of the year field should not be leap", date.dayOfYear().isLeap());
    }
}