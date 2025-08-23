package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests the mark/reset functionality of NullReader.
 */
class NullReaderMarkResetTest {

    /**
     * A custom NullReader that returns the read-position as the character,
     * allowing us to verify the reader's state.
     */
    private static class TestNullReader extends NullReader {

        TestNullReader(final long size, final boolean markSupported, final boolean throwEofException) {
            super(size, markSupported, throwEofException);
        }

        /** Returns the character code corresponding to the position before this read. */
        @Override
        protected int processChar() {
            // Position is 1-based after reading, so we return position - 1.
            return (int) getPosition() - 1;
        }

        /** Fills the buffer with character codes corresponding to their stream position. */
        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            final int startPos = (int) getPosition() - length;
            for (int i = 0; i < length; i++) {
                chars[offset + i] = (char) (startPos + i);
            }
        }
    }

    @Test
    @DisplayName("mark() and reset() should throw UnsupportedOperationException when not supported")
    void markAndResetShouldThrowExceptionWhenNotSupported() {
        // Given a reader that does not support marking
        try (Reader reader = new TestNullReader(100, false, false)) {
            // Then markSupported() should be false
            assertFalse(reader.markSupported(), "markSupported() should return false");

            // And when trying to mark or reset, an exception should be thrown
            assertThrows(UnsupportedOperationException.class, () -> reader.mark(10),
                "mark() should throw UnsupportedOperationException");
            assertThrows(UnsupportedOperationException.class, reader::reset,
                "reset() should throw UnsupportedOperationException");
        }
    }

    @Nested
    @DisplayName("When mark is supported")
    class WhenMarkIsSupported {

        private static final int READER_SIZE = 100;
        private static final int READ_LIMIT = 10;
        private static final int INITIAL_READ_COUNT = 3;

        private Reader reader;

        @BeforeEach
        void setUp() {
            reader = new TestNullReader(READER_SIZE, true, false);
        }

        @Test
        @DisplayName("markSupported() should return true")
        void markSupportedShouldReturnTrue() {
            assertTrue(reader.markSupported());
        }

        @Test
        @DisplayName("reset() should throw IOException if mark() was never called")
        void resetShouldThrowExceptionWhenNotMarked() {
            final IOException e = assertThrows(IOException.class, reader::reset);
            assertEquals("No position has been marked", e.getMessage());
        }

        @Test
        @DisplayName("reset() should restore stream to marked position if within read limit")
        void resetShouldWorkCorrectlyWithinReadLimit() throws IOException {
            // Given: we read a few characters to advance the position
            for (int i = 0; i < INITIAL_READ_COUNT; i++) {
                assertEquals(i, reader.read(), "Character mismatch before marking");
            }
            final int markedPosition = INITIAL_READ_COUNT;

            // When: we mark the current position and read a few more characters
            reader.mark(READ_LIMIT);
            assertEquals(markedPosition, reader.read(), "First character after mark");
            assertEquals(markedPosition + 1, reader.read(), "Second character after mark");

            // And: we reset the reader
            reader.reset();

            // Then: reading should resume from the marked position
            assertEquals(markedPosition, reader.read(), "First character after reset should be at marked position");
        }

        @Test
        @DisplayName("reset() should throw IOException if read limit is exceeded")
        void resetShouldThrowExceptionWhenReadLimitIsExceeded() throws IOException {
            // Given: we mark the current position
            final int markedPosition = 0;
            reader.mark(READ_LIMIT);

            // When: we read past the read limit
            for (int i = 0; i < READ_LIMIT + 1; i++) {
                reader.read();
            }

            // Then: reset() should throw an IOException with a specific message
            final IOException e = assertThrows(IOException.class, reader::reset);
            final String expectedMessage = "Marked position [" + markedPosition + "] is no longer valid - passed the read limit [" + READ_LIMIT + "]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}