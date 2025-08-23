package org.joda.time;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Seconds#negated()} method.
 */
class SecondsTest {

    @Test
    void negated_returnsCorrectValueAndIsImmutable() {
        // Arrange
        final Seconds originalSeconds = Seconds.seconds(12);
        final int expectedOriginalValue = 12;
        final int expectedNegatedValue = -12;

        // Act
        final Seconds negatedSeconds = originalSeconds.negated();

        // Assert
        assertEquals(expectedNegatedValue, negatedSeconds.getSeconds(),
                "The new instance should have the negated value.");
        assertEquals(expectedOriginalValue, originalSeconds.getSeconds(),
                "The original instance should remain unchanged, proving immutability.");
    }

    @Test
    void negated_onMinValue_throwsArithmeticException() {
        // Arrange
        final Seconds minValueSeconds = Seconds.MIN_VALUE;

        // Act & Assert
        // Negating Integer.MIN_VALUE causes an overflow, which should throw an exception.
        assertThrows(ArithmeticException.class, minValueSeconds::negated,
                "Negating MIN_VALUE should cause an overflow and throw an ArithmeticException.");
    }
}