package org.joda.time;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Seconds#minus(int)} method.
 */
class SecondsTest {

    @Test
    void minus_shouldSubtractValueAndReturnNewInstance() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);
        final int valueToSubtract = 3;
        final int expectedSeconds = -1;

        // Act
        final Seconds result = twoSeconds.minus(valueToSubtract);

        // Assert
        assertEquals(expectedSeconds, result.getSeconds(), "2 seconds - 3 should be -1 seconds");
    }

    @Test
    void minus_shouldNotChangeOriginalInstance() {
        // Arrange
        final Seconds twoSeconds = Seconds.seconds(2);
        final int originalValue = twoSeconds.getSeconds();

        // Act
        // The result of the operation is intentionally ignored to test immutability.
        twoSeconds.minus(3);

        // Assert
        assertEquals(originalValue, twoSeconds.getSeconds(), "Original Seconds instance must be immutable");
    }

    @Test
    void minus_whenSubtractingZero_shouldReturnEqualInstance() {
        // Arrange
        final Seconds oneSecond = Seconds.ONE;

        // Act
        final Seconds result = oneSecond.minus(0);

        // Assert
        assertEquals(oneSecond.getSeconds(), result.getSeconds(), "Subtracting zero should not change the value");
    }

    @Test
    void minus_whenResultUnderflows_shouldThrowArithmeticException() {
        // Arrange
        final Seconds minSeconds = Seconds.MIN_VALUE;

        // Act & Assert
        assertThrows(ArithmeticException.class, () -> {
            minSeconds.minus(1);
        }, "Subtracting 1 from MIN_VALUE should cause an integer underflow");
    }
}