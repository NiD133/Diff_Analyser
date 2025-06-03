package org.jfree.data.time;

import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateRangeImmutabilityTest { // Renamed class for clarity

    @Test
    public void testDateRangeIsImmutable() { // Renamed method for clarity
        // Arrange:  Create initial Dates and a DateRange
        Date initialLowerDate = new Date(10L);
        Date initialUpperDate = new Date(20L);
        DateRange dateRange = new DateRange(initialLowerDate, initialUpperDate);

        // Act: Attempt to modify the original Date objects after the DateRange is created
        initialLowerDate.setTime(11L); // Modify the lower Date
        dateRange.getUpperDate().setTime(22L); // Modify the upper Date obtained from the DateRange

        // Assert: Verify that the DateRange still holds the original dates, proving immutability
        assertEquals(new Date(10L), dateRange.getLowerDate(), "Lower date should remain unchanged."); //Added descriptive message
        assertEquals(new Date(20L), dateRange.getUpperDate(), "Upper date should remain unchanged."); //Added descriptive message
    }
}