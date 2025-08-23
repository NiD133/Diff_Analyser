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
class WindowsLineEndingInputStreamTest {

    /**
     * Creates a {@link WindowsLineEndingInputStream} for a given string input.
     *
     * @param input The string to be used as the source for the input stream.
     * @param ensureLineFeedAtEos Corresponds to the constructor parameter of the class under test.
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
     * Reads the entire content of a given stream using the single-byte {@code read()} method.
     */
    private String readFullyUsingByteByByteRead(final InputStream in) throws IOException {
        final byte[] buffer = new byte[1024];
        int bytesRead = 0;
        int b;
        while ((b = in.read()) != -1) {
            buffer[bytesRead++] = (byte) b;
        }
        return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
    }

    /**
     * Reads the entire content of a given stream using the {@code read(byte[])} method.
     */
    private String readFullyUsingByteArrayRead(final InputStream in) throws IOException {
        final byte[] buffer = new byte[1024];
        final int bytesRead = in.read(buffer);
        return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
    }

    /**
     * Reads the entire content of a given stream using the {@code read(byte[], int, int)} method.
     */
    private String readFullyUsingByteArrayReadWithOffset(final InputStream in) throws IOException {
        final byte[] buffer = new byte[1024];
        final int bytesRead = in.read(buffer, 0, buffer.length);
        return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("mark() should throw UnsupportedOperationException regardless of constructor parameters")
    void mark_shouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        try (final InputStream in = createInputStream("", ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> in.mark(1));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("markSupported() should return false regardless of constructor parameters")
    void markSupported_shouldReturnFalse(final boolean ensureLineFeedAtEos) {
        try (final InputStream in = createInputStream("", ensureLineFeedAtEos)) {
            assertFalse(in.markSupported());
        }
    }

    @Test
    @DisplayName("Should add CRLF at end of stream if ensureLineFeedAtEos is true and input does not end with a newline")
    void shouldAddCrlfAtEndWhenEnsureIsTrue() throws Exception {
        final String input = "a\r\nbc";
        final String expectedOutput = "a\r\nbc\r\n";
        final boolean ensureLineFeedAtEos = true;

        // Test behavior for all three read() method variants
        assertEquals(expectedOutput, readFullyUsingByteByByteRead(createInputStream(input, ensureLineFeedAtEos)));
        assertEquals(expectedOutput, readFullyUsingByteArrayRead(createInputStream(input, ensureLineFeedAtEos)));
        assertEquals(expectedOutput, readFullyUsingByteArrayReadWithOffset(createInputStream(input, ensureLineFeedAtEos)));
    }

    @Test
    @DisplayName("Should NOT add CRLF at end of stream if ensureLineFeedAtEos is false")
    void shouldNotAddCrlfAtEndWhenEnsureIsFalse() throws Exception {
        final String input = "a\r\nbc";
        final String expectedOutput = "a\r\nbc"; // Expect no change
        final boolean ensureLineFeedAtEos = false;

        // Test behavior for all three read() method variants
        assertEquals(expectedOutput, readFullyUsingByteByByteRead(createInputStream(input, ensureLineFeedAtEos)));
        assertEquals(expectedOutput, readFullyUsingByteArrayRead(createInputStream(input, ensureLineFeedAtEos)));
        assertEquals(expectedOutput, readFullyUsingByteArrayReadWithOffset(createInputStream(input, ensureLineFeedAtEos)));
    }
}