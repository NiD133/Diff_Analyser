package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its behavior
 * with an empty string value.
 */
public class SerializedStringTest {

    /**
     * Tests that {@link SerializedString#appendUnquotedUTF8} returns -1 when
     * the provided buffer is too small to accommodate the content, even when
     * the content itself is empty.
     */
    @Test
    public void appendUnquotedUTF8_withEmptyStringAndInsufficientBuffer_shouldReturnNegativeOne() {
        // Arrange
        SerializedString emptyString = new SerializedString("");
        byte[] buffer = new byte[0];
        int offset = 1; // An offset that is out of bounds for the empty buffer.

        // Act
        // Attempt to append the empty string's content to the buffer.
        // The operation should fail because (offset + content_length) > buffer.length.
        int bytesAppended = emptyString.appendUnquotedUTF8(buffer, offset);

        // Assert
        // The method should return -1 to indicate failure due to insufficient buffer space.
        assertEquals("Should return -1 for insufficient buffer space", -1, bytesAppended);
    }

    /**
     * Tests that {@link SerializedString#writeUnquotedUTF8} correctly writes zero bytes
     * to an output stream when the SerializedString was created from an empty string.
     */
    @Test
    public void writeUnquotedUTF8_withEmptyString_shouldWriteZeroBytes() throws IOException {
        // Arrange
        SerializedString emptyString = new SerializedString("");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        // Write the unquoted UTF-8 representation of the empty string to the stream.
        int bytesWritten = emptyString.writeUnquotedUTF8(outputStream);

        // Assert
        // The method should report that 0 bytes were written.
        assertEquals("Bytes written should be 0 for an empty string", 0, bytesWritten);
        // The output stream should consequently be empty.
        assertEquals("Output stream should be empty", 0, outputStream.size());
    }
}