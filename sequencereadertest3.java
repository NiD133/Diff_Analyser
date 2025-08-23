package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader} focusing on the close() behavior.
 */
public class SequenceReaderTest {

    /**
     * A test-purpose Reader that spies on the close() method to track if it has been called.
     * It delegates actual read operations to a standard StringReader, cleanly separating
     * the "spying" concern from the "reading" concern.
     */
    private static class CloseSpyReader extends Reader {

        private final Reader delegate;
        private boolean closed;

        /**
         * Constructs a new spy reader with the given string content.
         *
         * @param content The content the reader should provide. Use an empty string for an empty reader.
         */
        CloseSpyReader(final String content) {
            this.delegate = new StringReader(content);
            this.closed = false;
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            return delegate.read(cbuf, off, len);
        }

        @Override
        public void close() throws IOException {
            this.closed = true;
            delegate.close();
        }

        /**
         * Checks if the close() method has been invoked on this reader.
         *
         * @return {@code true} if close() has been called, {@code false} otherwise.
         */
        public boolean isClosed() {
            return closed;
        }
    }

    @Test
    void whenSequenceReaderIsClosed_thenAllConstituentReadersAreAlsoClosed() throws IOException {
        // Arrange: Create two spy readers, one with content and one that is empty.
        final CloseSpyReader readerWithContent = new CloseSpyReader("A");
        final CloseSpyReader emptyReader = new CloseSpyReader("");

        // Act: Use a try-with-resources block to ensure the SequenceReader is closed.
        // Read through the sequence to ensure both underlying readers are processed.
        try (final SequenceReader sequenceReader = new SequenceReader(readerWithContent, emptyReader)) {
            // The first read gets 'A' from the first reader.
            assertEquals('A', sequenceReader.read(), "First read should come from the first reader.");
            // The second read exhausts the first reader and moves to the second (empty) one, returning EOF.
            assertEquals(EOF, sequenceReader.read(), "Second read should be EOF as readers are exhausted.");
        }

        // Assert: Verify that closing the SequenceReader cascaded the close() call to both underlying readers.
        assertTrue(readerWithContent.isClosed(), "The first reader should have been closed.");
        assertTrue(emptyReader.isClosed(), "The second reader should have been closed.");
    }
}