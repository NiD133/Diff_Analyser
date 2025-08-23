package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tests the sorting behavior of DateTimeComparator.
 * This test focuses on sorting a list of DateTime objects.
 */
public class DateTimeComparatorSortTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Tests that the hour-based comparator correctly sorts a list of DateTimes
     * according to the hour of the day, ignoring other fields.
     */
    @Test
    public void sortUsingHourComparator_arrangesListByHourOfDay() {
        // Arrange: Define the comparator and the data for the test.
        // The comparator will consider fields from hourOfDay up to (but not including) dayOfYear.
        Comparator<Object> hourComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());

        List<DateTime> dateTimes = createUtcDateTimeList(
            "1999-02-01T10:00:00",
            "1999-02-01T23:00:00",
            "1999-02-01T01:00:00",
            "1999-02-01T15:00:00",
            "1999-02-01T05:00:00",
            "1999-02-01T20:00:00",
            "1999-02-01T17:00:00"
        );

        // Explicitly define the expected order for clarity and a robust assertion.
        List<DateTime> expectedOrder = createUtcDateTimeList(
            "1999-02-01T01:00:00", // 1st
            "1999-02-01T05:00:00", // 2nd
            "1999-02-01T10:00:00", // 3rd
            "1999-02-01T15:00:00", // 4th
            "1999-02-01T17:00:00", // 5th
            "1999-02-01T20:00:00", // 6th
            "1999-02-01T23:00:00"  // 7th
        );

        // Act: Perform the action under test.
        Collections.sort(dateTimes, hourComparator);

        // Assert: Verify the outcome.
        assertEquals("The list should be sorted by the hour of the day", expectedOrder, dateTimes);
    }

    /**
     * Helper method to create a list of DateTime objects from ISO-formatted strings in UTC.
     *
     * @param dateStrings The date-time strings to parse.
     * @return A list of DateTime objects.
     */
    private List<DateTime> createUtcDateTimeList(String... dateStrings) {
        List<DateTime> dateTimes = new ArrayList<>();
        for (String str : dateStrings) {
            dateTimes.add(new DateTime(str, UTC));
        }
        return dateTimes;
    }
}