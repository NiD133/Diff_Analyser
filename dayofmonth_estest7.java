package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DayOfMonth#compareTo(DayOfMonth)} method.
 */
public class DayOfMonthCompareToTest {

    /**
     * Tests that compareTo returns a positive integer when the first day-of-month
     * is greater than the second.
     */
    @Test
    public void compareTo_whenDayIsGreaterThanOther_shouldReturnPositiveValue() {
        // Arrange: Create two DayOfMonth instances, one with a greater value.
        // The original test was non-deterministic because it used DayOfMonth.now()
        // and assumed the current day was the 14th. We make this explicit.
        DayOfMonth day14 = DayOfMonth.of(14);
        DayOfMonth day1 = DayOfMonth.of(1);

        // Act: Compare the greater day to the lesser day.
        int comparisonResult = day14.compareTo(day1);

        // Assert: The result should be the positive difference between the two values.
        // The contract of compareTo is to return a positive integer, and for this class,
        // it is specifically the difference in days (14 - 1 = 13).
        assertEquals(13, comparisonResult);
    }
}