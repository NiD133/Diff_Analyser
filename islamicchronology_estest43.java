package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The original test was auto-generated, which resulted in a non-descriptive name ("test42")
// and a "magic number" for the date instant (2602780732800000L).
// This improved version clarifies the test's intent and makes it self-documenting.
public class IslamicChronology_ESTestTest43 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests that getDaysInMonthMax(long instant) returns 30 for any instant
     * that falls within an odd-numbered month of the Islamic calendar.
     * <p>
     * In the Islamic calendar, odd-numbered months (1, 3, 5, etc.) always have 30 days.
     * The original test's magic instant fell in the first month. This test uses a
     * clearly defined date in the third month to verify the same logic.
     */
    @Test
    public void getDaysInMonthMax_forInstantInOddMonth_shouldReturn30() {
        // Arrange
        // Use the UTC zone for test stability, avoiding dependency on the system's default time zone.
        IslamicChronology chronology = IslamicChronology.getInstance(DateTimeZone.UTC);

        // Create a representative instant that falls within an odd-numbered month.
        // Here, we use a date in the 3rd month of the Islamic year 1435.
        // This replaces the original test's opaque magic number (2602780732800000L).
        DateTime dateTimeInOddMonth = new DateTime(1435, 3, 1, 0, 0, chronology);
        long instant = dateTimeInOddMonth.getMillis();

        // Act
        int maxDaysInMonth = chronology.getDaysInMonthMax(instant);

        // Assert
        // The maximum number of days for any odd-numbered month in the Islamic calendar is 30.
        assertEquals("Odd-numbered months in the Islamic calendar should have a maximum of 30 days.", 30, maxDaysInMonth);
    }
}