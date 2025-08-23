package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
class WindowsLineEndingInputStreamTest {

    /**
     * Creates a WindowsLineEndingInputStream for a given string input, reads the entire
     * stream using the read(byte[]) method, and returns the result as a string.
     *
     * @param input The string to be processed.
     * @param ensureLineFeedAtEos The flag for the WindowsLineEndingInputStream.
     * @return The transformed string after passing through the stream.
     * @throws IOException If an I/O error occurs.
     */
    private String transformWithReadByteArray(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(
                CharSequenceInputStream.builder()
                        .setCharSequence(input)
                        .setCharset(StandardCharsets.UTF_8)
                        .get(),
                ensureLineFeedAtEos)) {

            // A buffer large enough for test data to test the read(byte[]) method.
            final byte[] buffer = new byte[1024];
            final int bytesRead = windowsStream.read(buffer);
            if (bytesRead == -1) {
                return "";
            }
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1),
                "mark() should not be supported.");
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported(), "markSupported() should return false.");
        }
    }

    @Test
    void readByteArrayWithEnsureLineFeedFalseShouldPreserveExistingCrLf() throws Exception {
        // Arrange
        final String input = "a\r\n\r\n";
        final String expected = "a\r\n\r\n";

        // Act
        final String result = transformWithReadByteArray(input, false);

        // Assert
        assertEquals(expected, result, "Existing CRLF sequences should be preserved.");
    }

    @Test
    void readByteArrayWithEnsureLineFeedFalseShouldNotAddCrLfForInputWithoutLineEndings() throws Exception {
        // Arrange
        final String input = "a";
        final String expected = "a";

        // Act
        final String result = transformWithReadByteArray(input, false);

        // Assert
        assertEquals(expected, result, "Should not add a CRLF to a string that doesn't end with a line feed.");
    }
}