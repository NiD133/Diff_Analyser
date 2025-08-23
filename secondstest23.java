package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the minus(Seconds) method in the Seconds class.
 */
public class SecondsTest {

    @Test
    public void minus_shouldReturnCorrectDifference() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);
        final Seconds threeSeconds = Seconds.seconds(3);
        final int expectedDifference = -1;

        // Act
        Seconds result = twoSeconds.minus(threeSeconds);

        // Assert
        assertEquals(expectedDifference, result.getSeconds());
    }

    @Test
    public void minus_shouldBeImmutable() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);
        final Seconds threeSeconds = Seconds.seconds(3);

        // Act
        twoSeconds.minus(threeSeconds);

        // Assert
        // Verify that the original instances were not modified.
        assertEquals(2, twoSeconds.getSeconds());
        assertEquals(3, threeSeconds.getSeconds());
    }

    @Test
    public void minus_shouldTreatNullAsZero() {
        // Arrange
        final Seconds oneSecond = Seconds.ONE;

        // Act
        // Per the method's contract, subtracting null is the same as subtracting zero.
        Seconds result = oneSecond.minus((Seconds) null);

        // Assert
        assertEquals(1, result.getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_shouldThrowExceptionOnOverflow() {
        // Arrange
        final Seconds min = Seconds.MIN_VALUE;
        final Seconds one = Seconds.ONE;

        // Act
        // This operation (Integer.MIN_VALUE - 1) should cause an overflow.
        min.minus(one);
    }
}