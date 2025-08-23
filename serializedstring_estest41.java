package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class, focusing on its
 * byte-appending functionality.
 */
public class SerializedStringTest {

    /**
     * Verifies that appendUnquotedUTF8 correctly appends the string's UTF-8 bytes
     * into a destination buffer and returns the number of bytes written.
     */
    @Test
    public void appendUnquotedUTF8_shouldAppendBytesAndReturnCorrectLength() {
        // Arrange
        final String content = "TP1aO6Zd";
        final SerializedString serializedString = new SerializedString(content);
        final byte[] destinationBuffer = new byte[16]; // A buffer larger than the content
        final int offset = 0;

        // Act
        final int bytesAppended = serializedString.appendUnquotedUTF8(destinationBuffer, offset);

        // Assert
        // 1. Verify the method returns the correct number of bytes appended.
        final byte[] expectedBytes = content.getBytes(StandardCharsets.UTF_8);
        assertEquals("The number of appended bytes should match the content's UTF-8 length.",
                     expectedBytes.length, bytesAppended);

        // 2. Verify the content was actually written to the destination buffer.
        final byte[] actualAppendedBytes = new byte[bytesAppended];
        System.arraycopy(destinationBuffer, offset, actualAppendedBytes, 0, bytesAppended);
        assertArrayEquals("The destination buffer should contain the correct byte sequence.",
                          expectedBytes, actualAppendedBytes);
    }
}