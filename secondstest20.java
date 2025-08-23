package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test cases for the plus(int) method of the Seconds class.
 */
public class SecondsTest {

    //-----------------------------------------------------------------------
    // plus(int)
    //-----------------------------------------------------------------------

    @Test
    public void plus_shouldAddPositiveValue() {
        // Arrange
        final Seconds initial = Seconds.seconds(2);
        final Seconds expected = Seconds.seconds(5);

        // Act
        Seconds result = initial.plus(3);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void plus_shouldBeImmutable() {
        // Arrange
        final Seconds initial = Seconds.seconds(2);
        final Seconds expected = Seconds.seconds(2); // The initial object should not change

        // Act
        initial.plus(3); // The return value is ignored, we are testing the original object

        // Assert
        assertEquals(expected, initial);
    }

    @Test
    public void plus_shouldReturnSameInstanceWhenAddingZero() {
        // Arrange
        final Seconds initial = Seconds.ONE;

        // Act
        Seconds result = initial.plus(0);

        // Assert
        // The implementation is optimized to return the same instance for a zero add.
        assertSame(initial, result);
    }

    @Test(expected = ArithmeticException.class)
    public void plus_shouldThrowExceptionOnOverflow() {
        // Arrange
        Seconds max = Seconds.MAX_VALUE;

        // Act
        // This operation should cause an integer overflow and throw an exception.
        max.plus(1);
    }
}