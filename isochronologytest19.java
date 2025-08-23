package org.joda.time.chrono;

import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Tests for leap year and leap day handling in {@link ISOChronology}.
 */
public class ISOChronologyLeapYearTest {

    /**
     * Verifies that for a date on a leap day (Feb 29th), the relevant date fields
     * correctly identify themselves as being part of a leap context.
     */
    @Test
    public void isLeap_shouldReturnTrue_forFieldsOfLeapDay() {
        // Arrange: Create a date for February 29th in a known leap year (2012).
        // We use the UTC chronology to make the test independent of the system's default time zone.
        final Chronology isoChronology = ISOChronology.getInstanceUTC();
        final DateTime leapDayDateTime = new DateTime(2012, 2, 29, 0, 0, isoChronology);

        // Act & Assert: Check the 'isLeap' property on various fields of the date.

        // The year 2012 is a leap year.
        assertTrue("The year field (2012) should be leap.", leapDayDateTime.year().isLeap());

        // The month of year (February) contains the leap day.
        assertTrue("The month of year field (February) should be leap.", leapDayDateTime.monthOfYear().isLeap());

        // The day of month (29th) is the leap day itself.
        assertTrue("The day of month field (29th) should be leap.", leapDayDateTime.dayOfMonth().isLeap());

        // The day of year (60th day) is the leap day.
        assertTrue("The day of year field should be leap.", leapDayDateTime.dayOfYear().isLeap());
    }
}