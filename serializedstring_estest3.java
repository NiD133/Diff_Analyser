package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its writing capabilities.
 */
public class SerializedStringTest {

    /**
     * Tests that {@link SerializedString#writeUnquotedUTF8(OutputStream)}
     * correctly writes the unquoted UTF-8 representation of the string to an OutputStream
     * and returns the correct number of bytes written.
     */
    @Test
    public void writeUnquotedUTF8_shouldWriteCorrectBytesAndReturnLength() throws IOException {
        // Arrange: Create a test string and a stream to capture the output.
        final String testString = "E]`R4#OI%";
        final SerializedString serializedString = new SerializedString(testString);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act: Call the method under test.
        final int bytesWritten = serializedString.writeUnquotedUTF8(outputStream);

        // Assert: Verify the output is correct.
        final byte[] expectedBytes = testString.getBytes(StandardCharsets.UTF_8);

        // 1. Verify that the method returns the correct number of bytes written.
        assertEquals("The method should return the number of bytes written.",
                expectedBytes.length, bytesWritten);

        // 2. Verify that the correct bytes were written to the output stream.
        assertArrayEquals("The bytes written to the stream should match the string's UTF-8 representation.",
                expectedBytes, outputStream.toByteArray());
    }
}