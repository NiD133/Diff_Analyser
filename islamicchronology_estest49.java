package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    @Test
    public void setYear_shouldChangeYearWhilePreservingMonthDayAndTime() {
        // Arrange
        // To ensure the test is deterministic, we use the UTC time zone.
        IslamicChronology islamicChronology = IslamicChronology.getInstance(DateTimeZone.UTC);

        // The original test used the millisecond value 2602045900800000L.
        // In the Islamic calendar (UTC), this corresponds to the date 82035-12-11.
        DateTime initialDateTime = new DateTime(82035, 12, 11, 0, 0, 0, 0, islamicChronology);
        
        int newYear = 46;

        // The expected result should have the year changed to 46,
        // while preserving the month, day, and time of day.
        DateTime expectedDateTime = new DateTime(newYear, 12, 11, 0, 0, 0, 0, islamicChronology);

        // Act
        long resultMillis = islamicChronology.setYear(initialDateTime.getMillis(), newYear);

        // Assert
        assertEquals(expectedDateTime.getMillis(), resultMillis);
    }
}