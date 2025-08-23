package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link Weeks} class, focusing on arithmetic operations.
 */
public class WeeksTest {

    /**
     * Verifies that adding a value to Weeks.MAX_VALUE that causes an integer overflow
     * correctly throws an ArithmeticException.
     */
    @Test
    public void plus_whenSumExceedsIntegerMax_throwsArithmeticException() {
        // Arrange: Start with the maximum possible value for Weeks.
        final Weeks maxWeeks = Weeks.MAX_VALUE;
        final String expectedMessage = "The calculation caused an overflow: 2147483647 + 2147483647";

        // Act & Assert: Execute the operation and verify that it throws the expected exception.
        // The assertThrows method captures the thrown exception for further inspection.
        ArithmeticException thrown = assertThrows(
            "Adding MAX_VALUE to itself should cause an overflow.",
            ArithmeticException.class,
            () -> maxWeeks.plus(maxWeeks)
        );

        // Assert: Verify that the exception message is correct, confirming the cause of the error.
        assertEquals(expectedMessage, thrown.getMessage());
    }
}