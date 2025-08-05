package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.JUnit5TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UTF8Writer} class.
 */
class UTF8WriterTest extends JUnit5TestBase {

    // A string with a mix of 1, 2, and 3-byte UTF-8 characters.
    private static final String MULTIBYTE_STRING = "AB\u00A0\u1AE9\uFFFC"; // A, B, NO-BREAK SPACE, KHMER VOWEL, FULLWIDTH TILDE

    // A 4-byte UTF-8 character (smiling emoji) represented as a surrogate pair.
    private static final String EMOJI_STRING = "\uD83D\uDE03";
    private static final byte[] EMOJI_UTF8_BYTES = {(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83};

    @Test
    @DisplayName("Should write string correctly using various write method overloads")
    void writeUsingVariousMethodsShouldConcatenateCorrectly() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);
        char[] contentChars = MULTIBYTE_STRING.toCharArray();
        byte[] expectedBytesPerWrite = MULTIBYTE_STRING.getBytes(StandardCharsets.UTF_8);
        String expectedFinalString = MULTIBYTE_STRING + MULTIBYTE_STRING + MULTIBYTE_STRING;

        // Act: Write the same content three times using different methods
        // 1. Write the full string
        writer.write(MULTIBYTE_STRING);

        // 2. Write character by character and as a sub-array
        writer.append(contentChars[0]);
        writer.write(contentChars[1]);
        writer.write(contentChars, 2, 3);
        writer.flush(); // Flush in the middle of operations

        // 3. Write the full string from a substring
        writer.write(MULTIBYTE_STRING, 0, MULTIBYTE_STRING.length());
        writer.close();

        // Assert
        byte[] actualBytes = out.toByteArray();
        String actualString = new String(actualBytes, StandardCharsets.UTF_8);

        assertEquals(3 * expectedBytesPerWrite.length, actualBytes.length);
        assertEquals(expectedFinalString, actualString);
    }

    @Test
    @DisplayName("Should write a mostly ASCII string correctly")
    void writeMostlyAsciiStringShouldEncodeCorrectly() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);
        String content = "abcdefghijklmnopqrst\u00A0"; // Ends with a 2-byte char
        byte[] expectedBytes = content.getBytes(StandardCharsets.UTF_8);

        // Act
        writer.write(content);
        writer.flush(); // Flush before close to exercise a different code path
        writer.close();

        // Assert
        assertArrayEquals(expectedBytes, out.toByteArray());
        assertEquals(content, utf8String(out));
    }

    @Test
    @DisplayName("Should not throw exceptions when flush() or close() are called multiple times")
    void writerMethodsAfterCloseShouldBeIdempotent() throws IOException {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);
        writer.write("XY");
        writer.close();
        assertEquals(2, out.size(), "Initial write should be successful before extra calls");

        // Act & Assert: Subsequent calls should be ignored without error
        assertDoesNotThrow(() -> {
            writer.flush();
            writer.close();
            writer.flush();
        }, "flush() and close() should be idempotent after the writer is closed");
    }

    @Test
    @DisplayName("Should correctly encode a valid surrogate pair written as individual chars")
    void writeValidSurrogatePairAsCharsShouldEncodeCorrectly() throws IOException {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);
        char highSurrogate = EMOJI_STRING.charAt(0); // 0xD83D
        char lowSurrogate = EMOJI_STRING.charAt(1);  // 0xDE03

        // Act
        writer.write(highSurrogate);
        writer.write(lowSurrogate);
        writer.close();

        // Assert
        assertArrayEquals(EMOJI_UTF8_BYTES, out.toByteArray());
    }

    @Test
    @DisplayName("Should correctly encode a valid surrogate pair written as a String")
    void writeValidSurrogatePairAsStringShouldEncodeCorrectly() throws IOException {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);

        // Act
        writer.write(EMOJI_STRING);
        writer.close();

        // Assert
        assertArrayEquals(EMOJI_UTF8_BYTES, out.toByteArray());
    }

    @ParameterizedTest(name = "[{index}] Writing {2} should fail with: {1}")
    @MethodSource("invalidSurrogateProvider")
    @DisplayName("Should throw IOException for invalid surrogate sequences")
    void writeWithInvalidSurrogateSequenceShouldThrowIOException(
            IOConsumer<UTF8Writer> writeAction, String expectedMessage, String description) {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);

        // Act & Assert
        IOException e = assertThrows(IOException.class, () -> {
            writeAction.accept(writer);
            writer.close(); // Some errors are only thrown on close
        });
        verifyException(e, expectedMessage);
    }

    private static Stream<Arguments> invalidSurrogateProvider() {
        return Stream.of(
                Arguments.of(writer -> writer.write(0xDE03), "Unmatched second part", "a lone low surrogate char"),
                Arguments.of(writer -> {
                    writer.write(0xD83D);
                    writer.write('a');
                }, "Broken surrogate pair", "a high surrogate followed by a non-surrogate"),
                Arguments.of(writer -> writer.write("\uDE03"), "Unmatched second part", "a string with a lone low surrogate"),
                Arguments.of(writer -> writer.write("\uD83Da"), "Broken surrogate pair", "a string with a high surrogate followed by a non-surrogate")
        );
    }

    // Functional interface to allow lambda expressions that throw IOException
    @FunctionalInterface
    private interface IOConsumer<T> {
        void accept(T t) throws IOException;
    }

    @Test
    @DisplayName("Internal: Surrogate pair to code point conversion formula should be correct")
    void internalSurrogateConversionFormulaShouldBeCorrect() {
        // This test verifies the correctness of the internal formula used to convert
        // a UTF-16 surrogate pair into a single Unicode code point.
        for (int high = UTF8Writer.SURR1_FIRST; high <= UTF8Writer.SURR1_LAST; high++) {
            for (int low = UTF8Writer.SURR2_FIRST; low <= UTF8Writer.SURR2_LAST; low++) {
                // Standard formula for converting a surrogate pair
                int expectedCodePoint = 0x10000 + ((high - UTF8Writer.SURR1_FIRST) << 10) + (low - UTF8Writer.SURR2_FIRST);
                // The formula used inside UTF8Writer
                int actualCodePoint = (high << 10) + low + UTF8Writer.SURROGATE_BASE;

                assertEquals(expectedCodePoint, actualCodePoint,
                        () -> String.format("Mismatch for surrogate pair [U+%X, U+%X]", high, low));
            }
        }
    }

    private IOContext _ioContext() {
        return testIOContext();
    }
}