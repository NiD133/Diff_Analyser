package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    @Test
    public void readWithNegativeOffsetShouldThrowIllegalArgumentException() {
        // Arrange: Set up the test conditions
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] destinationArray = new byte[10];
        final int negativeOffset = -1;
        final int length = 1;

        // Act & Assert: Execute the method and verify the outcome
        try {
            buffer.read(destinationArray, negativeOffset, length);
            fail("Expected an IllegalArgumentException to be thrown for a negative offset.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is correct and informative
            final String expectedMessage = "Illegal offset: " + negativeOffset;
            assertEquals("The exception message should detail the illegal offset.",
                         expectedMessage, e.getMessage());
        }
    }
}