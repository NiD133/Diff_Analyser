package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 * This class verifies that the stream correctly converts different line endings
 * to the Windows CRLF format (\r\n).
 */
public class WindowsLineEndingInputStreamTest {

    // --- Helper Methods for Reading from the Stream ---

    /**
     * Reads the entire content from a {@link WindowsLineEndingInputStream} by calling the single-byte {@code read()} method.
     *
     * @param input The raw string to be processed by the stream.
     * @param ensureLineFeedAtEos Whether to ensure the stream ends with a CRLF.
     * @return The processed string content.
     */
    private String readAllByteByByte(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createStream(input, ensureLineFeedAtEos);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            int ch;
            while ((ch = stream.read()) != -1) {
                output.write(ch);
            }
            return output.toString(StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the content from a {@link WindowsLineEndingInputStream} by calling the {@code read(byte[])} method.
     *
     * @param input The raw string to be processed by the stream.
     * @param ensureLineFeedAtEos Whether to ensure the stream ends with a CRLF.
     * @return The processed string content.
     */
    private String readAllByByteArray(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createStream(input, ensureLineFeedAtEos)) {
            // A buffer larger than any test input is sufficient here.
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer);
            return bytesRead == -1 ? "" : new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the content from a {@link WindowsLineEndingInputStream} by calling the {@code read(byte[], int, int)} method.
     *
     * @param input The raw string to be processed by the stream.
     * @param ensureLineFeedAtEos Whether to ensure the stream ends with a CRLF.
     * @return The processed string content.
     */
    private String readAllByByteArrayWithOffset(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createStream(input, ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            return bytesRead == -1 ? "" : new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Factory method to create a {@link WindowsLineEndingInputStream} from a String.
     */
    private WindowsLineEndingInputStream createStream(final String input, final boolean ensureLineFeedAtEos) {
        final InputStream source = CharSequenceInputStream.builder()
            .setCharSequence(input)
            .setCharset(StandardCharsets.UTF_8)
            .get();
        return new WindowsLineEndingInputStream(source, ensureLineFeedAtEos);
    }

    // --- Test Cases ---

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("mark() should not be supported")
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("markSupported() should always return false")
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        // Use try-with-resources to adhere to best practices and avoid resource leak warnings.
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported());
        }
    }

    @ParameterizedTest(name = "Input: \"{0}\", ensureEOL: {2} -> Expected: \"{1}\"")
    @CsvSource({
        // Standard conversions from LF and CR to CRLF
        "'a\\nb',    'a\\r\\nb',   true",
        "'a\\nb',    'a\\r\\nb',   false",
        "'a\\rb',    'a\\r\\nb',   true",
        "'a\\rb',    'a\\r\\nb',   false",
        // Existing CRLF should remain unchanged
        "'a\\r\\nb', 'a\\r\\nb',   true",
        "'a\\r\\nb', 'a\\r\\nb',   false",
        // Test `ensureLineFeedAtEos` flag when input does not end with a line ending
        "'a',        'a\\r\\n',    true",
        "'a',        'a',         false",
        // Test `ensureLineFeedAtEos` flag when input already ends with a line ending
        "'a\\n',     'a\\r\\n',    true",
        "'a\\n',     'a\\r\\n',    false",
        "'a\\r',     'a\\r\\n',    true",
        "'a\\r',     'a\\r\\n',    false",
        "'a\\r\\n',  'a\\r\\n',    true",
        "'a\\r\\n',  'a\\r\\n',    false",
        // Original test case: multiple CRLF endings
        "'a\\r\\n\\r\\n', 'a\\r\\n\\r\\n', true",
        // Empty input
        "'' ,        '\\r\\n',     true",
        "'' ,        '',          false"
    })
    @DisplayName("Should correctly convert various line endings to Windows format (CRLF)")
    void shouldConvertLineEndingsToWindowsFormat(String input, String expected, boolean ensureLineFeedAtEos) throws IOException {
        // CsvSource provides literal backslashes, so we need to unescape them for the test.
        final String unescapedInput = input.replace("\\n", "\n").replace("\\r", "\r");
        final String unescapedExpected = expected.replace("\\n", "\n").replace("\\r", "\r");

        // Verify that all three read() method overloads produce the same, correct result.
        assertEquals(unescapedExpected, readAllByteByByte(unescapedInput, ensureLineFeedAtEos), "Mismatch when reading byte-by-byte");
        assertEquals(unescapedExpected, readAllByByteArray(unescapedInput, ensureLineFeedAtEos), "Mismatch when reading into a byte array");
        assertEquals(unescapedExpected, readAllByByteArrayWithOffset(unescapedInput, ensureLineFeedAtEos), "Mismatch when reading into a byte array with offset");
    }
}