package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
public class SequenceReaderTest {

    /**
     * Tests that a SequenceReader constructed with multiple empty readers
     * immediately returns EOF (-1) upon the first read.
     */
    @Test
    void shouldReturnEofWhenAllReadersAreEmpty() throws IOException {
        // Arrange: Create a SequenceReader with three empty StringReaders.
        // The SequenceReader should correctly handle iterating through all empty
        // readers and find the end of the sequence.
        try (final Reader sequenceReader = new SequenceReader(
                new StringReader(StringUtils.EMPTY),
                new StringReader(StringUtils.EMPTY),
                new StringReader(StringUtils.EMPTY))) {

            // Act & Assert: The first read should indicate the end of the stream.
            assertEquals(EOF, sequenceReader.read(), "A sequence of empty readers should immediately be at EOF.");
        }
    }
}