package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link Minutes#plus(Minutes)} method.
 */
public class MinutesTest {

    @Test
    public void plus_addsMinutesCorrectlyAndIsImmutable() {
        // Arrange
        final Minutes twoMinutes = Minutes.minutes(2);
        final Minutes threeMinutes = Minutes.minutes(3);

        // Act
        final Minutes result = twoMinutes.plus(threeMinutes);

        // Assert
        // 1. The result of the addition should be correct.
        assertEquals("2 + 3 should equal 5", 5, result.getMinutes());

        // 2. The original instances should not be changed (verifying immutability).
        assertEquals("Original instance should remain unchanged", 2, twoMinutes.getMinutes());
        assertEquals("Parameter instance should remain unchanged", 3, threeMinutes.getMinutes());
    }

    @Test
    public void plus_addingZeroMinutes_returnsAnEqualInstance() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;

        // Act
        final Minutes result = oneMinute.plus(Minutes.ZERO);

        // Assert
        assertEquals("Adding zero should not change the value", oneMinute, result);
    }

    @Test
    public void plus_addingNull_isTreatedAsAddingZero() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;

        // Act
        // The method contract states that a null input is treated as zero.
        final Minutes result = oneMinute.plus((Minutes) null);

        // Assert
        assertEquals("Adding null should not change the value", oneMinute, result);
    }

    @Test(expected = ArithmeticException.class)
    public void plus_whenResultOverflows_throwsArithmeticException() {
        // Arrange
        final Minutes max = Minutes.MAX_VALUE;
        final Minutes one = Minutes.ONE;

        // Act: This operation should cause an integer overflow.
        max.plus(one);

        // Assert: The test will pass if an ArithmeticException is thrown.
    }
}