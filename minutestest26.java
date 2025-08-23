package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the negated() method in the {@link Minutes} class.
 */
public class MinutesNegatedTest {

    @Test
    public void negated_shouldReturnNegativeValue_forPositiveInput() {
        // Arrange
        final Minutes twelveMinutes = Minutes.minutes(12);
        final int expectedNegatedValue = -12;

        // Act
        final Minutes negatedResult = twelveMinutes.negated();

        // Assert
        assertEquals(expectedNegatedValue, negatedResult.getMinutes());
    }

    @Test
    public void negated_shouldNotChangeOriginalObject() {
        // Arrange
        final Minutes twelveMinutes = Minutes.minutes(12);
        final int originalValue = 12;

        // Act
        // The result of the operation is intentionally ignored.
        twelveMinutes.negated();

        // Assert
        // Verify that the original object remains unmodified, confirming its immutability.
        assertEquals("The original Minutes object should not be modified.",
                     originalValue, twelveMinutes.getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_whenValueIsMinValue_shouldThrowArithmeticException() {
        // Act: Attempting to negate the minimum integer value should cause an overflow.
        // Assert: An ArithmeticException is expected.
        Minutes.MIN_VALUE.negated();
    }
}