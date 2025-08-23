package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DayOfMonth} class, focusing on comparison logic.
 */
public class DayOfMonthComparisonTest {

    /**
     * Tests that the compareTo method correctly returns a negative value
     * when comparing a smaller day to a larger day.
     */
    @Test
    public void compareTo_shouldReturnNegative_whenComparingSmallerToLargerDay() {
        // Arrange: Define a smaller and a larger day of the month.
        // The original test was non-deterministic, relying on DayOfMonth.now().
        // This version uses fixed values for a reliable and clear test.
        DayOfMonth smallerDay = DayOfMonth.of(14);
        DayOfMonth largerDay = DayOfMonth.of(28);

        // Act: Compare the smaller day to the larger day.
        int comparisonResult = smallerDay.compareTo(largerDay);

        // Assert: The result should be the difference between the two day values,
        // which is negative.
        assertEquals(14 - 28, comparisonResult);
    }
}