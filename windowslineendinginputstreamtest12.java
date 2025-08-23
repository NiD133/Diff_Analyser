package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
@DisplayName("WindowsLineEndingInputStream")
class WindowsLineEndingInputStreamTest {

    /**
     * Creates a WindowsLineEndingInputStream with the given configuration.
     * This helper centralizes stream creation.
     */
    private WindowsLineEndingInputStream createInputStream(final String data, final boolean ensureLineFeedAtEos) {
        return new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder()
                .setCharSequence(data)
                .setCharset(StandardCharsets.UTF_8)
                .get(),
            ensureLineFeedAtEos);
    }

    /**
     * Reads the entire stream using the single-byte read() method and returns the content as a String.
     * This simulates a specific client usage pattern.
     */
    private String readAllWithSingleByteRead(final String data, final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream stream = createInputStream(data, ensure)) {
            final byte[] buffer = new byte[100];
            int i = 0;
            while (i < buffer.length) {
                final int b = stream.read();
                if (b == -1) {
                    break;
                }
                buffer[i++] = (byte) b;
            }
            return new String(buffer, 0, i, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the stream into a byte array and returns the content as a String.
     * This simulates reading into a pre-allocated buffer.
     */
    private String readAllWithByteArrayRead(final String data, final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream stream = createInputStream(data, ensure)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    /**
     * Reads the stream into a byte array with offset and length, and returns the content as a String.
     */
    private String readAllWithByteArrayIndexRead(final String data, final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream stream = createInputStream(data, ensure)) {
            final byte[] buffer = new byte[100];
            final int bytesRead = stream.read(buffer, 0, buffer.length);
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    @Nested
    @DisplayName("Mark and Reset Support")
    class MarkResetTests {

        @DisplayName("markSupported() should always return false")
        @ParameterizedTest(name = "when ensureLineFeedAtEndOfFile is {0}")
        @ValueSource(booleans = {false, true})
        void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEndOfFile) {
            try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile)) {
                assertFalse(stream.markSupported());
            }
        }

        @DisplayName("mark() should throw UnsupportedOperationException")
        @ParameterizedTest(name = "when ensureLineFeedAtEndOfFile is {0}")
        @ValueSource(booleans = {false, true})
        void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEndOfFile) {
            try (InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEndOfFile)) {
                assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
            }
        }
    }

    @Nested
    @DisplayName("End-of-File Line Ending")
    class EofHandlingTests {

        private static final String INPUT_WITHOUT_TRAILING_LF = "a\r\n\r\nbc";
        private static final String EXPECTED_WITH_TRAILING_CRLF = "a\r\n\r\nbc\r\n";

        @Test
        @DisplayName("should add CRLF at EOF with single-byte read() when ensure=true")
        void ensureEofLfWithSingleByteRead() throws Exception {
            // Act
            final String result = readAllWithSingleByteRead(INPUT_WITHOUT_TRAILING_LF, true);
            // Assert
            assertEquals(EXPECTED_WITH_TRAILING_CRLF, result);
        }

        @Test
        @DisplayName("should add CRLF at EOF with read(byte[]) when ensure=true")
        void ensureEofLfWithByteArrayRead() throws Exception {
            // Act
            final String result = readAllWithByteArrayRead(INPUT_WITHOUT_TRAILING_LF, true);
            // Assert
            assertEquals(EXPECTED_WITH_TRAILING_CRLF, result);
        }

        @Test
        @DisplayName("should add CRLF at EOF with read(byte[], int, int) when ensure=true")
        void ensureEofLfWithByteArrayIndexRead() throws Exception {
            // Act
            final String result = readAllWithByteArrayIndexRead(INPUT_WITHOUT_TRAILING_LF, true);
            // Assert
            assertEquals(EXPECTED_WITH_TRAILING_CRLF, result);
        }
    }
}