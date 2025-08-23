package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Month;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth#atMonth(Month)} method.
 *
 * <p>This class focuses on specific behaviors of DayOfMonth, ensuring its methods
 * are robust and handle edge cases correctly.
 */
class DayOfMonthTest {

    @Test
    @DisplayName("atMonth(Month) should throw NullPointerException when the month is null")
    void atMonth_withNullMonth_throwsNullPointerException() {
        // Given a valid DayOfMonth instance
        DayOfMonth dayOfMonth = DayOfMonth.of(12);

        // When calling atMonth with a null argument
        // Then a NullPointerException is expected.
        // The cast to (Month) is necessary to resolve method ambiguity with atMonth(int).
        assertThrows(
                NullPointerException.class,
                () -> dayOfMonth.atMonth((Month) null),
                "Passing a null Month should throw a NullPointerException."
        );
    }
}