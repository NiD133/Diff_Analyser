package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link NullReader} class, focusing on the read() and close() behavior.
 */
public class NullReaderTest {

    /**
     * A test-specific implementation of {@link NullReader} that returns a sequence of
     * integers (0, 1, 2, ...) instead of the default zero. This makes it easier to
     * verify the reader's behavior.
     */
    private static final class TestNullReader extends NullReader {

        TestNullReader(final int size) {
            super(size);
        }

        /**
         * Returns a character corresponding to the read position for the {@code read()} method.
         * The position is incremented before this method is called, so we subtract 1.
         * For a read at position 0, getPosition() will be 1, and this returns 0.
         */
        @Override
        protected int processChar() {
            return (int) getPosition() - 1;
        }

        /**
         * Fills the buffer with characters corresponding to the read position for the
         * {@code read(char[], int, int)} method.
         */
        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            final int startPos = (int) getPosition() - length;
            for (int i = 0; i < length; i++) {
                chars[offset + i] = (char) (startPos + i);
            }
        }
    }

    @Test
    @DisplayName("read() should return sequenced characters until the reader is exhausted")
    void readShouldReturnSequencedCharactersUntilEnd() throws IOException {
        // Arrange
        final int size = 5;
        final Reader reader = new TestNullReader(size);

        // Act & Assert
        for (int i = 0; i < size; i++) {
            final int expectedChar = i;
            assertEquals(expectedChar, reader.read(), "Character at position " + i + " should match");
        }
    }

    @Test
    @DisplayName("read() should return -1 (EOF) when the end of the stream is reached")
    void readAtEndOfStreamShouldReturnEOF() throws IOException {
        // Arrange
        final int size = 5;
        final Reader reader = new TestNullReader(size);
        reader.skip(size); // Consume all characters to reach the end

        // Act
        final int result = reader.read();

        // Assert
        assertEquals(-1, result, "Reading at the end of the stream should return -1");
    }

    @Test
    @DisplayName("read() should throw IOException when attempting to read past the end of the stream")
    void readPastEndOfStreamShouldThrowIOException() throws IOException {
        // Arrange
        final int size = 5;
        final Reader reader = new TestNullReader(size);
        reader.skip(size); // Move to the end of the stream
        assertEquals(-1, reader.read(), "The first read at EOF should return -1");

        // Act & Assert
        final IOException e = assertThrows(IOException.class, reader::read,
                "A second read at EOF should throw an exception");
        assertEquals("Read after end of file", e.getMessage(), "The exception message should indicate reading past EOF");
    }

    @Test
    @DisplayName("close() should reset the reader's position to the beginning")
    void closeShouldResetReaderPosition() throws IOException {
        // Arrange
        final int size = 5;
        final NullReader reader = new TestNullReader(size);
        reader.skip(size);
        assertEquals(size, reader.getPosition(), "Position should be at the end before closing");

        // Act
        reader.close();

        // Assert
        assertEquals(0, reader.getPosition(), "Position should be reset to 0 after closing");
    }
}