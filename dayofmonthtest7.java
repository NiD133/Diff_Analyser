package org.threeten.extra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link DayOfMonth#of(int)} factory method.
 */
class DayOfMonthOfTest {

    @Test
    @DisplayName("of() throws DateTimeException for a day greater than 31")
    void of_whenDayIsTooHigh_throwsException() {
        // The valid range for a day-of-month is 1 to 31.
        // This test verifies that the factory method rejects values outside this range.
        int invalidDay = 32;

        // Act & Assert
        assertThrows(
                DateTimeException.class,
                () -> DayOfMonth.of(invalidDay),
                "DayOfMonth.of() should not accept a day greater than 31."
        );
    }
}