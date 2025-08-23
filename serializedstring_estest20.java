package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.IOException;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link SerializedString} class, focusing on its interaction
 * with output streams.
 */
public class SerializedStringTest {

    /**
     * Verifies that writeQuotedUTF8 correctly propagates an IOException when the
     * underlying OutputStream fails. This is tested using a PipedOutputStream
     * that has not been connected to a PipedInputStream.
     */
    @Test
    public void writeQuotedUTF8_shouldThrowIOException_whenWritingToUnconnectedPipe() {
        // Arrange: Create a SerializedString and an unconnected PipedOutputStream.
        // An unconnected pipe will throw an IOException upon any write attempt.
        SerializedString serializedString = new SerializedString("any string");
        PipedOutputStream unconnectedPipe = new PipedOutputStream();

        // Act & Assert: Attempting to write should fail and throw an IOException.
        try {
            serializedString.writeQuotedUTF8(unconnectedPipe);
            fail("Expected an IOException because the pipe is not connected.");
        } catch (IOException e) {
            // Verify that the caught exception is the one expected from the stream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}