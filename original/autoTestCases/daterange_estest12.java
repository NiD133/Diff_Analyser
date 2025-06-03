package org.example;

import org.junit.jupiter.api.Test;  // Use JUnit 5 annotations
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals; // Use JUnit 5 assertions

public class DateRangeTest { // More descriptive class name

    @Test
    public void testGetLowerDateReturnsCorrectDate() {  // Clearer method name
        // Arrange: Create a DateRange object with a range starting at 0.0
        DateRange dateRange = new DateRange(0.0, 10.0); // Give an upper bound for clarity

        // Act: Get the lower date from the DateRange
        Date lowerDate = dateRange.getLowerDate();

        // Assert: Verify that the lower date is January 1, 1970 (the epoch)
        // The comparison uses getTime() to compare the milliseconds since epoch, 
        // which is more reliable and less dependent on locale settings for String representation.
        assertEquals(0L, lowerDate.getTime(), "The lower date should be the epoch (Jan 1, 1970 00:00:00 GMT).");  // More specific assertion message
    }
}