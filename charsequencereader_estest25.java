package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that calling read() on a reader with an empty CharSequence
     * immediately returns -1, the standard indicator for the end of a stream.
     */
    @Test
    public void read_onEmptySequence_returnsEndOfStream() throws IOException {
        // Arrange: Create a reader for an empty CharSequence.
        // An empty string is a simple and clear way to represent this.
        CharSequenceReader reader = new CharSequenceReader("");

        // Act: Attempt to read a single character from the reader.
        int characterRead = reader.read();

        // Assert: The result should be -1, signifying the end of the stream.
        assertEquals(-1, characterRead);
    }
}