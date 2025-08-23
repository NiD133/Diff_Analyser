package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link Minutes} class.
 * This specific test focuses on the behavior of the multipliedBy method
 * under overflow conditions.
 */
public class Minutes_ESTestTest62 extends Minutes_ESTest_scaffolding {

    /**
     * Tests that multiplying Minutes.MAX_VALUE by a negative number
     * throws an ArithmeticException due to integer overflow.
     */
    @Test
    public void multipliedBy_whenResultOverflows_shouldThrowArithmeticException() {
        // Arrange: Start with the largest possible Minutes value.
        Minutes maxMinutes = Minutes.MAX_VALUE;
        int multiplier = -4433;

        // Act & Assert: Attempt the multiplication and verify the exception.
        try {
            maxMinutes.multipliedBy(multiplier);
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // Verify that the exception message clearly indicates an overflow,
            // which is the expected behavior.
            String expectedMessage = "Multiplication overflows an int: 2147483647 * -4433";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}