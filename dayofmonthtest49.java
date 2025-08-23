package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth} class, focusing on comparison behavior.
 */
class DayOfMonthComparisonTest {

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------

    @Test
    void compareTo_whenArgumentIsNull_throwsNullPointerException() {
        // Arrange
        DayOfMonth day15 = DayOfMonth.of(15);

        // Act & Assert
        // The contract of Comparable.compareTo specifies that it throws a
        // NullPointerException if the specified object is null.
        assertThrows(NullPointerException.class, () -> day15.compareTo(null));
    }
}