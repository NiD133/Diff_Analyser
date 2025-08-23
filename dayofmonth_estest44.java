package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link DayOfMonth} class, focusing on exception handling.
 */
public class DayOfMonthTest {

    /**
     * Tests that calling atMonth() with an invalid (out of range) month value
     * throws a DateTimeException.
     */
    @Test
    public void atMonth_whenMonthIsInvalid_shouldThrowDateTimeException() {
        // Arrange: Set up the test data.
        // We create a DayOfMonth instance and define an integer value that is
        // outside the valid month range of 1-12.
        DayOfMonth dayOfMonth = DayOfMonth.of(31);
        int invalidMonthValue = -550;

        // Act & Assert: Execute the method and verify the outcome.
        try {
            dayOfMonth.atMonth(invalidMonthValue);
            // If this line is reached, the test fails because no exception was thrown.
            fail("Expected a DateTimeException to be thrown for an invalid month value.");
        } catch (DateTimeException e) {
            // Verify that the exception has the expected message. This makes the test
            // more specific and robust than only checking the exception type.
            String expectedMessage = "Invalid value for MonthOfYear: " + invalidMonthValue;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}