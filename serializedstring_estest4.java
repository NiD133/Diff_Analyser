package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link SerializedString} class, focusing on its
 * writing capabilities.
 */
public class SerializedStringTest {

    /**
     * Tests that writing a quoted UTF-8 representation of an empty SerializedString
     * to an OutputStream results in zero bytes being written. This is an important
     * edge case to ensure correct handling of empty strings.
     */
    @Test
    public void writeQuotedUTF8_withEmptyString_shouldWriteZeroBytes() throws IOException {
        // Arrange: Create a SerializedString from an empty string and an output stream to capture the result.
        SerializedString emptySerializedString = new SerializedString("");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act: Write the quoted UTF-8 content to the stream.
        int bytesWritten = emptySerializedString.writeQuotedUTF8(outputStream);

        // Assert: Verify that the number of bytes written is zero and the stream is indeed empty.
        assertEquals("The method should report that 0 bytes were written.", 0, bytesWritten);
        assertEquals("The output stream should contain no data.", 0, outputStream.size());
    }
}