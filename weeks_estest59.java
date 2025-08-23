package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Weeks_ESTestTest59 { // Note: Class name kept as per original for context

    /**
     * Tests that converting Weeks.MIN_VALUE to standard hours throws an
     * ArithmeticException due to integer overflow.
     * The calculation (-2,147,483,648 weeks * 168 hours/week) is too large
     * to fit in a standard integer.
     */
    @Test
    public void toStandardHours_whenConvertingMinValue_throwsArithmeticException() {
        // Arrange: Start with the minimum possible value for Weeks.
        final Weeks minWeeks = Weeks.MIN_VALUE;
        final String expectedErrorMessage = "Multiplication overflows an int: -2147483648 * 168";

        // Act & Assert: Attempt the conversion and verify the expected exception is thrown.
        try {
            minWeeks.toStandardHours();
            fail("Expected an ArithmeticException to be thrown due to integer overflow, but it was not.");
        } catch (ArithmeticException e) {
            // Verify that the exception was thrown for the correct reason.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}