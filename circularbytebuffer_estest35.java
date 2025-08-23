package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the peek() method throws an IllegalArgumentException
     * when called with a negative offset.
     */
    @Test
    public void peekShouldThrowIllegalArgumentExceptionForNegativeOffset() {
        // Arrange: Create a buffer and define the invalid arguments for the peek method.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] sourceBuffer = new byte[10]; // The buffer content doesn't matter for this test.
        final int negativeOffset = -1;
        final int anyValidLength = 1; // Use a valid length to isolate the negative offset as the cause.

        // Act & Assert: Call peek() and verify that the correct exception is thrown.
        try {
            buffer.peek(sourceBuffer, negativeOffset, anyValidLength);
            fail("Expected an IllegalArgumentException to be thrown due to the negative offset.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is specific and helpful.
            final String expectedMessage = "Illegal offset: " + negativeOffset;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}