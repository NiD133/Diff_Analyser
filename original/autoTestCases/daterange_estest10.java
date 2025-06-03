package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 for better readability
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static org.junit.jupiter.api.Assertions.*; // More readable assertions

public class DateRangeTest { // More descriptive class name

    @Test
    public void testDateRangeCreationWithSameDates() {
        // Arrange: Create a specific date for testing.  Using Calendar provides more control.
        Calendar calendar = new GregorianCalendar(1974, Calendar.OCTOBER, 1); // Year, Month (0-indexed), Day
        Date date = calendar.getTime();

        // Act: Create a DateRange object where the start and end dates are the same.
        DateRange dateRange = new DateRange(date, date);

        // Assert: Verify that the DateRange object represents a valid, non-NaN range.
        assertFalse(dateRange.isNaNRange(), "The date range should not be NaN.");
    }
}