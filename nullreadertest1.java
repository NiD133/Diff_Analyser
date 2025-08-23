package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link NullReader}.
 */
public class NullReaderTest {

    /**
     * A test-specific subclass of {@link NullReader} that returns predictable,
     * sequential character values (0, 1, 2, ...). This allows for verifying that
     * the reader's position is advancing correctly.
     */
    private static final class TestNullReader extends NullReader {

        /**
         * Constructs a TestNullReader.
         *
         * @param size The size of the reader to emulate.
         * @param throwEofException Whether to throw an EOFException at the end of the stream.
         */
        TestNullReader(final int size, final boolean throwEofException) {
            // For this test's purpose, mark/reset support is not needed.
            super(size, false, throwEofException);
        }

        /**
         * Returns a value based on the reader's position to produce a predictable sequence.
         * The first read returns 0, the second 1, and so on.
         */
        @Override
        protected int processChar() {
            return (int) getPosition() - 1;
        }

        /**
         * Fills the buffer with a predictable sequence of characters.
         * (Corrected from the original for robustness).
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
    void readShouldThrowEOFExceptionWhenConfiguredAndPastEndOfStream() throws IOException {
        // Arrange: Create a reader with a size of 2, configured to throw an EOFException.
        try (Reader reader = new TestNullReader(2, true)) {

            // Act & Assert: Verify that the first two reads succeed and return expected values.
            assertEquals(0, reader.read(), "First read should return the first character (0).");
            assertEquals(1, reader.read(), "Second read should return the second character (1).");

            // Assert: A third read, which is past the end of the stream, must throw an EOFException.
            assertThrows(EOFException.class, reader::read,
                "Should throw EOFException when reading past the configured size.");
        }
    }
}