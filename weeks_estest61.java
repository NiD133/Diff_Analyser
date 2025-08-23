package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that converting Weeks.MAX_VALUE to days throws an ArithmeticException.
     * This is expected because the calculation (Integer.MAX_VALUE * 7) results
     * in a value that overflows the capacity of an integer.
     */
    @Test
    public void toStandardDays_withMaxValue_throwsArithmeticException() {
        // Arrange: Use the largest possible value for Weeks.
        Weeks maxWeeks = Weeks.MAX_VALUE;

        // Act & Assert
        try {
            maxWeeks.toStandardDays();
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // Verify that the exception is the one we expect and that its message
            // clearly indicates the cause of the overflow.
            assertEquals("Multiplication overflows an int: 2147483647 * 7", e.getMessage());
        }
    }
}