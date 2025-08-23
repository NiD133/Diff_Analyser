package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void reset_onClosedReader_throwsIOException() throws IOException {
        // Arrange: Create a reader and immediately close it.
        // A simple String is a readable choice for the CharSequence.
        CharSequenceReader reader = new CharSequenceReader("test data");
        reader.close();

        // Act & Assert: Verify that calling reset() on a closed reader throws an exception.
        try {
            reader.reset();
            fail("Expected an IOException to be thrown when resetting a closed reader.");
        } catch (IOException expected) {
            // The source code for CharSequenceReader uses this specific message.
            assertEquals("reader closed", expected.getMessage());
        }
    }
}