package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link UTF8Writer} class, focusing on encoding correctness.
 */
// Renamed from UTF8WriterTestTest2 for clarity and convention.
class UTF8WriterTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    /**
     * Verifies that the UTF8Writer correctly encodes a string containing both
     * single-byte ASCII characters and a multi-byte character (U+00A0).
     * This ensures the writer produces the correct byte sequence and length.
     */
    @Test
    void shouldCorrectlyEncodeStringWithMultiByteCharacter() throws IOException {
        // Arrange
        final String asciiPart = "abcdefghijklmnopqrst";
        final String multiByteChar = "\u00A0"; // Non-breaking space, encoded as 2 bytes in UTF-8
        final String inputString = asciiPart + multiByteChar;
        final char[] inputChars = inputString.toCharArray();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // The IOContext is a factory for buffers, part of Jackson's internal mechanics.
        IOContext ioContext = _ioContext();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // Act
        utf8Writer.write(inputChars, 0, inputChars.length);
        utf8Writer.flush(); // Ensure internal buffer is written before closing
        utf8Writer.close(); // Closes the writer and the underlying stream

        // Assert
        byte[] resultBytes = outputStream.toByteArray();
        String resultString = utf8String(outputStream); // Helper from JUnit5TestBase

        // The non-breaking space (U+00A0) is encoded as 2 bytes in UTF-8 (0xC2, 0xA0).
        // The other 20 characters are single-byte ASCII.
        // Expected length = 20 * 1 (ASCII) + 1 * 2 (U+00A0) = 22 bytes.
        int expectedByteLength = asciiPart.length() + 2;
        assertEquals(expectedByteLength, resultBytes.length, "Unexpected byte array length");

        assertEquals(inputString, resultString, "Decoded string should match the original input");
    }
}