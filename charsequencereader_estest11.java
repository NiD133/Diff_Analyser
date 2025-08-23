package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that calling ready() on a closed reader throws an IOException.
     */
    @Test
    public void ready_afterClose_throwsIOException() throws IOException {
        // Arrange: Create a reader and close it.
        // The content of the sequence is irrelevant for this test.
        CharSequenceReader reader = new CharSequenceReader("test-data");
        reader.close();

        // Act & Assert: Attempting to check if the closed reader is ready should fail.
        try {
            reader.ready();
            fail("Expected an IOException to be thrown because the reader is closed.");
        } catch (IOException expected) {
            // The contract of a closed reader is to throw an exception with a clear message.
            assertEquals("Reader closed", expected.getMessage());
        }
    }
}