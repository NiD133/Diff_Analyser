package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader} focusing on the constructor that accepts an Iterable.
 */
public class SequenceReaderTest {

    /**
     * Tests that the SequenceReader correctly reads from a sequence of readers
     * provided as an Iterable. The test ensures that characters are read in the
     * correct order from each reader and that the end of the stream is correctly
     * reported once all readers are exhausted.
     */
    @Test
    void shouldReadFromIterableOfReadersInSequence() throws IOException {
        // Arrange: Create an iterable of readers with distinct, single-character content.
        final List<Reader> readers = Arrays.asList(
            new StringReader("F"),
            new StringReader("B")
        );

        // Act & Assert: Use a try-with-resources block to ensure the reader is properly closed.
        try (Reader sequenceReader = new SequenceReader(readers)) {
            // Assert that characters are read sequentially from the provided readers.
            assertEquals('F', sequenceReader.read(), "Should read the character from the first reader.");
            assertEquals('B', sequenceReader.read(), "Should read the character from the second reader.");

            // Assert that the end of the stream is reached after all readers are exhausted.
            assertEquals(EOF, sequenceReader.read(), "Should return EOF after all readers have been read.");
        }
    }
}