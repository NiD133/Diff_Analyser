package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for better readability and features
import static org.junit.jupiter.api.Assertions.*; // Updated assertions
import java.util.Date;

public class GeneratedTestCase {

    @Test
    void testDefaultDateRangeLowerBound() {
        // Arrange: Create a DateRange object with default constructor (representing a default range).
        DateRange dateRange = new DateRange();

        // Act: Get the lower bound (in milliseconds) of the date range.
        long lowerBoundMillis = dateRange.getLowerMillis();

        // Assert: Verify that the lower bound is 0 milliseconds (epoch start).
        assertEquals(0L, lowerBoundMillis, "The lower bound of a default DateRange should be 0 milliseconds (epoch start).");

        // Additionally, verify that the upper bound is 1 (to ensure the DateRange is initialized correctly).
        assertEquals(1L, dateRange.getUpperMillis(), "The upper bound of a default DateRange should be 1 millisecond.");
    }
}