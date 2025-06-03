package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 annotations
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

import java.util.Date; // Necessary for DateRange interaction
import org.junit.jupiter.api.DisplayName; // For adding descriptive names to tests


public class DateRangeTest { // Renamed class for clarity (and JUnit 5 convention)

    @Test
    @DisplayName("Test Case: Default DateRange Upper Bound is 1 Millisecond")
    public void testDefaultDateRangeUpperBound() {
        // Arrange: Create a DateRange object using the default constructor
        DateRange dateRange = new DateRange();

        // Act: Get the upper bound of the DateRange in milliseconds
        long upperBoundMillis = dateRange.getUpperMillis();

        // Assert: Verify that the upper bound is 1 millisecond (as expected for the default constructor).
        assertEquals(1L, upperBoundMillis, "The upper bound of a default DateRange should be 1 millisecond.");
    }

    @Test
    @DisplayName("Test Case: Default DateRange Lower Bound is 0 Milliseconds")
    public void testDefaultDateRangeLowerBound() {
        // Arrange: Create a default DateRange object.
        DateRange dateRange = new DateRange();

        // Act: Get the lower bound of the DateRange.
        long lowerBoundMillis = dateRange.getLowerMillis();

        // Assert: Verify that the lower bound is 0 milliseconds.
        assertEquals(0L, lowerBoundMillis, "The lower bound of a default DateRange should be 0 milliseconds.");
    }
}