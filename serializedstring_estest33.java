package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that putUnquotedUTF8 returns -1 when the target ByteBuffer has no
     * remaining capacity, correctly indicating that no bytes could be written.
     */
    @Test
    public void putUnquotedUTF8ShouldReturnNegativeOneWhenBufferHasNoRemainingSpace() {
        // Arrange
        SerializedString serializedString = new SerializedString("some-text");
        // A buffer with zero capacity has no space for any data.
        ByteBuffer emptyBuffer = ByteBuffer.allocate(0);

        // Act
        int bytesWritten = serializedString.putUnquotedUTF8(emptyBuffer);

        // Assert
        // The method should return -1 to signal that the buffer is full.
        assertEquals("Expected -1 when buffer has no space", -1, bytesWritten);
    }
}