package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
class WindowsLineEndingInputStreamTest {

    /**
     * Reads all bytes from a WindowsLineEndingInputStream and returns them as a UTF-8 string.
     * This helper method simulates a full read of the transformed stream.
     *
     * @param input The original string to be read.
     * @param ensureLineFeedAtEos Corresponds to the constructor parameter of WindowsLineEndingInputStream.
     * @return The transformed string after processing.
     * @throws IOException If an I/O error occurs.
     */
    private String readAllAsString(final String input, final boolean ensureLineFeedAtEos) throws IOException {
        try (final InputStream original = CharSequenceInputStream.builder().setCharSequence(input).setCharset(StandardCharsets.UTF_8).get();
             final WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(original, ensureLineFeedAtEos)) {
            final byte[] resultBytes = IOUtils.toByteArray(windowsStream);
            return new String(resultBytes, StandardCharsets.UTF_8);
        }
    }

    @DisplayName("Stream should correctly transform various line endings to CRLF")
    @ParameterizedTest(name = "Input: \"{0}\", ensureLineFeedAtEos: {1}, Expected: \"{2}\"")
    // @formatter:off
    @CsvSource({
        // input                , ensureLineFeedAtEos, expectedOutput
        "''                     , false              , ''",
        "''                     , true               , '\r\n'",
        "'abc'                  , false              , 'abc'",
        "'abc'                  , true               , 'abc\r\n'",
        "'a\nbc'                , false              , 'a\r\nbc'",
        "'a\nbc'                , true               , 'a\r\nbc\r\n'",
        "'a\rbc'                , false              , 'a\r\nbc'",
        "'a\rbc'                , true               , 'a\r\nbc\r\n'",
        "'a\r\nbc'              , false              , 'a\r\nbc'",
        "'a\r\nbc'              , true               , 'a\r\nbc\r\n'",
        "'abc\n'                , false              , 'abc\r\n'",
        "'abc\n'                , true               , 'abc\r\n'",
        "'abc\r'                , false              , 'abc\r\n'",
        "'abc\r'                , true               , 'abc\r\n'",
        "'abc\r\n'              , false              , 'abc\r\n'",
        "'abc\r\n'              , true               , 'abc\r\n'",
        "'a\r\n\r\nbc'          , false              , 'a\r\n\r\nbc'",
        "'a\r\n\r\nbc'          , true               , 'a\r\n\r\nbc\r\n'"
    })
    // @formatter:on
    void streamShouldCorrectlyTransformLineEndings(final String input, final boolean ensureLineFeedAtEos, final String expectedOutput) throws IOException {
        // Act
        final String actualOutput = readAllAsString(input, ensureLineFeedAtEos);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void mark_ShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) {
        // Arrange
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void markSupported_ShouldReturnFalse(final boolean ensureLineFeedAtEos) {
        // Arrange
        try (final WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensureLineFeedAtEos)) {
            // Act & Assert
            assertFalse(stream.markSupported());
        }
    }
}