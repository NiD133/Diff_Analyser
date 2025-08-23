package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that dividing a Seconds instance by zero throws an ArithmeticException.
     */
    @Test
    public void dividedBy_zeroDivisor_throwsArithmeticException() {
        // Arrange
        Seconds seconds = Seconds.ONE;

        // Act & Assert
        // We expect an ArithmeticException to be thrown when dividing by zero.
        ArithmeticException thrown = assertThrows(
            ArithmeticException.class,
            () -> seconds.dividedBy(0)
        );

        // Verify that the exception message is as expected.
        assertEquals("/ by zero", thrown.getMessage());
    }
}