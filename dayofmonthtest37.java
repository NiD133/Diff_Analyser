package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.Temporal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth#adjustInto(Temporal)} method.
 */
class DayOfMonthAdjustIntoTest {

    private static final DayOfMonth DAY_OF_MONTH_12 = DayOfMonth.of(12);

    @Test
    @DisplayName("adjustInto() should throw NullPointerException when the temporal is null")
    void adjustInto_withNullTemporal_throwsNullPointerException() {
        // This test verifies the method's contract: it must not accept a null argument.
        assertThrows(NullPointerException.class, () -> DAY_OF_MONTH_12.adjustInto(null));
    }
}