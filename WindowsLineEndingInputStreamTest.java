package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class WindowsLineEndingInputStreamTest {

    private static final int BUFFER_SIZE = 100;
    private static final String EXPECTED_CRLF = "\r\n";
    
    /**
     * Reads the input message using single byte reads and returns the result as a string.
     */
    private String readWithSingleByte(final String message, final boolean ensureCRLF) throws IOException {
        try (WindowsLineEndingInputStream stream = createStream(message, ensureCRLF)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            while (bytesRead < buffer.length) {
                int read = stream.read();
                if (read < 0) {
                    break;
                }
                buffer[bytesRead++] = (byte) read;
            }
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the input message using byte array reads and returns the result as a string.
     */
    private String readWithByteArray(final String message, final boolean ensureCRLF) throws IOException {
        try (WindowsLineEndingInputStream stream = createStream(message, ensureCRLF)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = stream.read(buffer);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the input message using byte array reads with index and returns the result as a string.
     */
    private String readWithByteArrayIndex(final String message, final boolean ensureCRLF) throws IOException {
        try (WindowsLineEndingInputStream stream = createStream(message, ensureCRLF)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = stream.read(buffer, 0, BUFFER_SIZE);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Helper method to create a WindowsLineEndingInputStream.
     */
    private WindowsLineEndingInputStream createStream(final String message, final boolean ensureCRLF) {
        return new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder()
                        .setCharSequence(message)
                        .setCharset(StandardCharsets.UTF_8)
                        .get(),
                ensureCRLF);
    }

    @Test
    void testCRLFInTheMiddle_SingleByte() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + "bc" + EXPECTED_CRLF, readWithSingleByte("a\r\nbc", true));
    }

    @Test
    void testCRLFInTheMiddle_ByteArray() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + "bc" + EXPECTED_CRLF, readWithByteArray("a\r\nbc", true));
    }

    @Test
    void testCRLFInTheMiddle_ByteArrayIndex() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + "bc" + EXPECTED_CRLF, readWithByteArrayIndex("a\r\nbc", true));
    }

    @Test
    void testLinuxLineFeeds_SingleByte() throws Exception {
        assertEquals("ab" + EXPECTED_CRLF + "c", readWithSingleByte("ab\nc", false));
    }

    @Test
    void testLinuxLineFeeds_ByteArray() throws Exception {
        assertEquals("ab" + EXPECTED_CRLF + "c", readWithByteArray("ab\nc", false));
    }

    @Test
    void testLinuxLineFeeds_ByteArrayIndex() throws Exception {
        assertEquals("ab" + EXPECTED_CRLF + "c", readWithByteArrayIndex("ab\nc", false));
    }

    @Test
    void testMalformedLine_SingleByte() throws Exception {
        assertEquals("a\rbc", readWithSingleByte("a\rbc", false));
    }

    @Test
    void testMalformedLine_ByteArray() throws Exception {
        assertEquals("a\rbc", readWithByteArray("a\rbc", false));
    }

    @Test
    void testMalformedLine_ByteArrayIndex() throws Exception {
        assertEquals("a\rbc", readWithByteArrayIndex("a\rbc", false));
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMarkThrowsException(final boolean ensureLineFeedAtEndOfFile) {
        assertThrows(UnsupportedOperationException.class, 
            () -> new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile).mark(1));
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMarkSupportedReturnsFalse(final boolean ensureLineFeedAtEndOfFile) {
        assertFalse(new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile).markSupported());
    }

    @Test
    void testMultipleBlankLines_SingleByte() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF + "bc" + EXPECTED_CRLF, readWithSingleByte("a\r\n\r\nbc", true));
    }

    @Test
    void testMultipleBlankLines_ByteArray() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF + "bc" + EXPECTED_CRLF, readWithByteArray("a\r\n\r\nbc", true));
    }

    @Test
    void testMultipleBlankLines_ByteArrayIndex() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF + "bc" + EXPECTED_CRLF, readWithByteArrayIndex("a\r\n\r\nbc", true));
    }

    @Test
    void testRetainLineFeed_SingleByte() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF, readWithSingleByte("a\r\n\r\n", false));
        assertEquals("a", readWithSingleByte("a", false));
    }

    @Test
    void testRetainLineFeed_ByteArray() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF, readWithByteArray("a\r\n\r\n", false));
        assertEquals("a", readWithByteArray("a", false));
    }

    @Test
    void testRetainLineFeed_ByteArrayIndex() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF, readWithByteArrayIndex("a\r\n\r\n", false));
        assertEquals("a", readWithByteArrayIndex("a", false));
    }

    @Test
    void testSimpleString_SingleByte() throws Exception {
        assertEquals("abc" + EXPECTED_CRLF, readWithSingleByte("abc", true));
    }

    @Test
    void testSimpleString_ByteArray() throws Exception {
        assertEquals("abc" + EXPECTED_CRLF, readWithByteArray("abc", true));
    }

    @Test
    void testSimpleString_ByteArrayIndex() throws Exception {
        assertEquals("abc" + EXPECTED_CRLF, readWithByteArrayIndex("abc", true));
    }

    @Test
    void testTwoLinesAtEnd_SingleByte() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF, readWithSingleByte("a\r\n\r\n", true));
    }

    @Test
    void testTwoLinesAtEnd_ByteArray() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF, readWithByteArray("a\r\n\r\n", true));
    }

    @Test
    void testTwoLinesAtEnd_ByteArrayIndex() throws Exception {
        assertEquals("a" + EXPECTED_CRLF + EXPECTED_CRLF, readWithByteArrayIndex("a\r\n\r\n", true));
    }
}