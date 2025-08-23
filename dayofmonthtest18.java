package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.TemporalField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth#range(TemporalField)} method.
 */
class DayOfMonthRangeTest {

    private static final DayOfMonth TEST_DAY_OF_MONTH = DayOfMonth.of(12);

    @Test
    @DisplayName("range(TemporalField) should throw NullPointerException for a null field")
    void range_whenFieldIsNull_throwsNullPointerException() {
        // The method contract for range(TemporalField) specifies the field must not be null.
        // This test verifies that a NullPointerException is thrown as expected.
        assertThrows(NullPointerException.class, () -> {
            // The cast to (TemporalField) is needed to resolve method overload ambiguity for null.
            TEST_DAY_OF_MONTH.range((TemporalField) null);
        });
    }
}