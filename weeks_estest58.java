package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that adding a value that causes an integer overflow
     * correctly throws an ArithmeticException.
     */
    @Test
    public void plus_whenResultingInIntegerOverflow_throwsArithmeticException() {
        // Arrange: Create a Weeks instance with the minimum possible integer value.
        Weeks minWeeks = Weeks.weeks(Integer.MIN_VALUE);
        int valueToAdd = Integer.MIN_VALUE;

        // Act & Assert: Attempt the addition and verify the expected exception.
        try {
            minWeeks.plus(valueToAdd);
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // Verify that the exception message clearly indicates an overflow.
            String expectedMessage = "The calculation caused an overflow: -2147483648 + -2147483648";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}