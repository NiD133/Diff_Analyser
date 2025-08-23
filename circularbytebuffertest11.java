package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CircularByteBuffer} focusing on invalid arguments.
 */
class CircularByteBufferTest {

    @Test
    void peekShouldThrowExceptionWhenLengthIsNegative() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] anyTargetBuffer = new byte[10];
        final int negativeLength = -1;
        final String expectedMessage = "Illegal length: " + negativeLength;

        // Act
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            buffer.peek(anyTargetBuffer, 0, negativeLength);
        });

        // Assert
        assertEquals(expectedMessage, thrown.getMessage());
    }
}