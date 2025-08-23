package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link DayOfYear#isValidYear(int)}.
 */
public class DayOfYearIsValidYearTest {

    @Test
    public void isValidYear_shouldReturnFalse_forDay366InNonLeapYear() {
        // Arrange
        // The 366th day of the year only exists in a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        int nonLeapYear = 2021; // 2021 is a well-known non-leap year.

        // Act
        boolean isValid = day366.isValidYear(nonLeapYear);

        // Assert
        assertFalse("Day 366 should be invalid in a non-leap year", isValid);
    }
}