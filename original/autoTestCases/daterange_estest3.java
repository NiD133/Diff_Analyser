package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jfree.data.Range;
import java.util.Date;

public class UnderstandableDateRangeTest {

    @Test
    public void testDateRangeInitializationFromExpandedNullRange() {
        // Arrange: Create a Range by expanding a null range to include 1801.7621.
        //          This effectively creates a Range with both lower and upper bounds set to 1801.7621.
        Range range = Range.expandToInclude(null, 1801.7621);

        // Act: Create a DateRange object using the generated Range.  This DateRange
        //      will represent a date range with its boundaries corresponding to the
        //      numeric values of the Range object, treated as milliseconds since the epoch.
        DateRange dateRange = new DateRange(range);

        // Assert: Verify that the lower bound of the DateRange, expressed in milliseconds,
        //         is equal to 1801.  We're expecting an integer truncation here since the
        //         original value was 1801.7621.
        assertEquals(1801L, dateRange.getLowerMillis());

        // Assert: Verify that the upper bound of the DateRange, expressed in milliseconds,
        //         is also equal to 1801.  This confirms that the DateRange was constructed
        //         as expected from the Range object.
        assertEquals(1801L, dateRange.getUpperMillis());
    }
}