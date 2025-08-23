package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class, focusing on arithmetic operations.
 */
public class MinutesTest {

    /**
     * Tests that subtracting a negative value from MAX_VALUE causes an overflow.
     * The operation {@code MAX_VALUE.minus(-1)} is equivalent to adding 1 to
     * {@code Integer.MAX_VALUE}, which is expected to throw an {@link ArithmeticException}.
     */
    @Test(expected = ArithmeticException.class)
    public void minus_fromMaxValueWithNegativeValue_throwsArithmeticExceptionOnOverflow() {
        // Attempting to subtract a negative number from the maximum value
        // should result in an integer overflow.
        Minutes.MAX_VALUE.minus(-1);
    }
}