package org.joda.time;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the time-only comparator provided by {@link DateTimeComparator#getTimeOnlyInstance()}.
 */
public class DateTimeComparatorTimeOnlyTest {

    /**
     * Verifies that the time-only comparator correctly sorts a list of DateTime objects
     * based on their time of day, while ignoring their date components.
     */
    @Test
    public void shouldSortDateTimeListByTimeIgnoringDate() {
        // Arrange
        // Create a list of DateTime objects with different dates and unsorted times.
        // Using different dates makes it clear that the date part is being ignored.
        List<DateTime> dateTimes = createDateTimesFromStrings(
            "2000-01-01T01:02:05Z",
            "2010-04-02T22:22:22Z",
            "1999-12-31T05:30:45Z",
            "2005-07-15T09:17:59Z",
            "2008-02-29T09:17:58Z",
            "2020-11-20T15:30:00Z",
            "1995-09-01T17:00:44Z"
        );

        // Define the expected order after sorting by time of day.
        List<DateTime> expectedOrder = createDateTimesFromStrings(
            "2000-01-01T01:02:05Z", // 01:02:05
            "1999-12-31T05:30:45Z", // 05:30:45
            "2008-02-29T09:17:58Z", // 09:17:58
            "2005-07-15T09:17:59Z", // 09:17:59
            "2020-11-20T15:30:00Z", // 15:30:00
            "1995-09-01T17:00:44Z", // 17:00:44
            "2010-04-02T22:22:22Z"  // 22:22:22
        );

        // Ensure the list isn't already in the expected order by coincidence.
        assertNotEquals("Test setup issue: initial list is already sorted.", expectedOrder, dateTimes);

        Comparator<Object> timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();

        // Act
        // Sort the list using the time-only comparator.
        Collections.sort(dateTimes, timeOnlyComparator);

        // Assert
        // Verify that the sorted list matches the expected order.
        assertEquals(expectedOrder, dateTimes);
    }

    /**
     * Helper method to create a list of DateTime objects from ISO 8601 formatted strings.
     *
     * @param isoStrings The date-time strings to parse.
     * @return A list of DateTime objects.
     */
    private static List<DateTime> createDateTimesFromStrings(String... isoStrings) {
        List<DateTime> dateTimes = new ArrayList<>();
        for (String s : isoStrings) {
            // All test DateTimes are created in UTC to avoid time zone-related issues.
            dateTimes.add(new DateTime(s, DateTimeZone.UTC));
        }
        return dateTimes;
    }
}