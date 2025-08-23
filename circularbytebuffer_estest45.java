package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the {@code add(byte[], int, int)} method throws an
     * IllegalArgumentException when the provided offset is negative.
     */
    @Test
    public void addWithNegativeOffsetShouldThrowIllegalArgumentException() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] sourceData = new byte[16];
        final int negativeOffset = -4;
        // The length is also invalid, but the implementation checks the offset first.
        final int anyLength = -4;

        // Act & Assert
        try {
            buffer.add(sourceData, negativeOffset, anyLength);
            fail("Expected an IllegalArgumentException to be thrown due to the negative offset.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is correct.
            final String expectedMessage = "Illegal offset: " + negativeOffset;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}