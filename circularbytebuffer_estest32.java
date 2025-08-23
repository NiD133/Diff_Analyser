package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    @Test
    public void peekShouldThrowIllegalArgumentExceptionForNegativeLength() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] destinationBuffer = new byte[8];
        final int negativeLength = -1;
        final String expectedMessage = "Illegal length: " + negativeLength;

        // Act & Assert
        try {
            buffer.peek(destinationBuffer, 0, negativeLength);
            fail("Expected an IllegalArgumentException to be thrown due to negative length.");
        } catch (final IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}