package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that attempting to append the quoted UTF-8 representation of an
     * empty string to a buffer correctly results in zero bytes being appended.
     */
    @Test
    public void appendQuotedUTF8_withEmptyString_shouldAppendZeroBytes() {
        // Arrange: Create a SerializedString from an empty string.
        SerializedString emptyString = new SerializedString("");
        byte[] destinationBuffer = new byte[10];
        int offset = 1;

        // Act: Attempt to append the empty string's content to the buffer.
        int bytesAppended = emptyString.appendQuotedUTF8(destinationBuffer, offset);

        // Assert: The method should report that zero bytes were appended.
        assertEquals("Appending an empty string should result in zero bytes written", 0, bytesAppended);
    }
}