package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
class WindowsLineEndingInputStreamTest {

    /**
     * An enumeration of the different read methods on an InputStream.
     * This is used to parameterize tests to ensure consistent behavior.
     */
    private enum ReadMethod {
        BYTE_BY_BYTE,
        BYTE_ARRAY,
        BYTE_ARRAY_WITH_OFFSET
    }

    /**
     * Creates a {@link WindowsLineEndingInputStream} for a given string input.
     *
     * @param input The string to be read.
     * @param ensureLineFeedAtEos Corresponds to the constructor parameter.
     * @return A new instance of the stream under test.
     */
    private WindowsLineEndingInputStream createStream(final String input, final boolean ensureLineFeedAtEos) {
        return new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder()
                .setCharSequence(input)
                .setCharset(StandardCharsets.UTF_8)
                .get(),
            ensureLineFeedAtEos
        );
    }

    /**
     * Reads all bytes from the stream using one of the three read methods.
     *
     * @param stream The input stream to read from.
     * @param readMethod The {@link ReadMethod} to use.
     * @return The content of the stream as a String.
     * @throws IOException If an I/O error occurs.
     */
    private String readAllBytesWithMethod(final InputStream stream, final ReadMethod readMethod) throws IOException {
        final byte[] buffer = new byte[100];
        int bytesRead;

        switch (readMethod) {
            case BYTE_ARRAY:
                bytesRead = stream.read(buffer);
                return bytesRead > 0 ? new String(buffer, 0, bytesRead, StandardCharsets.UTF_8) : "";

            case BYTE_ARRAY_WITH_OFFSET:
                bytesRead = stream.read(buffer, 0, buffer.length);
                return bytesRead > 0 ? new String(buffer, 0, bytesRead, StandardCharsets.UTF_8) : "";

            case BYTE_BY_BYTE:
            default:
                final StringBuilder sb = new StringBuilder();
                int b;
                while ((b = stream.read()) != -1) {
                    sb.append((char) b);
                }
                return sb.toString();
        }
    }

    @Nested
    @DisplayName("InputStream Contract Tests")
    class InputStreamContractTest {

        @DisplayName("mark() should throw UnsupportedOperationException")
        @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
        @ValueSource(booleans = {false, true})
        void mark_shouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
            try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
                assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
            }
        }

        @DisplayName("markSupported() should return false")
        @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
        @ValueSource(booleans = {false, true})
        void markSupported_shouldReturnFalse(final boolean ensureLineFeedAtEos) {
            try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
                assertFalse(stream.markSupported());
            }
        }
    }

    @Nested
    @DisplayName("Line Ending Conversion Tests")
    class ConversionTest {

        static Stream<Arguments> readMethods() {
            return Stream.of(
                Arguments.of(ReadMethod.BYTE_BY_BYTE),
                Arguments.of(ReadMethod.BYTE_ARRAY),
                Arguments.of(ReadMethod.BYTE_ARRAY_WITH_OFFSET)
            );
        }

        @DisplayName("Should convert LF to CRLF")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("readMethods")
        void shouldConvertLfToCrLf(final ReadMethod readMethod) throws IOException {
            final String input = "a\nb\nc";
            final String expected = "a\r\nb\r\nc";

            try (final WindowsLineEndingInputStream stream = createStream(input, false)) {
                final String result = readAllBytesWithMethod(stream, readMethod);
                assertEquals(expected, result);
            }
        }

        @DisplayName("Should not alter input that already has CRLF endings when ensure is false")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("readMethods")
        void shouldNotAlterInputWithCrLfWhenEnsureIsFalse(final ReadMethod readMethod) throws IOException {
            final String input = "a\r\n\r\n";
            final String expected = "a\r\n\r\n";

            try (final WindowsLineEndingInputStream stream = createStream(input, false)) {
                final String result = readAllBytesWithMethod(stream, readMethod);
                assertEquals(expected, result);
            }
        }

        @DisplayName("Should not alter input without line endings when ensure is false")
        @ParameterizedTest(name = "using {0}")
        @MethodSource("readMethods")
        void shouldNotAlterInputWithoutLineEndingsWhenEnsureIsFalse(final ReadMethod readMethod) throws IOException {
            final String input = "a";
            final String expected = "a";

            try (final WindowsLineEndingInputStream stream = createStream(input, false)) {
                final String result = readAllBytesWithMethod(stream, readMethod);
                assertEquals(expected, result);
            }
        }
    }
}