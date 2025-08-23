package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on buffer operations.
 */
public class SerializedStringTest {

    /**
     * Tests that {@link SerializedString#putQuotedUTF8(ByteBuffer)} returns -1
     * when the provided ByteBuffer does not have enough remaining capacity to hold
     * the entire quoted, UTF-8 encoded string.
     */
    @Test
    public void putQuotedUTF8ShouldReturnNegativeOneWhenBufferIsTooSmall() {
        // Arrange
        // The string "8g9F:23V=x" is 10 characters long. Its JSON-quoted UTF-8
        // representation is ""8g9F:23V=x"", which requires 12 bytes.
        SerializedString serializedString = new SerializedString("8g9F:23V=x");

        // We create a buffer that is intentionally too small to hold the 12-byte result.
        final int insufficientCapacity = 7;
        ByteBuffer smallBuffer = ByteBuffer.allocate(insufficientCapacity);

        // Act
        // Attempt to write the quoted string into the buffer.
        int bytesWritten = serializedString.putQuotedUTF8(smallBuffer);

        // Assert
        // The method's contract specifies returning -1 to indicate failure due to lack of space.
        final int expectedFailureCode = -1;
        assertEquals("Should return -1 for insufficient buffer space", expectedFailureCode, bytesWritten);
    }
}