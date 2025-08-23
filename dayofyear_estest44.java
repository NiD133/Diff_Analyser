package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DayOfYear_ESTestTest44 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that creating a LocalDate for the 366th day of a non-leap year
     * throws a DateTimeException.
     */
    @Test
    public void atYear_withDay366AndNonLeapYear_throwsDateTimeException() {
        // Arrange: Define the 366th day of the year and a year that is not a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        int nonLeapYear = 366; // The year 366 is not a leap year.
        String expectedErrorMessage = "Invalid date 'DayOfYear 366' as '366' is not a leap year";

        // Act & Assert: Attempting to create a date from these values should fail.
        try {
            day366.atYear(nonLeapYear);
            fail("Expected a DateTimeException because the 366th day does not exist in a non-leap year.");
        } catch (DateTimeException e) {
            // Verify that the exception has the expected, specific error message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}