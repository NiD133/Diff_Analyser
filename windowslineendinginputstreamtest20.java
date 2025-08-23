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
 *
 * <p>This test suite focuses on verifying the stream's core functionality,
 * such as line ending conversion and contract adherence for unsupported operations.
 * </p>
 */
class WindowsLineEndingInputStreamTest {

    @Test
    @DisplayName("read() should preserve input that already has Windows line endings")
    void readShouldPreserveExistingWindowsLineEndings() throws IOException {
        // Arrange
        final String inputWithWindowsLineEndings = "a\r\n\r\n";
        final String expectedOutput = "a\r\n\r\n";
        final boolean ensureLineFeedAtEndOfFile = true;

        try (final InputStream underlyingStream = CharSequenceInputStream.builder()
                .setCharSequence(inputWithWindowsLineEndings)
                .setCharset(StandardCharsets.UTF_8)
                .get();
             final WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(underlyingStream, ensureLineFeedAtEndOfFile)) {

            // Act
            final byte[] resultBytes = windowsStream.readAllBytes();
            final String actualOutput = new String(resultBytes, StandardCharsets.UTF_8);

            // Assert
            assertEquals(expectedOutput, actualOutput, "The stream should not alter content that already has CRLF endings.");
        }
    }

    @Test
    @DisplayName("markSupported() should always return false")
    void markSupportedShouldReturnFalse() throws IOException {
        // Arrange
        // The 'ensureLineFeedAtEndOfFile' parameter does not affect mark/reset behavior.
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), true)) {
            // Act & Assert
            assertFalse(stream.markSupported(), "markSupported() is expected to be false.");
        }
    }

    @Test
    @DisplayName("mark() should throw UnsupportedOperationException")
    void markShouldThrowUnsupportedOperationException() throws IOException {
        // Arrange
        // The 'ensureLineFeedAtEndOfFile' parameter does not affect mark/reset behavior.
        try (final InputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), true)) {
            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1), "mark() is not supported and should throw.");
        }
    }
}