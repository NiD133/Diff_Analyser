package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link Weeks} class, focusing on edge cases and exceptional behavior.
 */
public class WeeksTest {

    /**
     * Verifies that subtracting a negative value from Weeks.MAX_VALUE
     * throws an ArithmeticException due to integer overflow.
     * The operation is equivalent to an addition that exceeds Integer.MAX_VALUE.
     */
    @Test(expected = ArithmeticException.class)
    public void testMinusFromMaxValueWithNegativeNumberThrowsArithmeticException() {
        // Arrange: Start with the maximum possible value for Weeks.
        Weeks maxWeeks = Weeks.MAX_VALUE;

        // Act: Attempt to subtract a negative number. This is equivalent to
        // Integer.MAX_VALUE - (-1), or Integer.MAX_VALUE + 1, which overflows.
        maxWeeks.minus(-1);

        // Assert: The test expects an ArithmeticException, which is declared
        // in the @Test annotation. If no exception is thrown, the test will fail.
    }
}