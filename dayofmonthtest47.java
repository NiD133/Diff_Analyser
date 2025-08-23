package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.YearMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth#atYearMonth(YearMonth)} method.
 */
class DayOfMonthTest {

    private static final DayOfMonth SAMPLE_DAY_OF_MONTH = DayOfMonth.of(12);

    @Test
    @DisplayName("atYearMonth() should throw NullPointerException when the YearMonth is null")
    void atYearMonth_withNullYearMonth_throwsNullPointerException() {
        // Arrange: The input YearMonth is null.
        // The DayOfMonth instance is arbitrary but valid.

        // Act & Assert: Calling atYearMonth with a null argument should throw an exception.
        // The explicit cast to (YearMonth) is necessary to resolve method ambiguity for the compiler.
        assertThrows(NullPointerException.class, () -> {
            SAMPLE_DAY_OF_MONTH.atYearMonth((YearMonth) null);
        });
    }
}