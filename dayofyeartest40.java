package org.threeten.extra;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link DayOfYear#atYear(Year)}.
 */
public class DayOfYearTest {

    private static final Year LEAP_YEAR = Year.of(2008);
    private static final int LEAP_YEAR_LENGTH = 366;

    /**
     * Tests that atYear() correctly combines a DayOfYear with a leap year
     * for every one of the 366 possible days.
     */
    @Test
    void atYear_withLeapYear_returnsCorrectDateForAll366Days() {
        // Loop through all possible days in a leap year to ensure each is handled correctly.
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            // Arrange: Define the input and the expected outcome for this iteration.
            // Using LocalDate.ofYearDay makes the expected result explicit and independent of other iterations.
            DayOfYear dayOfYear = DayOfYear.of(day);
            LocalDate expectedDate = LocalDate.ofYearDay(LEAP_YEAR.getValue(), day);

            // Act: Call the method under test.
            LocalDate actualDate = dayOfYear.atYear(LEAP_YEAR);

            // Assert: Verify the result and provide a detailed message on failure.
            assertEquals(expectedDate, actualDate, "Failed for day " + day + " in a leap year.");
        }
    }
}