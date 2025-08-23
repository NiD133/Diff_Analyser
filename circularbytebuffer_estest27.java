package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    @Test
    public void readFromEmptyBufferShouldThrowIllegalStateException() {
        // Arrange: Create a new, empty buffer.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertFalse("Precondition: The buffer should be empty.", buffer.hasBytes());

        // Act & Assert: Attempting to read from the empty buffer should fail.
        try {
            buffer.read();
            fail("Expected an IllegalStateException to be thrown when reading from an empty buffer.");
        } catch (final IllegalStateException e) {
            // Assert: Verify the exception message is correct.
            assertEquals("The exception message does not match the expected value.", "No bytes available.", e.getMessage());
        }
    }
}