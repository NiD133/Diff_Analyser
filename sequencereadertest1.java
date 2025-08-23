package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
class SequenceReaderTest {

    @Test
    @DisplayName("Reading from a closed SequenceReader should consistently return EOF")
    void readAfterCloseReturnsEof() throws IOException {
        // Arrange: Create a SequenceReader with some content.
        // The try-with-resources block ensures the reader is closed at the end,
        // which also implicitly tests that closing an already-closed reader is safe.
        try (Reader sequenceReader = new SequenceReader(new CharSequenceReader("FooBar"))) {

            // Act 1: Read part of the content to advance the reader's position.
            readAndAssert(sequenceReader, "Foo");

            // Act 2: Explicitly close the reader.
            sequenceReader.close();

            // Assert: Subsequent reads should return End-Of-File (-1).
            assertEquals(EOF, sequenceReader.read(), "First read after close should be EOF.");
            assertEquals(EOF, sequenceReader.read(), "Second read after close should also be EOF to confirm state.");
        }
    }

    /**
     * Helper method to read a specific string from a Reader, asserting each character.
     *
     * @param reader The reader to read from.
     * @param expected The string that is expected to be read.
     * @throws IOException If an I/O error occurs.
     */
    private void readAndAssert(final Reader reader, final String expected) throws IOException {
        for (int i = 0; i < expected.length(); i++) {
            final char expectedChar = expected.charAt(i);
            assertEquals(expectedChar, (char) reader.read(), "Character at index " + i + " should match.");
        }
    }
}