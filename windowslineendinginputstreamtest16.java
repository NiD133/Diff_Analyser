package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Asserts that all read methods of the stream produce the expected output string.
     * This ensures consistent behavior across read(), read(byte[]), and read(byte[], int, int).
     *
     * @param input The string to be processed by the stream.
     * @param ensureLineFeedAtEos The flag for the stream's constructor.
     * @param expected The expected output string after processing.
     * @throws IOException If an I/O error occurs.
     */
    private void assertAllReadMethodsProduceExpectedOutput(final String input, final boolean ensureLineFeedAtEos, final String expected) throws IOException {
        assertEquals(expected, processStringWithSingleByteRead(input, ensureLineFeedAtEos), "Mismatch for read()");
        assertEquals(expected, processStringWithByteArrayRead(input, ensureLineFeedAtEos), "Mismatch for read(byte[])");
        assertEquals(expected, processStringWithByteArrayReadWithOffset(input, ensureLineFeedAtEos), "Mismatch for read(byte[], int, int)");
    }

    /**
     * Helper to test the stream by reading it byte by byte using the read() method.
     */
    private String processStringWithSingleByteRead(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createInputStream(input, ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[1024];
            int i = 0;
            int b;
            while ((b = stream.read()) != -1) {
                buffer[i++] = (byte) b;
            }
            return new String(buffer, 0, i, StandardCharsets.UTF_8);
        }
    }

    /**
     * Helper to test the stream by reading into a byte array using the read(byte[]) method.
     */
    private String processStringWithByteArrayRead(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createInputStream(input, ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[1024];
            final int bytesRead = stream.read(buffer);
            return bytesRead > 0 ? new String(buffer, 0, bytesRead, StandardCharsets.UTF_8) : "";
        }
    }

    /**
     * Helper to test the stream by reading into a byte array with an offset using the read(byte[], int, int) method.
     */
    private String processStringWithByteArrayReadWithOffset(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createInputStream(input, ensureLineFeedAtEos)) {
            final byte[] buffer = new byte[1024];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            return bytesRead > 0 ? new String(buffer, 0, bytesRead, StandardCharsets.UTF_8) : "";
        }
    }

    private InputStream createInputStream(final String data, final boolean ensureLineFeedAtEos) {
        final InputStream target = CharSequenceInputStream.builder()
            .setCharSequence(data)
            .setCharset(StandardCharsets.UTF_8)
            .get();
        return new WindowsLineEndingInputStream(target, ensureLineFeedAtEos);
    }

    @Test
    void markSupportedShouldReturnFalse() {
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), true)) {
            assertFalse(stream.markSupported());
        }
    }

    @Test
    void markShouldThrowUnsupportedOperationException() {
        try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), true)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @Test
    @DisplayName("Should not add CRLF to an empty string if not ensured")
    void emptyStringWhenNotEnsured() throws IOException {
        assertAllReadMethodsProduceExpectedOutput("", false, "");
    }

    @Test
    @DisplayName("Should add CRLF to an empty string if ensured")
    void emptyStringWhenEnsured() throws IOException {
        assertAllReadMethodsProduceExpectedOutput("", true, "\r\n");
    }

    @Test
    @DisplayName("Should not add CRLF to a simple string if not ensured")
    void simpleStringWhenNotEnsured() throws IOException {
        assertAllReadMethodsProduceExpectedOutput("abc", false, "abc");
    }

    @Test
    @DisplayName("Should add CRLF to a simple string if ensured")
    void simpleStringWhenEnsured() throws IOException {
        assertAllReadMethodsProduceExpectedOutput("abc", true, "abc\r\n");
    }

    @Test
    @DisplayName("Should replace a lone CR with CRLF")
    void shouldReplaceLoneCrWithCrLf() throws IOException {
        assertAllReadMethodsProduceExpectedOutput("a\rb\rc", false, "a\r\nb\r\nc");
    }

    @Test
    @DisplayName("Should replace a lone LF with CRLF")
    void shouldReplaceLoneLfWithCrLf() throws IOException {
        assertAllReadMethodsProduceExpectedOutput("a\nb\nc", false, "a\r\nb\r\nc");
    }

    @Test
    @DisplayName("Should not modify an existing CRLF")
    void shouldNotModifyExistingCrLf() throws IOException {
        assertAllReadMethodsProduceExpectedOutput("a\r\nb\r\nc", false, "a\r\nb\r\nc");
    }

    @Test
    @DisplayName("Should handle a string ending with a lone CR")
    void shouldHandleStringEndingWithCr() throws IOException {
        // A lone CR at EOF is always converted to CRLF, regardless of the 'ensure' flag.
        assertAllReadMethodsProduceExpectedOutput("abc\r", false, "abc\r\n");
        assertAllReadMethodsProduceExpectedOutput("abc\r", true, "abc\r\n");
    }

    @Test
    @DisplayName("Should handle a string ending with a lone LF")
    void shouldHandleStringEndingWithLf() throws IOException {
        // A lone LF at EOF is always converted to CRLF, regardless of the 'ensure' flag.
        assertAllReadMethodsProduceExpectedOutput("abc\n", false, "abc\r\n");
        assertAllReadMethodsProduceExpectedOutput("abc\n", true, "abc\r\n");
    }

    @Test
    @DisplayName("Should not add an extra CRLF to a string already ending with CRLF")
    void shouldNotAddExtraCrLfWhenEndingWithCrLf() throws IOException {
        // If the string already ends with a proper CRLF, the 'ensure' flag should not add another one.
        assertAllReadMethodsProduceExpectedOutput("abc\r\n", false, "abc\r\n");
        assertAllReadMethodsProduceExpectedOutput("abc\r\n", true, "abc\r\n");
    }
}