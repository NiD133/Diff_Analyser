package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 * This test focuses on the behavior of the reader after it has been closed.
 */
public class SequenceReaderTest {

    /**
     * Verifies that calling read() on a closed SequenceReader returns EOF,
     * as is standard for the Reader contract.
     */
    @Test
    void read_onClosedReader_shouldReturnEof() throws IOException {
        // Arrange
        // The reader is not in a try-with-resources block because we need to
        // test its behavior *after* it has been explicitly closed.
        @SuppressWarnings("resource")
        final Reader sequenceReader = new SequenceReader(new CharSequenceReader("Some data"));

        // Act
        sequenceReader.close();

        // Assert
        // After a reader is closed, subsequent calls to read() should return EOF (-1).
        assertEquals(EOF, sequenceReader.read(), "First read() on a closed reader should return EOF.");
        assertEquals(EOF, sequenceReader.read(), "Subsequent read() calls on a closed reader should also return EOF.");
    }
}