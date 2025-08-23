package org.joda.time;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Helper method to create DateTime objects in the UTC time zone for consistent test data.
     */
    private DateTime createUtcDateTime(String isoDateTime) {
        return new DateTime(isoDateTime, DateTimeZone.UTC);
    }

    /**
     * Tests that sorting a list of dates with a week-of-weekyear comparator
     * correctly orders them based on their week number within the year.
     */
    @Test
    public void sortUsingWeekOfWeekyearComparator_shouldOrderDatesByWeekNumber() {
        // Arrange: Define the comparator and the data to be tested.
        // This comparator considers only the 'week of weekyear' field for comparison.
        Comparator<Object> weekOfWeekyearComparator = DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear());

        List<DateTime> datesToSort = new ArrayList<>(Arrays.asList(
            createUtcDateTime("2002-04-01T10:00:00"), // Week 14
            createUtcDateTime("2002-01-01T10:00:00"), // Week 1
            createUtcDateTime("2002-12-01T10:00:00"), // Week 48
            createUtcDateTime("2002-09-01T10:00:00"), // Week 35
            createUtcDateTime("2002-02-01T10:00:00"), // Week 5
            createUtcDateTime("2002-10-01T10:00:00")  // Week 40
        ));

        // The expected order after sorting by week number.
        List<DateTime> expectedOrder = Arrays.asList(
            createUtcDateTime("2002-01-01T10:00:00"), // Week 1
            createUtcDateTime("2002-02-01T10:00:00"), // Week 5
            createUtcDateTime("2002-04-01T10:00:00"), // Week 14
            createUtcDateTime("2002-09-01T10:00:00"), // Week 35
            createUtcDateTime("2002-10-01T10:00:00"), // Week 40
            createUtcDateTime("2002-12-01T10:00:00")  // Week 48
        );

        // Pre-condition: Ensure the list is not already in the expected order.
        // This confirms that the sorting operation has a meaningful effect.
        assertNotEquals("Test setup is incorrect: list is already sorted.", expectedOrder, datesToSort);

        // Act: Perform the sorting operation.
        datesToSort.sort(weekOfWeekyearComparator);

        // Assert: Verify that the list is now in the expected order.
        assertEquals("The list should be sorted by week of weekyear.", expectedOrder, datesToSort);
    }
}