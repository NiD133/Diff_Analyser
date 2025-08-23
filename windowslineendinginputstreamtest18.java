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
     * Provides test cases for line ending conversions.
     *
     * @return A stream of arguments: test name, input string, ensureEof flag, expected output.
     */
    static Stream<Arguments> lineEndingConversionTestCases() {
        return Stream.of(
            // Cases where ensureLineFeedAtEos is true (should add CRLF at EOF if not present)
            Arguments.of("Simple string without line endings", "abc", true, "abc\r\n"),
            Arguments.of("String with LF", "a\nb\nc", true, "a\r\nb\r\nc\r\n"),
            Arguments.of("String with CRLF", "a\r\nb\r\nc", true, "a\r\nb\r\nc\r\n"),
            Arguments.of("String with lone CR", "a\rb\rc", true, "a\r\nb\r\nc\r\n"),
            Arguments.of("String ending with LF", "abc\n", true, "abc\r\n"),
            Arguments.of("String ending with CR", "abc\r", true, "abc\r\n"),
            Arguments.of("String ending with CRLF", "abc\r\n", true, "abc\r\n"),
            Arguments.of("Empty string", "", true, "\r\n"),

            // Cases where ensureLineFeedAtEos is false (should not add CRLF at EOF)
            Arguments.of("Simple string without line endings", "abc", false, "abc"),
            Arguments.of("String with LF", "a\nb\nc", false, "a\r\nb\r\nc"),
            Arguments.of("String with CRLF", "a\r\nb\r\nc", false, "a\r\nb\r\nc"),
            Arguments.of("String with lone CR", "a\rb\rc", false, "a\r\nb\r\nc"),
            Arguments.of("String ending with LF", "abc\n", false, "abc\r\n"),
            Arguments.of("String ending with CR", "abc\r", false, "abc\r\n"),
            Arguments.of("String ending with CRLF", "abc\r\n", false, "abc\r\n"),
            Arguments.of("Empty string", "", false, "")
        );
    }

    @ParameterizedTest(name = "[{index}] {0} (ensureEof={2})")
    @MethodSource("lineEndingConversionTestCases")
    @DisplayName("Should correctly convert line endings to CRLF")
    void shouldCorrectlyConvertLineEndings(final String testName, final String input, final boolean ensureEof, final String expected) throws IOException {
        // Test all three read() methods to ensure consistent behavior.
        assertEquals(expected, readStringUsingReadByteByByte(input, ensureEof), "Mismatch when reading byte-by-byte");
        assertEquals(expected, readStringUsingReadByteArray(input, ensureEof), "Mismatch when reading into a byte array");
        assertEquals(expected, readStringUsingReadByteArrayWithOffset(input, ensureEof), "Mismatch when reading into a byte array with offset");
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("mark() should be unsupported")
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEndOfFile) {
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("markSupported() should always be false")
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEndOfFile) {
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile)) {
            assertFalse(stream.markSupported());
        }
    }

    /**
     * Reads the entire stream using the single-byte {@code read()} method.
     */
    private String readStringUsingReadByteByByte(final String input, final boolean ensureEof) throws IOException {
        try (InputStream stream = createInputStream(input, ensureEof)) {
            final byte[] buffer = new byte[100];
            int i = 0;
            int b;
            while ((b = stream.read()) != -1 && i < buffer.length) {
                buffer[i++] = (byte) b;
            }
            return new String(buffer, 0, i, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the entire stream using the {@code read(byte[])} method.
     */
    private String readStringUsingReadByteArray(final String input, final boolean ensureEof) throws IOException {
        try (InputStream stream = createInputStream(input, ensureEof)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer);
            return new String(buffer, 0, Math.max(0, bytesRead), StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the entire stream using the {@code read(byte[], int, int)} method.
     */
    private String readStringUsingReadByteArrayWithOffset(final String input, final boolean ensureEof) throws IOException {
        try (InputStream stream = createInputStream(input, ensureEof)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            return new String(buffer, 0, Math.max(0, bytesRead), StandardCharsets.UTF_8);
        }
    }

    private InputStream createInputStream(final String text, final boolean ensureEof) {
        return new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder()
                .setCharSequence(text)
                .setCharset(StandardCharsets.UTF_8)
                .get(),
            ensureEof
        );
    }
}