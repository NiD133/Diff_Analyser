package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams#skipFully(java.io.Reader, long)}.
 */
public class CharStreamsTest {

    /**
     * Verifies that skipFully propagates an IOException thrown by the underlying reader.
     * This is tested by attempting to skip characters from a PipedReader that has not been
     * connected to a PipedWriter.
     */
    @Test
    public void skipFully_onUnconnectedReader_throwsIOException() {
        // Arrange: Create a PipedReader that is not connected to a PipedWriter.
        // Any attempt to read from it will throw an IOException.
        PipedReader unconnectedReader = new PipedReader();
        final long charsToSkip = 100L;

        // Act & Assert
        try {
            CharStreams.skipFully(unconnectedReader, charsToSkip);
            fail("Expected an IOException to be thrown when skipping on an unconnected reader.");
        } catch (IOException expected) {
            // This is the expected behavior from PipedReader. We assert the message
            // to confirm that the correct exception was propagated.
            assertEquals("Pipe not connected", expected.getMessage());
        }
    }
}