package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class, focusing on its
 * append operations.
 */
public class SerializedStringTest {

    /**
     * Tests that calling {@link SerializedString#appendUnquotedUTF8(byte[], int)}
     * on an instance created from an empty string correctly appends zero bytes
     * and returns 0.
     */
    @Test
    public void appendUnquotedUTF8_withEmptyString_shouldAppendZeroBytesAndReturnZero() {
        // Arrange: Create a SerializedString from an empty string and a destination buffer.
        SerializedString emptySerializedString = new SerializedString("");
        byte[] destinationBuffer = new byte[10];
        int offset = 0;

        // Act: Attempt to append the unquoted UTF-8 bytes to the buffer.
        int bytesAppended = emptySerializedString.appendUnquotedUTF8(destinationBuffer, offset);

        // Assert: Verify that the number of bytes appended is zero.
        assertEquals("The number of appended bytes should be 0 for an empty string", 0, bytesAppended);
    }
}