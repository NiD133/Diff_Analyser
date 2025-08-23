package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link DateTimeComparator}.
 * This class focuses on testing list sorting capabilities using different comparators.
 */
public class DateTimeComparatorTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(DateTimeComparatorTest.class);
    }

    /**
     * Tests that a list of DateTimes is correctly sorted when using a comparator
     * that only considers the 'secondOfMinute' field.
     */
    public void testSortBySecond() {
        // Arrange
        // Comparator that compares only the 'secondOfMinute' field.
        Comparator<Object> secondComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour());

        List<DateTime> unsortedDateTimes = createUtcDateTimeList(
            "1999-02-01T00:00:10",
            "1999-02-01T00:00:30",
            "1999-02-01T00:00:25",
            "1999-02-01T00:00:18",
            "1999-02-01T00:00:01",
            "1999-02-01T00:00:59",
            "1999-02-01T00:00:22"
        );

        List<DateTime> expectedOrder = createUtcDateTimeList(
            "1999-02-01T00:00:01",
            "1999-02-01T00:00:10",
            "1999-02-01T00:00:18",
            "1999-02-01T00:00:22",
            "1999-02-01T00:00:25",
            "1999-02-01T00:00:30",
            "1999-02-01T00:00:59"
        );

        // Ensure the list is not already sorted, which would make the test trivial.
        assertFalse("Precondition failed: The list is already sorted.", unsortedDateTimes.equals(expectedOrder));

        // Act
        Collections.sort(unsortedDateTimes, secondComparator);

        // Assert
        assertEquals("The list was not sorted correctly by second.", expectedOrder, unsortedDateTimes);
    }

    /**
     * Helper method to create a list of DateTime objects in the UTC zone from string representations.
     *
     * @param dateTimeStrings The ISO 8601 date-time strings to parse.
     * @return A list of DateTime objects.
     */
    private List<DateTime> createUtcDateTimeList(String... dateTimeStrings) {
        List<DateTime> dateTimeList = new ArrayList<>();
        for (String str : dateTimeStrings) {
            dateTimeList.add(new DateTime(str, DateTimeZone.UTC));
        }
        return dateTimeList;
    }
}