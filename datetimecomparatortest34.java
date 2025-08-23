package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests for {@link DateTimeComparator}, focusing on sorting capabilities.
 */
public class DateTimeComparatorTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Creates a list of DateTime objects from ISO 8601 formatted strings, all in UTC.
     *
     * @param dateStrings The string representations of the dates.
     * @return A list of DateTime objects.
     */
    private List<DateTime> createUtcDateTimeList(String... dateStrings) {
        return Stream.of(dateStrings)
                     .map(s -> new DateTime(s, UTC))
                     .collect(Collectors.toList());
    }

    @Test
    public void testSortByDayOfYear() {
        // --- Arrange ---
        // Create a list of dates in an unsorted order. All dates are in the same year.
        List<DateTime> unsortedDateTimes = createUtcDateTimeList(
            "2002-04-20T10:00:00", // Day 110
            "2002-01-16T10:00:00", // Day 16
            "2002-12-31T10:00:00", // Day 365
            "2002-09-14T10:00:00", // Day 257
            "2002-09-19T10:00:00", // Day 262
            "2002-02-14T10:00:00", // Day 45
            "2002-10-30T10:00:00"  // Day 303
        );

        // Define the expected order after sorting by day of the year.
        List<DateTime> expectedOrder = createUtcDateTimeList(
            "2002-01-16T10:00:00",
            "2002-02-14T10:00:00",
            "2002-04-20T10:00:00",
            "2002-09-14T10:00:00",
            "2002-09-19T10:00:00",
            "2002-10-30T10:00:00",
            "2002-12-31T10:00:00"
        );

        // Create a mutable list for sorting.
        List<DateTime> listToSort = new ArrayList<>(unsortedDateTimes);

        // Sanity check: ensure the list is not already in the expected order.
        assertNotEquals("Precondition failed: list is already sorted.", expectedOrder, listToSort);

        // Get a comparator that compares fields from day-of-year up to (but not including) year.
        // For this test, it effectively only compares the day-of-year.
        Comparator<Object> dayOfYearComparator = DateTimeComparator.getInstance(
                DateTimeFieldType.dayOfYear(), DateTimeFieldType.year());

        // --- Act ---
        Collections.sort(listToSort, dayOfYearComparator);

        // --- Assert ---
        assertEquals("The list should be sorted by day of the year.", expectedOrder, listToSort);
    }
}