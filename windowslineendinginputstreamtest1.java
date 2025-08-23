package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
public class WindowsLineEndingInputStreamTest {

    /**
     * A helper method to read all bytes from a WindowsLineEndingInputStream and return them as a String.
     * This is more convenient than Apache Commons IOUtils.toString() for testing the byte-by-byte read() method.
     */
    private String readByteByByte(final InputStream stream) throws IOException {
        final byte[] buffer = new byte[100]; // Ample buffer for test data
        int i = 0;
        while (i < buffer.length) {
            final int b = stream.read();
            if (b == -1) {
                break;
            }
            buffer[i++] = (byte) b;
        }
        return new String(buffer, 0, i, StandardCharsets.UTF_8);
    }

    @DisplayName("mark() should be unsupported")
    @ParameterizedTest(name = "when ensureLineFeedAtEof = {0}")
    @ValueSource(booleans = { false, true })
    void markThrowsUnsupportedOperationException(final boolean ensureLineFeedAtEof) {
        // Arrange
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEof)) {
            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1),
                "mark() should throw UnsupportedOperationException");
        }
    }

    @DisplayName("markSupported() should always be false")
    @ParameterizedTest(name = "when ensureLineFeedAtEof = {0}")
    @ValueSource(booleans = { false, true })
    void markSupportedReturnsFalse(final boolean ensureLineFeedAtEof) {
        // Arrange
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEof)) {
            // Act & Assert
            assertFalse(stream.markSupported(), "markSupported() should return false");
        }
    }

    // The following tests verify that input with correct Windows line endings (CRLF) is passed through unchanged.
    // One set of tests ensures a CRLF is NOT added at the end, and the other set ensures it IS added.

    @Test
    void readByteByByte_withExistingCRLFAndEnsureEofDisabled_preservesCRLF() throws IOException {
        // Arrange
        final String input = "a\r\nbc";
        final String expected = "a\r\nbc";

        // Act
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(), false)) {
            final String result = readByteByByte(stream);
            // Assert
            assertEquals(expected, result);
        }
    }

    @Test
    void readIntoByteArray_withExistingCRLFAndEnsureEofDisabled_preservesCRLF() throws IOException {
        // Arrange
        final String input = "a\r\nbc";
        final String expected = "a\r\nbc";

        // Act
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(), false)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer);
            final String result = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            // Assert
            assertEquals(expected, result);
        }
    }

    @Test
    void readIntoByteArrayWithOffset_withExistingCRLFAndEnsureEofDisabled_preservesCRLF() throws IOException {
        // Arrange
        final String input = "a\r\nbc";
        final String expected = "a\r\nbc";

        // Act
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(), false)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            final String result = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            // Assert
            assertEquals(expected, result);
        }
    }

    @Test
    void readByteByByte_withExistingCRLFAndEnsureEofEnabled_preservesAndAddsCRLF() throws IOException {
        // Arrange
        final String input = "a\r\nbc";
        final String expected = "a\r\nbc\r\n";

        // Act
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(), true)) {
            final String result = readByteByByte(stream);
            // Assert
            assertEquals(expected, result);
        }
    }

    @Test
    void readIntoByteArray_withExistingCRLFAndEnsureEofEnabled_preservesAndAddsCRLF() throws IOException {
        // Arrange
        final String input = "a\r\nbc";
        final String expected = "a\r\nbc\r\n";

        // Act
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(), true)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer);
            final String result = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            // Assert
            assertEquals(expected, result);
        }
    }

    @Test
    void readIntoByteArrayWithOffset_withExistingCRLFAndEnsureEofEnabled_preservesAndAddsCRLF() throws IOException {
        // Arrange
        final String input = "a\r\nbc";
        final String expected = "a\r\nbc\r\n";

        // Act
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(), true)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            final String result = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            // Assert
            assertEquals(expected, result);
        }
    }
}