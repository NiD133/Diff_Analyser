package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Helper to create a list of DateTime objects from ISO date strings.
     * All DateTimes are created in the UTC time zone for consistency.
     */
    private List<DateTime> createDateTimeList(String... isoDateStrings) {
        return Arrays.stream(isoDateStrings)
                     .map(s -> new DateTime(s, UTC))
                     .collect(Collectors.toList());
    }

    @Test
    public void sort_whenUsingMinuteComparator_sortsListByMinuteOfHour() {
        // Arrange
        // This comparator only considers the minute-of-hour field for sorting.
        // The upper limit (hourOfDay) means fields of that magnitude or greater are ignored.
        Comparator<Object> minuteComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.minuteOfHour(), DateTimeFieldType.hourOfDay());

        List<DateTime> unsortedDateTimes = createDateTimeList(
            "1999-02-01T00:10:00Z",
            "1999-02-01T00:30:00Z",
            "1999-02-01T00:25:00Z",
            "1999-02-01T00:18:00Z",
            "1999-02-01T00:01:00Z",
            "1999-02-01T00:59:00Z",
            "1999-02-01T00:22:00Z"
        );

        List<DateTime> expectedSortedOrder = createDateTimeList(
            "1999-02-01T00:01:00Z", // 01 minute
            "1999-02-01T00:10:00Z", // 10 minutes
            "1999-02-01T00:18:00Z", // 18 minutes
            "1999-02-01T00:22:00Z", // 22 minutes
            "1999-02-01T00:25:00Z", // 25 minutes
            "1999-02-01T00:30:00Z", // 30 minutes
            "1999-02-01T00:59:00Z"  // 59 minutes
        );

        // Sanity check to ensure the test data is not already in the expected order.
        assertNotEquals("Precondition failed: test data is already sorted.", expectedSortedOrder, unsortedDateTimes);

        // Act
        // Create a mutable copy to sort, preserving the original list.
        List<DateTime> actualSortedList = new ArrayList<>(unsortedDateTimes);
        Collections.sort(actualSortedList, minuteComparator);

        // Assert
        assertEquals("List should be sorted according to the minute of the hour", expectedSortedOrder, actualSortedList);
    }
}