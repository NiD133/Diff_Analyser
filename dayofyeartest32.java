package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#adjustInto(java.time.temporal.Temporal)} method.
 */
@DisplayName("DayOfYear.adjustInto")
class DayOfYearAdjustIntoTest {

    private static final int NON_LEAP_YEAR = 2007;
    private static final int DAY_366 = 366;

    @Test
    @DisplayName("throws a DateTimeException when adjusting a date in a non-leap year to day 366")
    void adjustIntoNonLeapYearWithDay366ShouldThrowException() {
        // Arrange
        DayOfYear dayOfYear366 = DayOfYear.of(DAY_366);
        LocalDate dateInNonLeapYear = LocalDate.of(NON_LEAP_YEAR, 1, 1);

        // Act & Assert
        // The adjustInto method should fail because day 366 is invalid for the non-leap year 2007.
        assertThrows(DateTimeException.class, () -> {
            dayOfYear366.adjustInto(dateInNonLeapYear);
        });
    }
}