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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    private static final String CRLF = "\r\n";

    /**
     * Creates a {@link WindowsLineEndingInputStream} from a String and reads all its content.
     * This helper uses the efficient {@code readAllBytes()} method to test the stream's transformation logic.
     *
     * @param input The string to be used as the stream's source.
     * @param ensureLineFeedAtEos The flag for the stream's constructor.
     * @return The transformed content of the stream as a String.
     */
    private String readAllWithWindowsLineEndings(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream in = CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get();
             WindowsLineEndingInputStream wlis = new WindowsLineEndingInputStream(in, ensureLineFeedAtEos)) {
            return new String(wlis.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @DisplayName("The stream should correctly convert line endings based on input and the 'ensureLineFeedAtEos' flag.")
    @ParameterizedTest(name = "Input: \"{0}\", ensureEof: {1}, Expected: \"{2}\"")
    @CsvSource({
        // Standard cases with no line endings
        "'abc',      false, 'abc'",
        "'abc',      true,  'abc" + CRLF + "'",

        // Lone LF should be converted to CRLF
        "'a\\nbc',   false, 'a" + CRLF + "bc'",
        "'a\\nbc',   true,  'a" + CRLF + "bc" + CRLF + "'",

        // Lone CR should be converted to CRLF
        "'a\\rbc',   false, 'a" + CRLF + "bc'",
        "'a\\rbc',   true,  'a" + CRLF + "bc" + CRLF + "'",

        // Existing CRLF should be preserved
        "'a\\r\\nbc', false, 'a" + CRLF + "bc'",
        "'a\\r\\nbc', true,  'a" + CRLF + "bc" + CRLF + "'",

        // Trailing line endings
        "'abc\\n',   false, 'abc" + CRLF + "'",
        "'abc\\n',   true,  'abc" + CRLF + "'", // ensureEof is redundant here
        "'abc\\r',   false, 'abc" + CRLF + "'",
        "'abc\\r',   true,  'abc" + CRLF + "'", // ensureEof is redundant here

        // Empty input
        "'' ,        false, ''",
        "'' ,        true,  '" + CRLF + "'"
    })
    void shouldCorrectlyConvertLineEndings(final String input, final boolean ensureEof, final String expected) throws IOException {
        final String actual = readAllWithWindowsLineEndings(input, ensureEof);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("All read() method overloads should produce the same result")
    void allReadMethodsShouldProduceSameResult() throws IOException {
        final String input = "a\nb\r\nc\rd";
        final boolean ensureEof = true;
        final String expected = "a" + CRLF + "b" + CRLF + "c" + CRLF + "d" + CRLF;

        // Test reading byte-by-byte
        assertEquals(expected, readUsingSingleByteRead(input, ensureEof), "Mismatch for read()");

        // Test reading into a full byte array
        assertEquals(expected, readUsingByteArray(input, ensureEof), "Mismatch for read(byte[])");

        // Test reading into a byte array with offset
        assertEquals(expected, readUsingByteArrayWithOffset(input, ensureEof), "Mismatch for read(byte[], int, int)");
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("mark() should be unsupported")
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        try (InputStream in = new NullInputStream();
             WindowsLineEndingInputStream wlis = new WindowsLineEndingInputStream(in, ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> wlis.mark(1));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("markSupported() should return false")
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        try (InputStream in = new NullInputStream();
             WindowsLineEndingInputStream wlis = new WindowsLineEndingInputStream(in, ensureLineFeedAtEos)) {
            assertFalse(wlis.markSupported());
        }
    }

    // --- Helpers for testing different read() overloads ---

    private String readUsingSingleByteRead(final String msg, final boolean ensure) throws IOException {
        try (InputStream in = CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get();
             WindowsLineEndingInputStream wlis = new WindowsLineEndingInputStream(in, ensure)) {
            final byte[] buf = new byte[100];
            int i = 0;
            while (i < buf.length) {
                final int read = wlis.read();
                if (read < 0) {
                    break;
                }
                buf[i++] = (byte) read;
            }
            return new String(buf, 0, i, StandardCharsets.UTF_8);
        }
    }

    private String readUsingByteArray(final String msg, final boolean ensure) throws IOException {
        try (InputStream in = CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get();
             WindowsLineEndingInputStream wlis = new WindowsLineEndingInputStream(in, ensure)) {
            final byte[] buf = new byte[100];
            final int read = wlis.read(buf);
            return new String(buf, 0, read, StandardCharsets.UTF_8);
        }
    }

    private String readUsingByteArrayWithOffset(final String msg, final boolean ensure) throws IOException {
        try (InputStream in = CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get();
             WindowsLineEndingInputStream wlis = new WindowsLineEndingInputStream(in, ensure)) {
            final byte[] buf = new byte[100];
            final int read = wlis.read(buf, 0, buf.length);
            return new String(buf, 0, read, StandardCharsets.UTF_8);
        }
    }
}