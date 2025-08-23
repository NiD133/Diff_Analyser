package org.joda.time;

import junit.framework.TestCase;

/**
 * Contains unit tests for the {@link Minutes#minus(int)} method.
 */
public class MinutesTest extends TestCase {

    /**
     * Tests that subtracting a value returns a new instance with the correct
     * result, and that the original instance remains unchanged (is immutable).
     */
    public void testMinus_subtractsValueAndIsImmutable() {
        // Arrange
        final Minutes initialMinutes = Minutes.minutes(2);
        final int valueToSubtract = 3;

        // Act
        final Minutes result = initialMinutes.minus(valueToSubtract);

        // Assert
        assertEquals("The original Minutes object should be immutable.",
                2, initialMinutes.getMinutes());
        assertEquals("The result of 2 - 3 should be -1.",
                -1, result.getMinutes());
        assertNotSame("A new object should be returned.", initialMinutes, result);
    }

    /**
     * Tests that subtracting zero from a Minutes instance results in an
     * equal instance. For cached constants, it should return the same instance.
     */
    public void testMinus_whenSubtractingZero_returnsEqualInstance() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;

        // Act
        final Minutes result = oneMinute.minus(0);

        // Assert
        assertEquals("Subtracting zero should not change the value.", 1, result.getMinutes());
        assertSame("For cached constants, subtracting zero should return the same instance.",
                oneMinute, result);
    }

    /**
     * Tests that attempting to subtract a value that would cause an integer
     * underflow throws an ArithmeticException.
     */
    public void testMinus_whenResultUnderflows_throwsArithmeticException() {
        // Arrange
        final Minutes minMinutes = Minutes.MIN_VALUE;

        // Act & Assert
        try {
            minMinutes.minus(1);
            fail("Expected an ArithmeticException to be thrown for integer underflow.");
        } catch (ArithmeticException expected) {
            // This is the expected behavior, so the test passes.
        }
    }
}