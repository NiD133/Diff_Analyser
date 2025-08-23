package org.joda.time;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the {@link DateTimeComparator} class.
 * <p>
 * This revised test focuses on clarity and modern testing practices.
 * It uses JUnit 5 and a clear Arrange-Act-Assert pattern within each test method.
 * All test data and comparators are locally scoped to ensure tests are self-contained and easy to understand.
 */
public class DateTimeComparatorTest {

    // All DateTimes are created in the UTC zone to ensure consistency.
    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Tests that the year-only comparator correctly sorts a list of DateTimes.
     * The comparator should only consider the year field, ignoring month, day, and time.
     */
    @Test
    public void yearComparator_shouldSortDateTimesByYear() {
        // Arrange
        DateTime dt1066 = new DateTime(1066, 2, 1, 0, 0, 0, UTC);
        DateTime dt1776 = new DateTime(1776, 2, 1, 0, 0, 0, UTC);
        DateTime dt1863 = new DateTime(1863, 2, 1, 0, 0, 0, UTC);
        DateTime dt1998 = new DateTime(1998, 2, 1, 0, 0, 0, UTC);
        DateTime dt1999 = new DateTime(1999, 2, 1, 0, 0, 0, UTC);
        DateTime dt2100 = new DateTime(2100, 2, 1, 0, 0, 0, UTC);
        DateTime dt2525 = new DateTime(2525, 2, 1, 0, 0, 0, UTC);

        // A list of dates in an unsorted order.
        List<DateTime> unsortedDateTimes = new ArrayList<>(Arrays.asList(
            dt1999, dt1998, dt2525, dt1776, dt1863, dt1066, dt2100
        ));

        // The same list of dates, but in the expected sorted order.
        List<DateTime> expectedSortedDateTimes = Arrays.asList(
            dt1066, dt1776, dt1863, dt1998, dt1999, dt2100, dt2525
        );

        Comparator<Object> yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());

        // Act
        Collections.sort(unsortedDateTimes, yearComparator);

        // Assert
        assertEquals(expectedSortedDateTimes, unsortedDateTimes, "The list should be sorted by year.");
    }
}