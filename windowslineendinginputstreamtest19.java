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
@DisplayName("WindowsLineEndingInputStream")
class WindowsLineEndingInputStreamTest {

    /**
     * Processes an input string through a {@link WindowsLineEndingInputStream} by reading it
     * byte by byte and returns the resulting string.
     *
     * @param input The string to process.
     * @param ensureLineFeedAtEos Whether to configure the stream to ensure a CRLF at the end.
     * @return The processed string.
     * @throws IOException If an I/O error occurs.
     */
    private String processInputUsingSingleByteRead(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(),
            ensureLineFeedAtEos)) {

            final byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while (bytesRead < buffer.length) {
                final int b = stream.read();
                if (b == -1) {
                    break;
                }
                buffer[bytesRead++] = (byte) b;
            }
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("mark() should always throw UnsupportedOperationException")
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("markSupported() should always return false")
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported());
        }
    }

    @Test
    @DisplayName("Should not alter input that already has trailing Windows line endings")
    void shouldPreserveTrailingWindowsLineEndings() throws Exception {
        final String inputWithWindowsLineEndings = "a\r\n\r\n";
        final String result = processInputUsingSingleByteRead(inputWithWindowsLineEndings, true);
        assertEquals(inputWithWindowsLineEndings, result);
    }
}