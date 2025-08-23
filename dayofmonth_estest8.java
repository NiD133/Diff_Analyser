package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the comparison logic in {@link DayOfMonth#compareTo(DayOfMonth)}.
 */
public class DayOfMonthComparisonTest {

    private static final DayOfMonth DAY_15 = DayOfMonth.of(15);
    private static final DayOfMonth DAY_20 = DayOfMonth.of(20);

    @Test
    public void compareTo_shouldReturnNegative_whenDayIsSmallerThanOther() {
        // Arrange: A DayOfMonth instance (15) that is numerically smaller than another (20).

        // Act: Compare the smaller day to the larger day.
        int comparisonResult = DAY_15.compareTo(DAY_20);

        // Assert: The result must be a negative integer, as per the Comparable contract.
        assertTrue(
            "Expected a negative result when comparing day 15 to day 20, but was " + comparisonResult,
            comparisonResult < 0
        );
    }

    @Test
    public void compareTo_shouldReturnPositive_whenDayIsLargerThanOther() {
        // Arrange: A DayOfMonth instance (20) that is numerically larger than another (15).

        // Act: Compare the larger day to the smaller day.
        int comparisonResult = DAY_20.compareTo(DAY_15);

        // Assert: The result must be a positive integer.
        assertTrue(
            "Expected a positive result when comparing day 20 to day 15, but was " + comparisonResult,
            comparisonResult > 0
        );
    }

    @Test
    public void compareTo_shouldReturnZero_whenDaysAreEqual() {
        // Arrange: Two DayOfMonth instances representing the same day.
        DayOfMonth anotherDay15 = DayOfMonth.of(15);

        // Act: Compare the day to an equivalent day.
        int comparisonResult = DAY_15.compareTo(anotherDay15);

        // Assert: The result must be zero.
        assertEquals(
            "Expected a result of 0 when comparing two equal DayOfMonth objects",
            0,
            comparisonResult
        );
    }
}