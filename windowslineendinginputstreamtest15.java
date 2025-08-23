package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
class WindowsLineEndingInputStreamTest {

    /**
     * Processes an input string through the WindowsLineEndingInputStream and reads the result
     * using the {@code read(byte[])} method.
     *
     * @param input The string to process.
     * @param ensureLineFeedAtEof The flag for the stream constructor.
     * @return The processed string.
     */
    private String processWithReadByteArray(final String input, final boolean ensureLineFeedAtEof) throws IOException {
        try (InputStream original = CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get();
             WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(original, ensureLineFeedAtEof)) {
            final byte[] buffer = new byte[1024];
            final int bytesRead = stream.read(buffer);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Processes an input string through the WindowsLineEndingInputStream and reads the result
     * using the {@code read(byte[], int, int)} method.
     *
     * @param input The string to process.
     * @param ensureLineFeedAtEof The flag for the stream constructor.
     * @return The processed string.
     */
    private String processWithReadByteArrayRange(final String input, final boolean ensureLineFeedAtEof) throws IOException {
        try (InputStream original = CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get();
             WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(original, ensureLineFeedAtEof)) {
            final byte[] buffer = new byte[1024];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    @Test
    @DisplayName("When ensureLineFeedAtEof is false, the stream content should not be modified")
    void testStreamContentIsUnchangedWhenEnsureLineFeedAtEofIsFalse() throws IOException {
        // Arrange: Define inputs that should pass through unchanged.
        final String inputWithLineEndings = "a\r\n\r\n";
        final String inputWithoutLineEndings = "a";

        // Act & Assert: Verify that both read() overloads produce the original string.
        assertEquals(inputWithLineEndings, processWithReadByteArray(inputWithLineEndings, false));
        assertEquals(inputWithLineEndings, processWithReadByteArrayRange(inputWithLineEndings, false));

        assertEquals(inputWithoutLineEndings, processWithReadByteArray(inputWithoutLineEndings, false));
        assertEquals(inputWithoutLineEndings, processWithReadByteArrayRange(inputWithoutLineEndings, false));
    }

    @Nested
    @DisplayName("Mark/reset support")
    class MarkResetTest {

        @ParameterizedTest
        @ValueSource(booleans = {false, true})
        @DisplayName("mark() should always throw UnsupportedOperationException")
        void markShouldThrowException(final boolean ensureLineFeedAtEof) {
            try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEof)) {
                assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
            } catch (final IOException e) {
                // Should not happen with NullInputStream
            }
        }

        @ParameterizedTest
        @ValueSource(booleans = {false, true})
        @DisplayName("markSupported() should always return false")
        void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEof) {
            try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEof)) {
                assertFalse(stream.markSupported());
            } catch (final IOException e) {
                // Should not happen with NullInputStream
            }
        }
    }
}