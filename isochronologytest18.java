package org.joda.time.chrono;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Tests the behavior of leap year properties within the ISOChronology.
 * This test focuses on the isLeap() method for various DateTimeFields.
 */
public class ISOChronologyLeapYearTest {

    /**
     * Verifies that the isLeap() method on different fields of a DateTime
     * returns the correct value for a date within a known leap year.
     *
     * According to Joda-Time logic:
     * - The year field is leap if the year has 366 days.
     * - The monthOfYear field is leap if it's February in a leap year.
     * - Day-based fields are never considered leap.
     */
    @Test
    public void isLeap_forFieldsOfDateInLeapYear_returnsCorrectResults() {
        // Arrange: A date in a leap year (2012).
        // Using February 28th to show the month is leap even before the 29th.
        final DateTime dateInLeapYear = new DateTime(2012, 2, 28, 0, 0, ISOChronology.getInstanceUTC());

        // Act & Assert: Check the isLeap() status for different parts of the date.

        // The year 2012 is a leap year.
        assertTrue("The year field (2012) should be considered leap.",
                dateInLeapYear.year().isLeap());

        // In a leap year, the month of February is considered "leap".
        assertTrue("The month field (February) should be considered leap in a leap year.",
                dateInLeapYear.monthOfYear().isLeap());

        // A specific day is never a leap unit itself.
        assertFalse("The dayOfMonth field should never be considered leap.",
                dateInLeapYear.dayOfMonth().isLeap());

        // A specific day of the year is never a leap unit itself.
        assertFalse("The dayOfYear field should never be considered leap.",
                dateInLeapYear.dayOfYear().isLeap());
    }
}