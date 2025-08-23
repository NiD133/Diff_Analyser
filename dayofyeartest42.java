package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Year;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the atYear(int) method of {@link DayOfYear}.
 */
@DisplayName("DayOfYear.atYear(int)")
class DayOfYearAtYearTest {

    private static final int STANDARD_YEAR = 2007;

    @Test
    @DisplayName("for a standard year returns the correct LocalDate for every day")
    void atYear_forStandardYear_returnsCorrectDateForAllDays() {
        // A standard year has 365 days. We verify this assumption for clarity.
        int yearLength = Year.of(STANDARD_YEAR).length();
        assertEquals(365, yearLength, "Test setup assumes 2007 is a standard year.");

        // Iterate through all days of the standard year
        for (int day = 1; day <= yearLength; day++) {
            // Arrange: Create the DayOfYear and the expected LocalDate.
            // Using LocalDate.ofYearDay makes the expected result explicit and independent
            // of previous loop iterations.
            DayOfYear dayOfYear = DayOfYear.of(day);
            LocalDate expectedDate = LocalDate.ofYearDay(STANDARD_YEAR, day);

            // Act: Call the method under test.
            LocalDate actualDate = dayOfYear.atYear(STANDARD_YEAR);

            // Assert: Check if the actual result matches the expected result.
            // The assertion message provides context if the test fails.
            assertEquals(expectedDate, actualDate,
                    () -> "Failed for day " + day + " of year " + STANDARD_YEAR);
        }
    }
}