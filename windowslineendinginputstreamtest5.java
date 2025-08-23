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
@DisplayName("WindowsLineEndingInputStream")
class WindowsLineEndingInputStreamTest {

    /**
     * Creates a {@link WindowsLineEndingInputStream} from a given string input.
     *
     * @param input The string to be read by the stream.
     * @param ensureLineFeedAtEos The flag for the stream's constructor.
     * @return A new {@link WindowsLineEndingInputStream}.
     */
    private WindowsLineEndingInputStream createStream(final String input, final boolean ensureLineFeedAtEos) {
        final InputStream source = CharSequenceInputStream.builder()
            .setCharSequence(input)
            .setCharset(StandardCharsets.UTF_8)
            .get();
        return new WindowsLineEndingInputStream(source, ensureLineFeedAtEos);
    }

    @DisplayName("mark() should not be supported")
    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        try (final InputStream stream = createStream("", ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @DisplayName("markSupported() should always return false")
    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        try (final InputStream stream = createStream("", ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported());
        }
    }

    @Test
    @DisplayName("read() should replace a lone LF with CRLF when reading byte by byte")
    void readByteByByte_shouldReplaceLFWithCRLF() throws IOException {
        // Arrange
        final String input = "ab\nc";
        final String expected = "ab\r\nc";
        final boolean ensureLineFeedAtEos = false;

        try (final InputStream stream = createStream(input, ensureLineFeedAtEos);
             final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Act
            int ch;
            while ((ch = stream.read()) != -1) {
                out.write(ch);
            }
            final String actual = out.toString(StandardCharsets.UTF_8);

            // Assert
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("read(byte[]) should replace a lone LF with CRLF")
    void readIntoByteArray_shouldReplaceLFWithCRLF() throws IOException {
        // Arrange
        final String input = "ab\nc";
        final String expected = "ab\r\nc";
        final boolean ensureLineFeedAtEos = false;

        try (final InputStream stream = createStream(input, ensureLineFeedAtEos)) {
            // Act
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer);
            final String actual = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

            // Assert
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("read(byte[], int, int) should replace a lone LF with CRLF")
    void readIntoByteArrayWithOffset_shouldReplaceLFWithCRLF() throws IOException {
        // Arrange
        final String input = "ab\nc";
        final String expected = "ab\r\nc";
        final boolean ensureLineFeedAtEos = false;

        try (final InputStream stream = createStream(input, ensureLineFeedAtEos)) {
            // Act
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            final String actual = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

            // Assert
            assertEquals(expected, actual);
        }
    }
}