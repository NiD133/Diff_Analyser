package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the multipliedBy(int) method in the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void multipliedBy_withPositiveScalar_returnsCorrectProduct() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);
        final int scalar = 3;
        final Seconds expected = Seconds.seconds(6);

        // Act
        Seconds result = twoSeconds.multipliedBy(scalar);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectProduct() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);
        final int scalar = -3;
        final Seconds expected = Seconds.seconds(-6);

        // Act
        Seconds result = twoSeconds.multipliedBy(scalar);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void multipliedBy_withZero_returnsZeroSeconds() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);

        // Act
        Seconds result = twoSeconds.multipliedBy(0);

        // Assert
        assertEquals(Seconds.ZERO, result);
    }

    @Test
    public void multipliedBy_withOne_returnsSameInstance() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);

        // Act
        Seconds result = twoSeconds.multipliedBy(1);

        // Assert
        assertSame("Multiplying by 1 should return the same instance for immutability", twoSeconds, result);
    }

    @Test
    public void multipliedBy_isImmutable() {
        // Arrange
        final Seconds originalSeconds = Seconds.seconds(2);

        // Act
        originalSeconds.multipliedBy(3);

        // Assert
        assertEquals("The original Seconds object should not be modified", 2, originalSeconds.getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_throwsArithmeticException() {
        // Arrange
        Seconds largeSeconds = Seconds.seconds(Integer.MAX_VALUE / 2 + 1);

        // Act: This operation should throw an exception
        largeSeconds.multipliedBy(2);

        // Assert: The test passes if an ArithmeticException is thrown
    }
}