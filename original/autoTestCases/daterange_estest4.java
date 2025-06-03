package org.example;

import org.junit.jupiter.api.Test; // Modern JUnit 5 annotation
import static org.junit.jupiter.api.Assertions.*; // Modern JUnit 5 assertions

public class DateRangeTest { // Renamed class for clarity, making it a test class

    @Test
    void testGetLowerMillisForSameDates() {
        // Arrange: Create a DateRange object with the same start and end dates.
        double sameDateValue = -1157.7534;
        DateRange dateRange = new DateRange(sameDateValue, sameDateValue);

        // Act: Get the lower millisecond value from the DateRange object.
        long lowerMillis = dateRange.getLowerMillis();

        // Assert: Verify that the lower millisecond value is as expected (truncated double).
        assertEquals(-1157L, lowerMillis, "The lower millisecond value should be -1157.");

        // Assert: Verify that the upper millisecond value is also the same (as the dates are the same).
        assertEquals(-1157L, dateRange.getUpperMillis(), "The upper millisecond value should also be -1157.");
    }
}