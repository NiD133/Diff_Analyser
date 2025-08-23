package org.threeten.extra;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link DayOfYear#atYear(Year)}.
 */
class DayOfYearTest {

    @Test
    void atYear_throwsExceptionForDay366InNonLeapYear() {
        // Arrange: Define the 366th day of the year and a non-leap year.
        // The 366th day is only valid in a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        Year nonLeapYear = Year.of(2007);

        // Act & Assert: Verify that combining the 366th day with a non-leap year
        // throws a DateTimeException, as this date is invalid.
        assertThrows(DateTimeException.class, () -> day366.atYear(nonLeapYear));
    }
}