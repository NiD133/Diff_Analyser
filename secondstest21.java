package org.joda.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the plus(Seconds) method in the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void plus_whenAddingTwoInstances_returnsCorrectSumAndIsImmutable() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);
        final Seconds threeSeconds = Seconds.seconds(3);
        final Seconds expectedSum = Seconds.seconds(5);

        // Act
        final Seconds actualSum = twoSeconds.plus(threeSeconds);

        // Assert
        assertEquals(expectedSum, actualSum, "The sum of 2 and 3 seconds should be 5.");

        // Verify that the original instances are immutable
        assertEquals(2, twoSeconds.getSeconds(), "Original instance should not be modified.");
        assertEquals(3, threeSeconds.getSeconds(), "Instance added should not be modified.");
    }

    @Test
    public void plus_whenAddingZero_returnsEqualInstance() {
        // Arrange
        final Seconds oneSecond = Seconds.ONE;

        // Act
        final Seconds result = oneSecond.plus(Seconds.ZERO);

        // Assert
        assertEquals(oneSecond, result, "Adding zero should result in an equal Seconds object.");
    }

    @Test
    public void plus_whenAddingNull_returnsEqualInstance() {
        // Arrange
        final Seconds oneSecond = Seconds.ONE;

        // Act: Per Joda-Time documentation, adding null is equivalent to adding zero.
        final Seconds result = oneSecond.plus((Seconds) null);

        // Assert
        assertEquals(oneSecond, result, "Adding null should result in an equal Seconds object.");
    }

    @Test
    public void plus_whenSumOverflows_throwsArithmeticException() {
        // Arrange
        final Seconds maxSeconds = Seconds.MAX_VALUE;
        final Seconds oneSecond = Seconds.ONE;

        // Act & Assert
        assertThrows(ArithmeticException.class, () -> {
            maxSeconds.plus(oneSecond);
        }, "Adding to MAX_VALUE should cause an ArithmeticException.");
    }
}