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
 * This class focuses on mark/reset support and handling of malformed line endings.
 */
@DisplayName("WindowsLineEndingInputStream")
public class WindowsLineEndingInputStreamTest {

    /**
     * Processes a given string through a {@link WindowsLineEndingInputStream} and returns the result.
     * This helper simulates reading from the stream to verify its transformation logic.
     *
     * @param input The string to be processed.
     * @param ensureLineFeedAtEos The constructor parameter for the stream, determining if a CRLF
     *                            should be enforced at the end of the stream.
     * @return The processed string after passing through the stream.
     * @throws IOException If an I/O error occurs.
     */
    private String getStreamOutput(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        final InputStream source = CharSequenceInputStream.builder()
                .setCharSequence(input)
                .setCharset(StandardCharsets.UTF_8)
                .get();
        
        try (final WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(source, ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = windowsStream.read(buffer);
            if (bytesRead == -1) {
                return "";
            }
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("mark() should throw UnsupportedOperationException")
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        // Use try-with-resources to ensure the stream is properly closed.
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1),
                "mark() is not supported and should throw an exception.");
        } catch (final IOException e) {
            // This exception is not expected from NullInputStream.
        }
    }

    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    @DisplayName("markSupported() should return false")
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) throws IOException {
        // Use try-with-resources to avoid resource leaks and suppressions.
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported(),
                "The stream should report that mark/reset is not supported.");
        }
    }

    @Test
    @DisplayName("should preserve a lone CR when not ensuring a line feed at stream end")
    void loneCarriageReturnIsPreservedWhenNotEnsuringEofLineEnding() throws Exception {
        // A lone CR ('\r') is considered "malformed" as it's not part of a CRLF sequence.
        // This test verifies it's passed through unchanged under a specific configuration.
        final String inputWithLoneCR = "a\rbc";
        final String expectedOutput = "a\rbc";
        final boolean ensureLineFeedAtEos = false;

        final String actualOutput = getStreamOutput(inputWithLoneCR, ensureLineFeedAtEos);

        assertEquals(expectedOutput, actualOutput,
            "When not ensuring a line feed at EOF, a lone CR should pass through unchanged.");
    }
}