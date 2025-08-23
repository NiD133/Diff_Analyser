package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that putUnquotedUTF8() returns -1 when the target ByteBuffer has no
     * remaining capacity. This return value signals that no bytes could be written.
     */
    @Test
    public void putUnquotedUTF8_shouldReturnNegativeOne_whenBufferHasNoRemainingSpace() {
        // Arrange
        SerializedString content = new SerializedString("any-non-empty-string");
        ByteBuffer fullBuffer = ByteBuffer.allocate(0);

        // Act
        int bytesWritten = content.putUnquotedUTF8(fullBuffer);

        // Assert
        assertEquals("Should return -1 to indicate the buffer is full", -1, bytesWritten);
    }
}