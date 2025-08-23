package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that attempting to read from a reader that has been closed
     * throws an IOException.
     */
    @Test
    public void read_afterClose_throwsIOException() throws IOException {
        // Arrange: Create a reader and then close it.
        String sequence = "test-data";
        CharSequenceReader reader = new CharSequenceReader(sequence);
        reader.close();

        CharBuffer targetBuffer = CharBuffer.allocate(sequence.length());

        // Act & Assert: Attempting to read should fail with an IOException.
        try {
            reader.read(targetBuffer);
            fail("Expected an IOException to be thrown when reading from a closed reader.");
        } catch (IOException expected) {
            assertEquals("reader closed", expected.getMessage());
        }
    }
}