package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RandomUtils}.
 */
class RandomUtilsTest {

    @Test
    @DisplayName("randomDouble() should throw IllegalArgumentException for negative range values")
    void randomDoubleShouldThrowExceptionForNegativeRange() {
        // Arrange
        final RandomUtils randomUtils = RandomUtils.insecure();
        final double negativeStart = -5.0;
        final double negativeEnd = -5.0;
        final String expectedMessage = "Both range values must be non-negative.";

        // Act & Assert
        // Verify that the expected exception is thrown
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            randomUtils.randomDouble(negativeStart, negativeEnd);
        });

        // Verify that the exception has the correct message for more precise testing
        assertEquals(expectedMessage, exception.getMessage());
    }
}