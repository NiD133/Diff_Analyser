package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the compareTo method in {@link DayOfYear}.
 */
class DayOfYearCompareToTest {

    private static final DayOfYear DAY_60 = DayOfYear.of(60);
    private static final DayOfYear DAY_180 = DayOfYear.of(180);
    private static final DayOfYear DAY_180_DUPLICATE = DayOfYear.of(180);

    @Test
    void compareTo_whenDayIsGreater_returnsPositive() {
        // A day with a higher value should be considered "greater than" a day with a lower value.
        assertTrue(DAY_180.compareTo(DAY_60) > 0, "Comparing a day to a smaller day should return a positive value.");
    }

    @Test
    void compareTo_whenDayIsLesser_returnsNegative() {
        // A day with a lower value should be considered "less than" a day with a higher value.
        assertTrue(DAY_60.compareTo(DAY_180) < 0, "Comparing a day to a larger day should return a negative value.");
    }

    @Test
    void compareTo_whenDaysAreEqual_returnsZero() {
        // Two DayOfYear instances representing the same day should be considered equal.
        assertEquals(0, DAY_180.compareTo(DAY_180_DUPLICATE), "Comparing a day to an equal day should return zero.");
    }
}