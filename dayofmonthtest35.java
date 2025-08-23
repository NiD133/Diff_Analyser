package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth#adjustInto(Temporal)}.
 */
class DayOfMonthTest {

    @Test
    void adjustInto_whenDayIsInvalidForMonthInNonLeapYear_throwsException() {
        // Arrange
        // Day 29 is invalid for February in a non-leap year.
        DayOfMonth dayOfMonth29 = DayOfMonth.of(29);
        // The year 2007 is not a leap year.
        LocalDate dateInNonLeapYear = LocalDate.of(2007, Month.FEBRUARY, 1);

        // Act & Assert
        // Attempting to adjust the date to an invalid day (Feb 29th) should fail.
        assertThrows(DateTimeException.class, () -> dayOfMonth29.adjustInto(dateInNonLeapYear));
    }
}