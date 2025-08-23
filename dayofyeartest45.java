package org.threeten.extra;

import org.junit.jupiter.api.Test;
import java.time.DateTimeException;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Contains tests for the {@link DayOfYear} class.
 */
public class DayOfYearTest {

    /**
     * Verifies that calling atYear() with the 366th day on a non-leap year
     * correctly throws a DateTimeException.
     */
    @Test
    void whenCombiningDay366WithNonLeapYear_thenThrowDateTimeException() {
        // Arrange: Set up the 366th day of the year and a year that is not a leap year.
        // The year 366 is not divisible by 4 and is therefore not a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        Year nonLeapYear = Year.of(366);

        // Act & Assert: Execute the method call and verify that it throws the expected exception.
        DateTimeException exception = assertThrows(DateTimeException.class, () -> {
            day366.atYear(nonLeapYear);
        });

        // Assert: Further verify that the exception message is correct, confirming the cause.
        String expectedMessage = "Invalid date 'DayOfYear 366' as '366' is not a leap year";
        assertEquals(expectedMessage, exception.getMessage());
    }
}