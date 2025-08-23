package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology#setYear(long, int)} method.
 */
public class IslamicChronologySetYearTest {

    /**
     * Tests that setYear correctly changes the year of a given instant,
     * while preserving the month, day, and time-of-day fields.
     */
    @Test
    public void setYear_shouldChangeYearWhilePreservingMonthDayAndTime() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstance(DateTimeZone.UTC);

        // Define an initial date in the Islamic calendar: 15th of Shawwal, 1435 AH at noon.
        DateTime initialDateTime = new DateTime(1435, 10, 15, 12, 0, 0, 0, islamicChronology);
        
        // Define the target year we want to set.
        int targetYear = 1440;

        // Define the expected result: the same date and time, but in the target year.
        DateTime expectedDateTime = new DateTime(targetYear, 10, 15, 12, 0, 0, 0, islamicChronology);

        // Act
        // Call the setYear method with the initial instant and the target year.
        long actualMillis = islamicChronology.setYear(initialDateTime.getMillis(), targetYear);

        // Assert
        // Verify that the resulting millisecond instant matches the expected DateTime.
        assertEquals(expectedDateTime.getMillis(), actualMillis);
    }
}