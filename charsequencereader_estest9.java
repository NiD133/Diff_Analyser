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
    public void skip_onClosedReader_throwsIOException() throws IOException {
        // Arrange: Create a reader and then immediately close it.
        // The actual content of the sequence is not important for this test.
        CharSequenceReader reader = new CharSequenceReader("test-data");
        reader.close();

        // Act & Assert: Verify that skipping on a closed reader throws an IOException.
        try {
            reader.skip(10L);
            fail("Expected an IOException to be thrown when skipping on a closed reader.");
        } catch (IOException e) {
            // Assert that the exception has the expected message.
            assertEquals("Reader closed", e.getMessage());
        }
    }
}