package org.joda.time;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Tests the sorting behavior of DateTimeComparator.
 */
public class DateTimeComparatorSortingTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Tests that a list of DateTime objects is correctly sorted
     * using a comparator that only considers the day of the week.
     */
    @Test
    public void testSortsByDayOfWeek() {
        // Arrange: Create DateTime objects for a full week.
        // Note: 2002-04-15 is a Monday.
        DateTime monday = new DateTime(2002, 4, 15, 10, 0, 0, UTC);
        DateTime tuesday = new DateTime(2002, 4, 16, 10, 0, 0, UTC);
        DateTime wednesday = new DateTime(2002, 4, 17, 10, 0, 0, UTC);
        DateTime thursday = new DateTime(2002, 4, 18, 10, 0, 0, UTC);
        DateTime friday = new DateTime(2002, 4, 19, 10, 0, 0, UTC);
        DateTime saturday = new DateTime(2002, 4, 20, 10, 0, 0, UTC);
        DateTime sunday = new DateTime(2002, 4, 21, 10, 0, 0, UTC);

        // Create a list of dates in a non-sequential order.
        List<DateTime> datesToSort = new ArrayList<>(Arrays.asList(
            sunday, tuesday, monday, wednesday, friday, thursday, saturday
        ));

        // Define the expected order after sorting by day of the week (Monday-first).
        List<DateTime> expectedOrder = Arrays.asList(
            monday, tuesday, wednesday, thursday, friday, saturday, sunday
        );

        // Create a comparator that only considers the day of the week.
        // The upper limit (weekOfWeekyear) ensures that larger fields like year or month are ignored.
        Comparator<Object> dayOfWeekComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.dayOfWeek(), DateTimeFieldType.weekOfWeekyear());

        // Act: Sort the list using the custom comparator.
        Collections.sort(datesToSort, dayOfWeekComparator);

        // Assert: Verify that the list is sorted in the expected order.
        assertEquals("List should be sorted by day of week", expectedOrder, datesToSort);
    }
}