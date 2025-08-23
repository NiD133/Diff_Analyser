package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
class WindowsLineEndingInputStreamTest {

    /**
     * Creates a {@link WindowsLineEndingInputStream} for a given string.
     *
     * @param data The string to be read.
     * @param ensureLineFeedAtEos True to ensure the stream ends with CRLF.
     * @return A new instance of the stream under test.
     */
    private WindowsLineEndingInputStream createInputStream(final String data, final boolean ensureLineFeedAtEos) {
        final InputStream source = CharSequenceInputStream.builder()
            .setCharSequence(data)
            .setCharset(StandardCharsets.UTF_8)
            .get();
        return new WindowsLineEndingInputStream(source, ensureLineFeedAtEos);
    }

    /**
     * Reads the entire stream using the single-byte {@code read()} method and returns the content as a String.
     * This helper specifically tests the behavior of the {@code read()} override.
     */
    private String readAllUsingReadByte(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (final InputStream in = createInputStream(input, ensureLineFeedAtEos);
             final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int ch;
            while ((ch = in.read()) != -1) {
                out.write(ch);
            }
            return out.toString(StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the entire stream using the {@code read(byte[])} method and returns the content as a String.
     * This helper specifically tests the behavior of the {@code read(byte[])} override.
     */
    private String readAllUsingReadByteArray(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (final InputStream in = createInputStream(input, ensureLineFeedAtEos)) {
            // InputStream.readAllBytes() is a concise way to test reading into a byte array.
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the entire stream using the {@code read(byte[], int, int)} method and returns the content as a String.
     * This helper specifically tests the behavior of the {@code read(byte[], int, int)} override.
     */
    private String readAllUsingReadByteArrayWithOffset(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (final InputStream in = createInputStream(input, ensureLineFeedAtEos);
             final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[32]; // A small buffer to ensure multiple reads
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toString(StandardCharsets.UTF_8);
        }
    }

    @Test
    @DisplayName("A lone LF is converted to CRLF when reading byte-by-byte")
    void lfToCrlf_isConverted_whenReadingByteByByte() throws Exception {
        // Arrange
        final String input = "ab\nc";
        final String expected = "ab\r\nc";

        // Act
        final String actual = readAllUsingReadByte(input, false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("A lone LF is converted to CRLF when reading into a byte array")
    void lfToCrlf_isConverted_whenReadingIntoByteArray() throws Exception {
        // Arrange
        final String input = "ab\nc";
        final String expected = "ab\r\nc";

        // Act
        final String actual = readAllUsingReadByteArray(input, false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("A lone LF is converted to CRLF when reading into a byte array with offset")
    void lfToCrlf_isConverted_whenReadingIntoByteArrayWithOffset() throws Exception {
        // Arrange
        final String input = "ab\nc";
        final String expected = "ab\r\nc";

        // Act
        final String actual = readAllUsingReadByteArrayWithOffset(input, false);

        // Assert
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("mark() should throw UnsupportedOperationException")
    void mark_shouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        try (final InputStream stream = createInputStream("", ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("markSupported() should return false")
    void markSupported_shouldReturnFalse(final boolean ensureLineFeedAtEos) {
        try (final InputStream stream = createInputStream("", ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported());
        }
    }
}