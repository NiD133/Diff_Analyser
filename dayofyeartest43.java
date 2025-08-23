package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfYear}.
 */
class DayOfYearTest {

    @Test
    void atYear_forDay366_throwsExceptionOnNonLeapYear() {
        // Arrange: The 366th day of the year, which only exists in a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        int nonLeapYear = 2007; // 2007 is not a leap year.

        // Act & Assert: Attempting to create a date for the 366th day in a non-leap year
        // should fail with a DateTimeException.
        assertThrows(DateTimeException.class, () -> day366.atYear(nonLeapYear));
    }
}