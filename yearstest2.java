package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for the {@link Years} class, focusing on the {@code years(int)} factory method.
 */
class YearsTest {

    @Test
    @DisplayName("years(int) should return cached singletons for predefined constants")
    void years_whenCreatingFromConstantValue_returnsCachedInstance() {
        // The factory method is expected to return the exact same singleton instance
        // for common values like 0, 1, 2, 3, and the min/max values.
        assertSame(Years.ZERO, Years.years(0), "Should return singleton for 0 years");
        assertSame(Years.ONE, Years.years(1), "Should return singleton for 1 year");
        assertSame(Years.TWO, Years.years(2), "Should return singleton for 2 years");
        assertSame(Years.THREE, Years.years(3), "Should return singleton for 3 years");
        assertSame(Years.MAX_VALUE, Years.years(Integer.MAX_VALUE), "Should return singleton for MAX_VALUE");
        assertSame(Years.MIN_VALUE, Years.years(Integer.MIN_VALUE), "Should return singleton for MIN_VALUE");
    }

    @Test
    @DisplayName("years(int) should create new instances with the correct value for other integers")
    void years_whenCreatingFromArbitraryValue_returnsInstanceWithCorrectValue() {
        // For values that don't have a corresponding predefined constant, the factory
        // should create a new Years instance holding the correct value.
        assertEquals(-1, Years.years(-1).getYears(), "A negative value should be correctly represented");
        assertEquals(4, Years.years(4).getYears(), "A value just above the constants should be correctly represented");
    }
}