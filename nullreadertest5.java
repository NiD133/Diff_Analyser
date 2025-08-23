package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link NullReader}.
 */
class NullReaderTest {

    /**
     * A specialized {@link NullReader} for testing that generates a predictable sequence
     * of characters (0, 1, 2, ...). This allows tests to verify not only the number
     * of characters read but also that the correct "data" was processed.
     */
    private static final class TestNullReader extends NullReader {

        TestNullReader(final int size) {
            super(size);
        }

        @Override
        protected int processChar() {
            // Return the character value corresponding to the current stream position.
            return (int) getPosition() - 1;
        }

        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            // Fill the destination array slice with character values corresponding to their position in the stream.
            final int startPos = (int) getPosition() - length;
            for (int i = 0; i < length; i++) {
                chars[offset + i] = (char) (startPos + i);
            }
        }
    }

    @Test
    void testRead_fillsBufferAndReadsRemainder() throws IOException {
        // Given a reader with 15 characters and a buffer of 10
        final Reader reader = new TestNullReader(15);
        final char[] buffer = new char[10];

        // When reading the first chunk
        final int charsRead1 = reader.read(buffer);

        // Then it should fill the buffer completely with the first 10 characters
        assertEquals(10, charsRead1, "First read should fill the entire buffer.");
        final char[] expected1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(expected1, buffer, "First chunk of data should be the sequence 0-9.");

        // When reading the second chunk
        final int charsRead2 = reader.read(buffer);

        // Then it should read the remaining 5 characters into the start of the buffer
        assertEquals(5, charsRead2, "Second read should consume the remaining characters.");
        // The expected buffer state after the second read, where the first 5 chars are overwritten.
        final char[] expected2 = {10, 11, 12, 13, 14, 5, 6, 7, 8, 9};
        assertArrayEquals(expected2, buffer, "Second chunk of data should be the sequence 10-14.");
    }

    @Test
    void testRead_atEndOfFile_returnsNegativeOne() throws IOException {
        // Given a reader that has been fully read
        final Reader reader = new TestNullReader(5);
        reader.read(new char[10]); // Reads all 5 characters, exhausting the reader

        // When reading again
        final int eof = reader.read(new char[10]);

        // Then it should return -1 to signal the end of the file
        assertEquals(-1, eof, "Reading at EOF should return -1.");
    }

    @Test
    void testRead_afterEndOfFile_throwsIOException() throws IOException {
        // Given a reader that is past the end of the file
        final Reader reader = new TestNullReader(5);
        reader.read(new char[10]); // Exhaust the reader
        reader.read(new char[10]); // Trigger EOF state by reading again

        // When trying to read again
        // Then an IOException should be thrown
        final IOException e = assertThrows(IOException.class, () -> reader.read(new char[10]));
        assertEquals("Read after end of file", e.getMessage(), "Exception message should indicate reading after EOF.");
    }

    @Test
    void testClose_resetsReader() throws IOException {
        // Given a reader that has been partially read
        final Reader reader = new TestNullReader(15);
        reader.read(new char[5]); // Advance position to 5

        // When the reader is closed (which resets its position)
        reader.close();

        // Then a subsequent read should start from the beginning
        final char[] buffer = new char[10];
        final int offset = 2;
        final int length = 4;
        final int charsRead = reader.read(buffer, offset, length);

        // And it should read the requested number of characters into the correct slice
        assertEquals(length, charsRead, "Should read the specified number of characters after reset.");

        // And the buffer should contain the new data only in the specified slice
        final char[] expected = new char[10]; // All elements are '\0' by default
        expected[2] = 0;
        expected[3] = 1;
        expected[4] = 2;
        expected[5] = 3;
        assertArrayEquals(expected, buffer, "Buffer should contain [0, 1, 2, 3] at offset 2.");
    }
}