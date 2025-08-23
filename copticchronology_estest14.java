package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for the CopticChronology class.
 * The original test was auto-generated and has been improved for clarity.
 */
public class CopticChronology_ESTestTest14 extends CopticChronology_ESTest_scaffolding {

    /**
     * Tests that isLeapDay() returns false for a date that is not the Coptic leap day.
     * The Coptic leap day is the 6th epagomenal day (the last day of the 13th month),
     * which only occurs in a Coptic leap year.
     */
    @Test
    public void isLeapDay_shouldReturnFalse_forNonLeapDay() {
        // Arrange: Create a CopticChronology instance in a fixed timezone (UTC).
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();

        // An arbitrary instant that is known not to be a Coptic leap day.
        // The original test used the magic number 16965676800000L, which corresponds
        // to 2507-09-05T04:00:00.000Z in the Gregorian calendar.
        DateTime nonLeapDayDate = new DateTime(2507, 9, 5, 4, 0, DateTimeZone.UTC);

        // Act: Check if the given instant is a leap day.
        boolean isLeapDay = copticChronology.isLeapDay(nonLeapDayDate.getMillis());

        // Assert: Verify that the result is false.
        assertFalse("The date " + nonLeapDayDate + " should not be a leap day in the Coptic calendar.", isLeapDay);
    }
}