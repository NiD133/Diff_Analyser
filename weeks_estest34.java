package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link Weeks} class, focusing on arithmetic operations.
 */
public class WeeksTest {

    /**
     * Verifies that calling dividedBy() with a zero divisor throws an ArithmeticException.
     */
    @Test
    public void dividedBy_whenDivisorIsZero_throwsArithmeticException() {
        // Arrange: Create an instance of Weeks to test.
        Weeks twoWeeks = Weeks.TWO;

        // Act & Assert: Use assertThrows to verify that the expected exception is thrown.
        // This is the modern, preferred way to test exceptions in JUnit 4.13+.
        ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> twoWeeks.dividedBy(0)
        );

        // Assert (optional but recommended): Verify the exception message for more specific testing.
        assertEquals("/ by zero", exception.getMessage());
    }
}