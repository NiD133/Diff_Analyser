package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class DateRangeTest {

    @Test
    public void testDefaultDateRangeCreation() {
        // Arrange: Create a default DateRange object
        DateRange dateRange = new DateRange();

        // Act: Retrieve the lower and upper milliseconds and the string representation.
        long lowerMillis = dateRange.getLowerMillis();
        long upperMillis = dateRange.getUpperMillis();
        String stringRepresentation = dateRange.toString();

        // Assert:  Verify the expected values for a newly created DateRange.
        // The default DateRange should be initialized to Jan 1, 1970 00:00:00 AM for both lower and upper bounds.
        assertEquals(0L, lowerMillis, "Lower millisecond bound should be 0.");
        assertEquals(1L, upperMillis, "Upper millisecond bound should be 1.");
        assertEquals("[Jan 1, 1970 12:00:00 AM --> Jan 1, 1970 12:00:00 AM]", stringRepresentation, "String representation should match the default date range.");
    }
}