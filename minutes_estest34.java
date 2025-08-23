package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that adding a Minutes object to MAX_VALUE causes an ArithmeticException
     * due to integer overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void plus_whenResultOverflows_throwsArithmeticException() {
        // Arrange: Create a Minutes instance with the maximum possible value.
        Minutes maxMinutes = Minutes.MAX_VALUE;

        // Act: Add the maximum value to itself. This operation is expected to overflow.
        // Assert: The @Test(expected) annotation asserts that an ArithmeticException is thrown.
        maxMinutes.plus(maxMinutes);
    }
}