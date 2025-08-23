package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link Seconds} class, focusing on edge-case behavior.
 */
public class SecondsTest {

    /**
     * Verifies that calling negated() on Seconds.MIN_VALUE throws an ArithmeticException.
     * This is the expected behavior because negating the underlying Integer.MIN_VALUE
     * results in an integer overflow.
     */
    @Test
    public void negated_onMinValue_throwsArithmeticException() {
        // Arrange: Get the Seconds instance representing the minimum integer value.
        Seconds minValueSeconds = Seconds.MIN_VALUE;

        // Act & Assert: Attempt to negate the value and verify that the correct exception is thrown.
        // The assertThrows method is a modern and clear way to test for exceptions in JUnit 4.13+.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            minValueSeconds::negated
        );

        // Assert: Further verify that the exception message is as expected, ensuring the
        // failure occurs for the correct reason.
        assertEquals("Integer.MIN_VALUE cannot be negated", exception.getMessage());
    }
}