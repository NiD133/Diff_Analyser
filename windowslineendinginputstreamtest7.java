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
 * This suite focuses on mark/reset support and handling of malformed line endings.
 */
class WindowsLineEndingInputStreamTest {

    /**
     * Creates a {@link WindowsLineEndingInputStream} from a given string.
     *
     * @param input The string to be read.
     * @param ensureLineFeedAtEos Corresponds to the constructor parameter.
     * @return A new {@link WindowsLineEndingInputStream}.
     */
    private WindowsLineEndingInputStream createInputStream(final String input, final boolean ensureLineFeedAtEos) {
        return new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder()
                .setCharSequence(input)
                .setCharset(StandardCharsets.UTF_8)
                .get(),
            ensureLineFeedAtEos);
    }

    /**
     * Reads the full content of a stream by calling the single-byte {@code read()} method repeatedly.
     *
     * @param input The string to be read.
     * @param ensureLineFeedAtEos Whether to ensure a line feed at the end of the stream.
     * @return The content read from the stream.
     * @throws IOException If an I/O error occurs.
     */
    private String readStringUsingReadByteByByte(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (WindowsLineEndingInputStream stream = createInputStream(input, ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[1024];
            int bytesRead = 0;
            int data;
            // Read byte by byte to specifically test the read() method
            while (bytesRead < buffer.length && (data = stream.read()) != -1) {
                buffer[bytesRead++] = (byte) data;
            }
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    @DisplayName("mark() should not be supported")
    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        // Arrange
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @DisplayName("markSupported() should always return false")
    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        // Arrange
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            // Act & Assert
            assertFalse(stream.markSupported());
        }
    }

    @Test
    @DisplayName("A lone carriage return should be preserved when not ensuring a final line feed")
    void loneCarriageReturnIsPreservedWhenNotEnsuringEof() throws IOException {
        // Arrange
        final String inputWithLoneCR = "a\rbc";
        final String expectedOutput = "a\rbc";
        final boolean ensureLineFeedAtEos = false;

        // Act
        final String actualOutput = readStringUsingReadByteByByte(inputWithLoneCR, ensureLineFeedAtEos);

        // Assert
        assertEquals(expectedOutput, actualOutput,
            "A lone CR should pass through unchanged when ensureLineFeedAtEos is false.");
    }
}