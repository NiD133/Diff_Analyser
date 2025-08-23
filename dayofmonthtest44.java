package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth}.
 */
class DayOfMonthTest {

    @Test
    void atMonth_withInvalidMonthValue_throwsException() {
        // Arrange: A DayOfMonth instance and an invalid month value (months are 1-12).
        DayOfMonth dayOfMonth = DayOfMonth.of(12);
        int invalidMonth = 13;

        // Act & Assert: Verify that calling atMonth() with an out-of-range month
        // throws a DateTimeException.
        assertThrows(DateTimeException.class, () -> {
            dayOfMonth.atMonth(invalidMonth);
        });
    }
}