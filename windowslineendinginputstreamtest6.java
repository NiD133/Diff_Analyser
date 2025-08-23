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

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
class WindowsLineEndingInputStreamTest {

    // A functional interface to represent different ways of reading from the stream.
    @FunctionalInterface
    private interface TestReader {
        String read(WindowsLineEndingInputStream in) throws IOException;
    }

    // Reader strategy that uses the read() method.
    private static String readByteByByte(final WindowsLineEndingInputStream in) throws IOException {
        final byte[] buf = new byte[100];
        int i = 0;
        while (i < buf.length) {
            final int b = in.read();
            if (b == -1) {
                break;
            }
            buf[i++] = (byte) b;
        }
        return new String(buf, 0, i, StandardCharsets.UTF_8);
    }

    // Reader strategy that uses the read(byte[]) method.
    private static String readIntoByteArray(final WindowsLineEndingInputStream in) throws IOException {
        final byte[] buf = new byte[100];
        final int bytesRead = in.read(buf);
        return bytesRead == -1 ? "" : new String(buf, 0, bytesRead, StandardCharsets.UTF_8);
    }

    // Reader strategy that uses the read(byte[], int, int) method.
    private static String readIntoByteArrayRange(final WindowsLineEndingInputStream in) throws IOException {
        final byte[] buf = new byte[100];
        final int bytesRead = in.read(buf, 0, buf.length);
        return bytesRead == -1 ? "" : new String(buf, 0, bytesRead, StandardCharsets.UTF_8);
    }

    static Stream<Arguments> lineEndingConversionScenarios() {
        final TestReader byteByByteReader = WindowsLineEndingInputStreamTest::readByteByByte;
        final TestReader byteArrayReader = WindowsLineEndingInputStreamTest::readIntoByteArray;
        final TestReader byteArrayRangeReader = WindowsLineEndingInputStreamTest::readIntoByteArrayRange;

        return Stream.of(
            // Arguments: test case name, input string, expected output string, reader name, reader implementation
            Arguments.of("Linux EOL", "ab\nc", "ab\r\nc", "read()", byteByByteReader),
            Arguments.of("Linux EOL", "ab\nc", "ab\r\nc", "read(byte[])", byteArrayReader),
            Arguments.of("Linux EOL", "ab\nc", "ab\r\nc", "read(byte[], int, int)", byteArrayRangeReader),

            Arguments.of("No EOL", "abc", "abc", "read()", byteByByteReader),
            Arguments.of("No EOL", "abc", "abc", "read(byte[])", byteArrayReader),
            Arguments.of("No EOL", "abc", "abc", "read(byte[], int, int)", byteArrayRangeReader),

            Arguments.of("Windows EOL", "ab\r\nc", "ab\r\nc", "read()", byteByByteReader),
            Arguments.of("Windows EOL", "ab\r\nc", "ab\r\nc", "read(byte[])", byteArrayReader),
            Arguments.of("Windows EOL", "ab\r\nc", "ab\r\nc", "read(byte[], int, int)", byteArrayRangeReader),

            Arguments.of("Empty String", "", "", "read()", byteByByteReader),
            Arguments.of("Empty String", "", "", "read(byte[])", byteArrayReader),
            Arguments.of("Empty String", "", "", "read(byte[], int, int)", byteArrayRangeReader)
        );
    }

    @DisplayName("Should convert various line endings to Windows format (CRLF)")
    @ParameterizedTest(name = "[{index}] Case: {0}, Method: {3}")
    @MethodSource("lineEndingConversionScenarios")
    void shouldConvertLineEndings(final String testName, final String input, final String expected,
        final String readerName, final TestReader reader) throws IOException {

        try (final InputStream source = CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get();
             final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(source, false)) {
            final String actual = reader.read(stream);
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("Should add CRLF at EOF if ensure=true and input does not end with EOL")
    void shouldAddCrlfAtEofWhenEnsureIsTrue() throws IOException {
        final String input = "abc";
        final String expected = "abc\r\n";

        try (final InputStream source = CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get();
             final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(source, true)) {
            final String actual = readIntoByteArray(stream); // Using one reader is sufficient for this test
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("Should not add extra CRLF at EOF if ensure=true and input already ends with LF")
    void shouldNotAddExtraCrlfWhenInputEndsWithLfAndEnsureIsTrue() throws IOException {
        final String input = "abc\n";
        final String expected = "abc\r\n"; // \n is converted, but no extra CRLF is added

        try (final InputStream source = CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get();
             final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(source, true)) {
            final String actual = readIntoByteArray(stream);
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("mark() should not be supported")
    void markShouldThrowUnsupportedOperationException() {
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), true)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @Test
    @DisplayName("markSupported() should return false")
    void markSupportedShouldReturnFalse() {
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), false)) {
            assertFalse(stream.markSupported());
        }
    }
}