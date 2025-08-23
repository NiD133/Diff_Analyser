package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Creates a WindowsLineEndingInputStream for a given string and reads it back to a string
     * by calling the single-byte {@code read()} method repeatedly.
     *
     * @param input The string to process.
     * @param ensureLineFeedAtEos Whether to ensure a CRLF at the end of the stream.
     * @return The transformed string.
     */
    private String transformWithRead(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(),
                ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[100];
            int i = 0;
            while (i < buffer.length) {
                final int read = stream.read();
                if (read < 0) {
                    break;
                }
                buffer[i++] = (byte) read;
            }
            return new String(buffer, 0, i, StandardCharsets.UTF_8);
        }
    }

    /**
     * Creates a WindowsLineEndingInputStream for a given string and reads it back to a string
     * by calling the {@code read(byte[])} method.
     *
     * @param input The string to process.
     * @param ensureLineFeedAtEos Whether to ensure a CRLF at the end of the stream.
     * @return The transformed string.
     */
    private String transformWithReadByteArray(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(),
                ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[100];
            final int read = stream.read(buffer);
            return new String(buffer, 0, read, StandardCharsets.UTF_8);
        }
    }

    /**
     * Creates a WindowsLineEndingInputStream for a given string and reads it back to a string
     * by calling the {@code read(byte[], int, int)} method.
     *
     * @param input The string to process.
     * @param ensureLineFeedAtEos Whether to ensure a CRLF at the end of the stream.
     * @return The transformed string.
     */
    private String transformWithReadByteArrayWithOffset(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(),
                ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[100];
            final int read = stream.read(buffer, 0, 100);
            return new String(buffer, 0, read, StandardCharsets.UTF_8);
        }
    }

    @DisplayName("mark() should not be supported")
    @ParameterizedTest(name = "ensureLineFeedAtEos = {0}")
    @ValueSource(booleans = {false, true})
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        // Use try-with-resources for correctness, although NullInputStream requires no cleanup.
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @DisplayName("markSupported() should always return false")
    @ParameterizedTest(name = "ensureLineFeedAtEos = {0}")
    @ValueSource(booleans = {false, true})
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        // Use try-with-resources to avoid resource leak warnings and ensure correctness.
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported());
        }
    }

    static Stream<Arguments> simpleStringTransformationProvider() {
        return Stream.of(
            // If ensureLineFeedAtEos is true, a CRLF should be added.
            Arguments.of(true, "abc\r\n"),
            // If ensureLineFeedAtEos is false, the string should remain unchanged.
            Arguments.of(false, "abc")
        );
    }

    @DisplayName("Test transformation of a simple string without a newline")
    @ParameterizedTest(name = "With ensureLineFeedAtEos={0}, output should be \"{1}\"")
    @MethodSource("simpleStringTransformationProvider")
    void testTransformationOfSimpleStringWithoutNewline(final boolean ensureLineFeedAtEos, final String expected) throws Exception {
        final String input = "abc";

        // Assert that the behavior is consistent across all read() overloads
        assertEquals(expected, transformWithRead(input, ensureLineFeedAtEos), "Mismatch for read()");
        assertEquals(expected, transformWithReadByteArray(input, ensureLineFeedAtEos), "Mismatch for read(byte[])");
        assertEquals(expected, transformWithReadByteArrayWithOffset(input, ensureLineFeedAtEos), "Mismatch for read(byte[], int, int)");
    }
}