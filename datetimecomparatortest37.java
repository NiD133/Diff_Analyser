package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link DateTimeComparator} focusing on list sorting.
 */
public class DateTimeComparatorTestTest37 {

    /**
     * Tests that the month-of-year comparator correctly sorts a list of DateTime objects.
     */
    @Test
    public void sortsListByMonthOfYear() {
        // Arrange: Create a set of DateTime objects for different months in the same year.
        DateTime january = new DateTime("2002-01-15T10:00:00Z");
        DateTime february = new DateTime("2002-02-15T10:00:00Z");
        DateTime april = new DateTime("2002-04-15T10:00:00Z");
        DateTime september = new DateTime("2002-09-15T10:00:00Z");
        DateTime september_duplicate = new DateTime("2002-09-15T10:00:00Z"); // A duplicate to test stability
        DateTime october = new DateTime("2002-10-15T10:00:00Z");
        DateTime december = new DateTime("2002-12-15T10:00:00Z");

        // Create an unsorted list of these dates.
        List<DateTime> unsortedList = new ArrayList<>(Arrays.asList(
            april, january, december, september, september_duplicate, february, october
        ));

        // Define the expected order after sorting by month.
        List<DateTime> expectedSortedList = Arrays.asList(
            january, february, april, september, september_duplicate, october, december
        );

        // Get a comparator that compares by month, then by year.
        Comparator<Object> monthComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());

        // Act: Sort the list using the month comparator.
        Collections.sort(unsortedList, monthComparator);

        // Assert: The sorted list must match the expected order.
        assertEquals("The list should be sorted chronologically by month", expectedSortedList, unsortedList);
    }
}