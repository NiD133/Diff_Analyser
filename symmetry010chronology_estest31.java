package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry010Chronology}.
 * This class focuses on validating exception-throwing behavior for invalid date creation.
 */
// Note: The original class name and inheritance are preserved as requested.
public class Symmetry010Chronology_ESTestTest31 extends Symmetry010Chronology_ESTest_scaffolding {

    /**
     * Tests that calling date() with a month value outside the valid range (1-12)
     * throws a DateTimeException.
     */
    @Test(timeout = 4000)
    public void date_shouldThrowDateTimeException_whenMonthIsInvalid() {
        // Arrange: Set up the test conditions and inputs.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int invalidMonth = 13;
        // Use arbitrary but valid values for year and day to isolate the invalid month.
        int year = 2023;
        int dayOfMonth = 1;
        String expectedErrorMessage = "Invalid value for MonthOfYear (valid values 1 - 12): " + invalidMonth;

        // Act & Assert: Execute the method and verify the outcome.
        try {
            chronology.date(year, invalidMonth, dayOfMonth);
            fail("Expected a DateTimeException to be thrown for an invalid month, but it was not.");
        } catch (DateTimeException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}