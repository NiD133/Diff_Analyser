package org.joda.time.chrono;

import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Tests for leap year/day/month properties in EthiopicChronology.
 */
public class EthiopicChronologyTest {

    /**
     * Verifies that for a date representing the leap day in the Ethiopic calendar,
     * the relevant date-time fields correctly identify themselves as being part of a leap period.
     */
    @Test
    public void isLeap_onLeapDay_returnsTrueForAllRelevantFields() {
        // Arrange
        // Use a specific, non-default chronology to make the test deterministic and
        // independent of the local system's time zone.
        final Chronology ethiopicUTC = EthiopicChronology.getInstanceUTC();

        // According to the Ethiopic calendar rules (similar to Julian), year 3 is a leap year.
        // A leap year has a 13th month with 6 days. The 6th day of this "leap month"
        // is the actual leap day.
        final int leapYear = 3;
        final int leapMonth = 13;
        final int leapDayOfMonth = 6;
        DateTime ethiopicLeapDay = new DateTime(leapYear, leapMonth, leapDayOfMonth, 0, 0, ethiopicUTC);

        // Act & Assert
        // For a date that is the leap day, the corresponding year, month, and day fields
        // should all report as being 'leap'.

        assertTrue("Year containing a leap day should be identified as a leap year.",
                   ethiopicLeapDay.year().isLeap());

        assertTrue("The 13th month of a leap year should be identified as a leap month.",
                   ethiopicLeapDay.monthOfYear().isLeap());

        assertTrue("The 6th day of the 13th month should be identified as a leap day.",
                   ethiopicLeapDay.dayOfMonth().isLeap());

        assertTrue("The 366th day of a leap year should be identified as a leap day of the year.",
                   ethiopicLeapDay.dayOfYear().isLeap());
    }
}