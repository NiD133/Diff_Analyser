package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Writer;
import org.junit.Test;

/**
 * This test class contains tests for the {@link CharStreams} utility class.
 * This specific test focuses on the behavior of the copy() method with unconnected streams.
 */
public class CharStreams_ESTestTest28 { // Note: Test class name kept for consistency.

    /**
     * Verifies that CharStreams.copy() throws an IOException when trying to read
     * from a PipedReader that has not been connected to a PipedWriter.
     */
    @Test
    public void copy_fromUnconnectedPipedReader_throwsIOException() {
        // Arrange: Create a writer to copy to and a PipedReader that is not
        // connected to any PipedWriter.
        Writer nullWriter = CharStreams.nullWriter();
        PipedReader unconnectedReader = new PipedReader();

        try {
            // Act: Attempt to copy from the unconnected reader.
            CharStreams.copy(unconnectedReader, nullWriter);
            fail("Expected an IOException to be thrown for an unconnected PipedReader.");
        } catch (IOException expected) {
            // Assert: Verify that the correct IOException was thrown.
            assertEquals("Pipe not connected", expected.getMessage());
        }
    }
}