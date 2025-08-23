package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test suite for the {@link Minutes#multipliedBy(int)} method.
 */
public class MinutesTest {

    @Test
    public void multipliedBy_withPositiveScalar_returnsCorrectProduct() {
        // Arrange
        final Minutes twoMinutes = Minutes.minutes(2);
        final int scalar = 3;
        final Minutes expected = Minutes.minutes(6);

        // Act
        final Minutes result = twoMinutes.multipliedBy(scalar);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectProduct() {
        // Arrange
        final Minutes twoMinutes = Minutes.minutes(2);
        final int scalar = -3;
        final Minutes expected = Minutes.minutes(-6);

        // Act
        final Minutes result = twoMinutes.multipliedBy(scalar);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void multipliedBy_isImmutable() {
        // Arrange
        final Minutes twoMinutes = Minutes.minutes(2);
        final Minutes originalState = Minutes.minutes(2);

        // Act
        twoMinutes.multipliedBy(3); // This should return a new instance

        // Assert
        assertEquals("Original Minutes object should not be mutated",
                     originalState, twoMinutes);
    }

    @Test
    public void multipliedBy_one_returnsSameInstance() {
        // Arrange
        final Minutes twoMinutes = Minutes.minutes(2);

        // Act
        final Minutes result = twoMinutes.multipliedBy(1);

        // Assert
        assertSame("Multiplying by 1 should return the same instance for optimization",
                   twoMinutes, result);
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_throwsArithmeticException() {
        // Arrange
        final Minutes largeMinutes = Minutes.minutes(Integer.MAX_VALUE / 2 + 1);
        final int scalar = 2;

        // Act
        // This call is expected to throw an ArithmeticException
        largeMinutes.multipliedBy(scalar);
    }
}