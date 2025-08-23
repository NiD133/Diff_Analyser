package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

// Note: The original test class was named DayOfMonthTestTest5.
// It has been renamed to DayOfMonthTest for clarity and conciseness.
class DayOfMonthTest {

    // The DayOfMonth class caches instances for each valid day of the month.
    private static final int MAX_CACHED_DAY = 31;

    //-----------------------------------------------------------------------
    // of(int)
    //-----------------------------------------------------------------------

    @Test
    void of_returnsCachedInstanceForValidDays() {
        // This test verifies two key behaviors of the DayOfMonth.of(int) factory method:
        // 1. It creates an object with the correct day value.
        // 2. It returns a pre-allocated, cached instance for each valid day.
        for (int day = 1; day <= MAX_CACHED_DAY; day++) {
            DayOfMonth dayOfMonth = DayOfMonth.of(day);

            // 1. Verify the value is correct
            assertEquals(day, dayOfMonth.getValue(), "The value should match the input day.");

            // 2. Verify that the factory method returns the same instance on subsequent calls
            DayOfMonth sameDayOfMonth = DayOfMonth.of(day);
            assertSame(dayOfMonth, sameDayOfMonth, "Instances for the same day should be cached and identical.");
        }
    }
}