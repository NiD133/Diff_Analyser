package com.google.common.io;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that ready() returns true for an open reader, even if the
     * underlying sequence is empty. The method's contract is to return true
     * as long as the reader has not been closed.
     */
    @Test
    public void ready_whenReaderIsOpen_shouldReturnTrue() throws IOException {
        // Arrange: Create a reader with an empty CharSequence.
        // Using an empty string is a simple way to test this state.
        CharSequenceReader reader = new CharSequenceReader("");

        // Act & Assert: An open reader should always report itself as ready.
        assertTrue("An open CharSequenceReader should be ready.", reader.ready());
    }
}