package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.Year;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link DayOfYear} class.
 * This class focuses on improving the understandability of a single, specific test case.
 */
public class DayOfYearTest {

    /**
     * Tests that attempting to create a LocalDate for the 366th day of a non-leap year
     * throws a DateTimeException.
     */
    @Test
    public void atYear_throwsException_whenCombiningDay366WithNonLeapYear() {
        // Arrange: Define the 366th day of the year and a year that is not a leap year.
        // Day 366 is only valid in a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        Year nonLeapYear = Year.of(2021); // 2021 is a well-known non-leap year.

        // Act & Assert: Verify that combining these values throws the correct exception.
        try {
            day366.atYear(nonLeapYear);
            fail("Expected DateTimeException was not thrown for an invalid date combination.");
        } catch (DateTimeException e) {
            // Verify the exception message to ensure it's thrown for the right reason.
            String expectedMessage = "Invalid date 'DayOfYear 366' as '2021' is not a leap year";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}