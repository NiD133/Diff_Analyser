package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the Weeks class.
 */
public class WeeksTest {

    /**
     * Verifies that attempting to convert the maximum possible number of weeks
     * to minutes throws an ArithmeticException due to an integer overflow.
     */
    @Test
    public void toStandardMinutes_whenResultOverflows_throwsArithmeticException() {
        // Arrange: Define the input that will cause the overflow.
        // The number of minutes in one week is 7 (days) * 24 (hours) * 60 (minutes) = 10080.
        // Multiplying Weeks.MAX_VALUE (which is Integer.MAX_VALUE) by 10080
        // will exceed the capacity of a 32-bit integer.
        Weeks maxWeeks = Weeks.MAX_VALUE;

        // Act & Assert: Execute the method and verify the expected exception.
        try {
            maxWeeks.toStandardMinutes();
            fail("Expected an ArithmeticException to be thrown due to integer overflow, but it was not.");
        } catch (ArithmeticException e) {
            // Verify that the exception message clearly indicates an overflow,
            // which confirms the cause of the failure.
            String expectedMessage = "Multiplication overflows an int: 2147483647 * 10080";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}