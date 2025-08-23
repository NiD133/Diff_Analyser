package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * The Years class stores its value as an int. Negating Integer.MIN_VALUE
     * causes an arithmetic overflow. This test verifies that the negated() method
     * correctly throws an ArithmeticException in this edge case.
     */
    @Test
    public void negated_shouldThrowArithmeticException_whenNegatingMinValue() {
        // Arrange: The subject of our test is the constant representing the minimum value.
        // No explicit arrangement is needed besides using Years.MIN_VALUE.

        // Act & Assert: Call the method and verify that the expected exception is thrown.
        // The assertThrows method, available in JUnit 4.13+, is a clean way to handle this.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> Years.MIN_VALUE.negated()
        );

        // Assert: Further inspect the exception to ensure it has the correct, informative message.
        assertEquals("Integer.MIN_VALUE cannot be negated", exception.getMessage());
    }
}