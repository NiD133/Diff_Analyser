package org.jfree.data.time;

import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateRangeEqualsTest { // Renamed class for clarity

    @Test
    public void testEquals_SameDates_ReturnsTrue() {
        // Arrange: Create two DateRange objects with the same start and end dates.
        Date startDate = new Date(1000L);
        Date endDate = new Date(2000L);
        DateRange range1 = new DateRange(startDate, endDate);
        DateRange range2 = new DateRange(startDate, endDate);

        // Act:  (Implicit in the assertEquals call) Compare the two DateRange objects.

        // Assert:  Verify that the two DateRange objects are considered equal.
        assertEquals(range1, range2, "DateRanges with the same start and end dates should be equal.");
    }

    @Test
    public void testEquals_DifferentStartDates_ReturnsFalse() {
        // Arrange: Create two DateRange objects with different start dates but the same end date.
        Date startDate1 = new Date(1111L);
        Date startDate2 = new Date(1000L);
        Date endDate = new Date(2000L);
        DateRange range1 = new DateRange(startDate1, endDate);
        DateRange range2 = new DateRange(startDate2, endDate);

        // Act: (Implicit in the assertNotEquals call) Compare the two DateRange objects.

        // Assert: Verify that the two DateRange objects are considered not equal.
        assertNotEquals(range1, range2, "DateRanges with different start dates should not be equal.");
    }

    @Test
    public void testEquals_DifferentEndDates_ReturnsFalse() {
        // Arrange: Create two DateRange objects with the same start date but different end dates.
        Date startDate = new Date(1111L);
        Date endDate1 = new Date(2222L);
        Date endDate2 = new Date(2000L);

        DateRange range1 = new DateRange(startDate, endDate1);
        DateRange range2 = new DateRange(startDate, endDate2);

        // Act: (Implicit in the assertNotEquals call) Compare the two DateRange objects.

        // Assert: Verify that the two DateRange objects are considered not equal.
        assertNotEquals(range1, range2, "DateRanges with different end dates should not be equal.");
    }

    @Test
    public void testEquals_BothDatesDifferent_ReturnsFalse() {
        // Arrange: Create two DateRange objects with different start and end dates.
        Date startDate1 = new Date(1000L);
        Date endDate1 = new Date(2000L);
        Date startDate2 = new Date(1111L);
        Date endDate2 = new Date(2222L);

        DateRange range1 = new DateRange(startDate1, endDate1);
        DateRange range2 = new DateRange(startDate2, endDate2);

        // Act: (Implicit in the assertNotEquals call) Compare the two DateRange objects.

        // Assert: Verify that the two DateRange objects are considered not equal.
        assertNotEquals(range1, range2, "DateRanges with different start and end dates should not be equal.");
    }
}