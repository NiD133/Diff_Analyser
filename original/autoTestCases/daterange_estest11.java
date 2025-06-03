package org.example;

import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.assertEquals;

public class DateRangeTest {

    @Test
    public void testGetUpperDateReturnsCorrectDate() {
        // Arrange: Create a DateRange object with the same start and end values (0.0).
        // This likely represents a date range spanning a single point in time.
        DateRange dateRange = new DateRange(0.0, 0.0);

        // Act: Retrieve the upper date (end date) from the DateRange object.
        Date upperDate = dateRange.getUpperDate();

        // Assert:  Check if the retrieved upper date matches the expected date,
        // which should be January 1, 1970, 00:00:00 GMT, representing the epoch.
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", upperDate.toString());
    }
}