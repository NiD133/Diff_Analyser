package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth#of(int)} factory method.
 */
class DayOfMonthTestTest6 {

    /**
     * Tests that creating a DayOfMonth with a value of 0, which is below the
     * valid range of 1-31, throws an exception.
     */
    @Test
    void of_throwsExceptionForDayZero() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(0),
                "DayOfMonth.of(0) should throw DateTimeException as it's out of the valid 1-31 range.");
    }
}