package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
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
     * A functional interface to define different ways of reading an InputStream into a String.
     */
    @FunctionalInterface
    private interface InputStreamReader {
        String readAll(InputStream in) throws IOException;
    }

    /**
     * Provides different strategies for reading the input stream to test all `read` methods.
     */
    private static Stream<Arguments> readingStrategies() {
        // Reads the stream byte by byte, testing read()
        final InputStreamReader readByteByByte = in -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b;
            while ((b = in.read()) != -1) {
                baos.write(b);
            }
            return baos.toString(StandardCharsets.UTF_8);
        };

        // Reads the stream into a buffer, testing read(byte[])
        final InputStreamReader readIntoBuffer = in -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[128];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toString(StandardCharsets.UTF_8);
        };

        // Reads the stream into a buffer with offset, testing read(byte[], int, int)
        final InputStreamReader readIntoBufferWithOffset = in -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[128];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toString(StandardCharsets.UTF_8);
        };

        return Stream.of(
            Arguments.of(Named.of("using read()", readByteByByte)),
            Arguments.of(Named.of("using read(byte[])", readIntoBuffer)),
            Arguments.of(Named.of("using read(byte[], int, int)", readIntoBufferWithOffset))
        );
    }

    private WindowsLineEndingInputStream createInputStream(final String input, final boolean ensureLineFeedAtEndOfFile) {
        return new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get(),
            ensureLineFeedAtEndOfFile);
    }

    @DisplayName("markSupported() should always return false")
    @ParameterizedTest(name = "when ensureLineFeedAtEndOfFile is {0}")
    @ValueSource(booleans = {false, true})
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEndOfFile) {
        try (InputStream stream = createInputStream("", ensureLineFeedAtEndOfFile)) {
            assertFalse(stream.markSupported());
        }
    }

    @DisplayName("mark() should throw UnsupportedOperationException")
    @ParameterizedTest(name = "when ensureLineFeedAtEndOfFile is {0}")
    @ValueSource(booleans = {false, true})
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEndOfFile) {
        try (InputStream stream = createInputStream("", ensureLineFeedAtEndOfFile)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @DisplayName("Should convert LF to CRLF")
    @ParameterizedTest(name = "{0}")
    @MethodSource("readingStrategies")
    void loneLineFeedIsConvertedToCrLf(final InputStreamReader reader) throws IOException {
        final String input = "a\nbc\n";
        final String expected = "a\r\nbc\r\n";

        // The conversion should happen regardless of the ensureLineFeedAtEndOfFile flag.
        try (InputStream stream = createInputStream(input, false)) {
            assertEquals(expected, reader.readAll(stream));
        }
        try (InputStream stream = createInputStream(input, true)) {
            assertEquals(expected, reader.readAll(stream));
        }
    }

    @DisplayName("Should not modify lone CR in the middle of a stream")
    @ParameterizedTest(name = "{0}")
    @MethodSource("readingStrategies")
    void loneCarriageReturnInMiddleOfStreamIsPreserved(final InputStreamReader reader) throws IOException {
        final String input = "a\rbc";

        // A lone CR not at the end of the file should be preserved regardless of the flag.
        try (InputStream stream = createInputStream(input, false)) {
            assertEquals(input, reader.readAll(stream));
        }
        try (InputStream stream = createInputStream(input, true)) {
            assertEquals(input, reader.readAll(stream));
        }
    }

    @DisplayName("Should handle lone CR at the end of the file based on the flag")
    @ParameterizedTest(name = "{0}")
    @MethodSource("readingStrategies")
    void loneCarriageReturnAtEofIsHandledCorrectly(final InputStreamReader reader) throws IOException {
        final String input = "abc\r";

        // When ensuring EOF line feed, a lone CR should be completed with an LF.
        try (InputStream stream = createInputStream(input, true)) {
            assertEquals("abc\r\n", reader.readAll(stream));
        }

        // When not ensuring, a lone CR should be preserved.
        try (InputStream stream = createInputStream(input, false)) {
            assertEquals("abc\r", reader.readAll(stream));
        }
    }

    @DisplayName("Should handle file ending without a newline based on the flag")
    @ParameterizedTest(name = "{0}")
    @MethodSource("readingStrategies")
    void fileEndingWithoutNewlineIsHandledCorrectly(final InputStreamReader reader) throws IOException {
        final String input = "abc";

        // When ensuring EOF line feed, CRLF should be added.
        try (InputStream stream = createInputStream(input, true)) {
            assertEquals("abc\r\n", reader.readAll(stream));
        }

        // When not ensuring, the stream should remain unchanged.
        try (InputStream stream = createInputStream(input, false)) {
            assertEquals("abc", reader.readAll(stream));
        }
    }
}