package com.google.common.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("CharSequenceReader")
class CharSequenceReaderTest {

    private static final int CHUNK_SIZE = 5;

    private static Stream<String> testSequences() {
        return Stream.of(
                "",
                "abc",
                "abcde",
                "abcdefghijkl",
                "abcdefghijklmnopqrstuvwxyz\n"
                        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r"
                        + "0123456789\r\n"
                        + "!@#$%^&*()-=_+\t[]{};':\",./<>?\\| ");
    }

    @ParameterizedTest
    @MethodSource("testSequences")
    @DisplayName("read() should read the sequence character by character")
    void read_singleChar_readsEntireSequence(String sequence) throws IOException {
        CharSequenceReader reader = new CharSequenceReader(sequence);
        for (int i = 0; i < sequence.length(); i++) {
            assertEquals(sequence.charAt(i), reader.read());
        }
        assertReaderIsExhausted(reader);
    }

    @ParameterizedTest
    @MethodSource("testSequences")
    @DisplayName("read(char[]) should read the full sequence into an exact-size buffer")
    void read_intoSizedCharArray_readsEntireSequence(String sequence) throws IOException {
        CharSequenceReader reader = new CharSequenceReader(sequence);
        char[] buffer = new char[sequence.length()];

        // For this implementation, read() on an empty sequence returns -1.
        int expectedCharsRead = sequence.isEmpty() ? -1 : sequence.length();
        int charsRead = reader.read(buffer);

        assertEquals(expectedCharsRead, charsRead);
        if (!sequence.isEmpty()) {
            assertEquals(sequence, new String(buffer));
        }
        assertReaderIsExhausted(reader);
    }

    @ParameterizedTest
    @MethodSource("testSequences")
    @DisplayName("read(char[], off, len) should read the sequence in chunks")
    void read_intoCharArrayInChunks_readsEntireSequence(String sequence) throws IOException {
        CharSequenceReader reader = new CharSequenceReader(sequence);
        StringBuilder result = new StringBuilder();
        char[] chunk = new char[CHUNK_SIZE];
        int read;
        while ((read = reader.read(chunk, 0, chunk.length)) != -1) {
            result.append(chunk, 0, read);
        }
        assertEquals(sequence, result.toString());
        assertReaderIsExhausted(reader);
    }

    @ParameterizedTest
    @MethodSource("testSequences")
    @DisplayName("read(CharBuffer) should read the full sequence into an exact-size buffer")
    void read_intoSizedCharBuffer_readsEntireSequence(String sequence) throws IOException {
        CharSequenceReader reader = new CharSequenceReader(sequence);
        CharBuffer buffer = CharBuffer.allocate(sequence.length());

        int expectedCharsRead = sequence.isEmpty() ? -1 : sequence.length();
        int charsRead = reader.read(buffer);

        assertEquals(expectedCharsRead, charsRead);
        Java8Compatibility.flip(buffer); // Prepare buffer for reading
        assertEquals(sequence, buffer.toString());
        assertReaderIsExhausted(reader);
    }

    @ParameterizedTest
    @MethodSource("testSequences")
    @DisplayName("read(CharBuffer) should read the sequence in chunks")
    void read_intoCharBufferInChunks_readsEntireSequence(String sequence) throws IOException {
        CharSequenceReader reader = new CharSequenceReader(sequence);
        StringBuilder result = new StringBuilder();
        CharBuffer chunk = CharBuffer.allocate(CHUNK_SIZE);
        while (reader.read(chunk) != -1) {
            Java8Compatibility.flip(chunk); // Prepare buffer for reading
            result.append(chunk);
            Java8Compatibility.clear(chunk); // Prepare buffer for writing
        }
        assertEquals(sequence, result.toString());
        assertReaderIsExhausted(reader);
    }

    @ParameterizedTest
    @MethodSource("testSequences")
    @DisplayName("skip() should advance the reader position correctly")
    void skip_fullLength_exhaustsReader(String sequence) throws IOException {
        CharSequenceReader reader = new CharSequenceReader(sequence);
        long skipped = reader.skip(Long.MAX_VALUE);
        assertEquals(sequence.length(), skipped);
        assertReaderIsExhausted(reader);
    }

    @Test
    @DisplayName("skip() followed by read() should return the remaining sequence")
    void skip_partial_thenReadRemaining() throws IOException {
        String sequence = "abcdefghijkl";
        int skipAmount = 5;
        CharSequenceReader reader = new CharSequenceReader(sequence);

        long skipped = reader.skip(skipAmount);
        assertEquals(skipAmount, skipped);

        char[] remaining = new char[sequence.length() - skipAmount];
        int read = reader.read(remaining);
        assertEquals(remaining.length, read);
        assertEquals(sequence.substring(skipAmount), new String(remaining));

        assertReaderIsExhausted(reader);
    }

    /**
     * Asserts that the reader is fully consumed. Any further attempts to read or skip should
     * indicate the end of the stream.
     */
    private void assertReaderIsExhausted(CharSequenceReader reader) throws IOException {
        assertEquals(-1, reader.read(), "read() should return -1 at end of stream");
        assertEquals(-1, reader.read(new char[10], 0, 10), "read(char[]) should return -1 at end of stream");
        assertEquals(-1, reader.read(CharBuffer.allocate(10)), "read(CharBuffer) should return -1 at end of stream");
        assertEquals(0, reader.skip(10), "skip() should return 0 at end of stream");
    }

    // The original test file included this helper class, so we keep it for compilation.
    private static final class Java8Compatibility {
        static void flip(CharBuffer buffer) {
            buffer.flip();
        }

        static void clear(CharBuffer buffer) {
            buffer.clear();
        }
    }
}