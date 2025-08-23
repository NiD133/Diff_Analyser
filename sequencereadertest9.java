package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
class SequenceReaderTest {

    /**
     * Verifies that a reader has reached the end of the stream (EOF) and
     * consistently returns EOF on subsequent reads.
     *
     * @param reader The reader to check.
     * @throws IOException If an I/O error occurs.
     */
    private void assertReaderIsAtEof(final Reader reader) throws IOException {
        final int eof = -1;
        assertEquals(eof, reader.read(), "Reader should be at the end of the stream.");
        // Check a second time to ensure it stays at EOF
        assertEquals(eof, reader.read(), "Reader should remain at the end of the stream.");
    }

    @Test
    @DisplayName("Reads from a collection of readers sequentially until all are exhausted")
    void read_withCollectionOfReaders_readsAllInSequence() throws IOException {
        // Arrange: Create a collection of two readers with single-character content.
        final Collection<Reader> readers = Arrays.asList(
            new StringReader("A"),
            new StringReader("B")
        );

        // Act & Assert: Use a try-with-resources to ensure the SequenceReader is closed.
        try (final Reader sequenceReader = new SequenceReader(readers)) {
            // Assert that characters are read in the correct order from the underlying readers.
            assertEquals('A', sequenceReader.read(), "Should read the character from the first reader.");
            assertEquals('B', sequenceReader.read(), "Should read the character from the second reader.");

            // Assert that the end of the stream is reached after all readers are consumed.
            assertReaderIsAtEof(sequenceReader);
        }
    }
}