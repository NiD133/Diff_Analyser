package org.joda.time;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link DateTimeComparator}.
 * This suite focuses on the sorting capabilities of the comparator.
 */
public class DateTimeComparatorTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Tests that the comparator correctly sorts a list of DateTime objects
     * based on the millisecond field.
     */
    @Test
    public void shouldSortByMillisecond() {
        // Arrange
        long baseTime = 12345000L; // A base time to ensure other fields are identical
        DateTime dt_000ms = new DateTime(baseTime + 0, UTC);
        DateTime dt_123ms = new DateTime(baseTime + 123, UTC);
        DateTime dt_222ms = new DateTime(baseTime + 222, UTC);
        DateTime dt_456ms = new DateTime(baseTime + 456, UTC);
        DateTime dt_888ms = new DateTime(baseTime + 888, UTC);
        DateTime dt_999ms = new DateTime(baseTime + 999, UTC);

        // Create a list in an unsorted order
        List<DateTime> dateTimes = new ArrayList<>();
        dateTimes.add(dt_999ms);
        dateTimes.add(dt_222ms);
        dateTimes.add(dt_456ms);
        dateTimes.add(dt_888ms);
        dateTimes.add(dt_123ms);
        dateTimes.add(dt_000ms);

        // Define the expected order after sorting
        List<DateTime> expectedOrder = new ArrayList<>();
        expectedOrder.add(dt_000ms);
        expectedOrder.add(dt_123ms);
        expectedOrder.add(dt_222ms);
        expectedOrder.add(dt_456ms);
        expectedOrder.add(dt_888ms);
        expectedOrder.add(dt_999ms);

        // This comparator is configured to only consider fields smaller than a second,
        // which effectively means it compares by millisecond-of-second.
        Comparator<Object> millisComparator = DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute());

        // Sanity check to ensure the list is not already sorted
        assertNotEquals("Test setup issue: list is already sorted.", expectedOrder, dateTimes);

        // Act
        Collections.sort(dateTimes, millisComparator);

        // Assert
        assertEquals("The list should be sorted by millisecond.", expectedOrder, dateTimes);
    }
}