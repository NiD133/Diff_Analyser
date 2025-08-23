package org.joda.time;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests the sorting behavior of DateTimeComparator.
 */
public class DateTimeComparatorListSortTest {

    /**
     * Tests that a list of DateTime objects is correctly sorted
     * using a comparator that only considers the day-of-month field.
     */
    @Test
    public void sortsListByDayOfMonth() {
        // Arrange: Create a comparator that compares only the day-of-month field.
        // The upper limit of monthOfYear means comparison stops if days are equal.
        Comparator<Object> dayOfMonthComparator = DateTimeComparator.getInstance(
                DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());

        // Create an unsorted list of dates, all within the same month and year.
        List<DateTime> unsortedDates = createUtcDateTimeList(
                "2002-04-20T10:00:00",
                "2002-04-16T10:00:00",
                "2002-04-15T10:00:00",
                "2002-04-17T10:00:00",
                "2002-04-19T10:00:00",
                "2002-04-18T10:00:00",
                "2002-04-14T10:00:00"
        );

        // Define the expected order after sorting by day of the month.
        List<DateTime> expectedSortedDates = createUtcDateTimeList(
                "2002-04-14T10:00:00",
                "2002-04-15T10:00:00",
                "2002-04-16T10:00:00",
                "2002-04-17T10:00:00",
                "2002-04-18T10:00:00",
                "2002-04-19T10:00:00",
                "2002-04-20T10:00:00"
        );

        // A quick sanity check to ensure the list is not already sorted.
        assertNotEquals("Test setup issue: The list is already sorted.", expectedSortedDates, unsortedDates);

        // Act: Sort the list using the custom comparator.
        // A mutable copy is needed because the list from the helper is immutable.
        List<DateTime> actualSortedDates = new ArrayList<>(unsortedDates);
        Collections.sort(actualSortedDates, dayOfMonthComparator);

        // Assert: Verify that the list is now sorted as expected.
        assertEquals(expectedSortedDates, actualSortedDates);
    }

    /**
     * Helper method to create a list of DateTime objects from ISO date strings in UTC.
     *
     * @param dateStrings A varargs array of date strings in ISO format.
     * @return A list of DateTime objects.
     */
    private List<DateTime> createUtcDateTimeList(String... dateStrings) {
        return Arrays.stream(dateStrings)
                     .map(s -> new DateTime(s, DateTimeZone.UTC))
                     .collect(Collectors.toList());
    }
}