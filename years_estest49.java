package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A test suite for the {@link Years} class, focusing on its arithmetic operations.
 */
public class YearsTest {

    /**
     * Verifies that multiplying a Years instance by a value that causes the
     * underlying integer to overflow throws an ArithmeticException.
     */
    @Test
    public void multipliedBy_whenResultOverflows_throwsArithmeticException() {
        // Arrange: Start with the maximum possible value for Years.
        final Years maxYears = Years.MAX_VALUE;
        final int multiplier = 3;

        // Act & Assert: Attempt the multiplication and expect an exception.
        try {
            maxYears.multipliedBy(multiplier);
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // Verify that the exception message clearly indicates an overflow.
            final String expectedMessage = "Multiplication overflows an int: 2147483647 * 3";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}