package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 * This is a subset of tests focusing on a specific case.
 */
// The class name and inheritance are preserved from the original to avoid breaking a potential
// existing test suite structure. In a real-world scenario, these would also be cleaned up
// (e.g., to `CharStreamsTest`).
public class CharStreams_ESTestTest17 extends CharStreams_ESTest_scaffolding {

    /**
     * Verifies that readLines() throws an IOException when given a reader that is backed by
     * an unconnected PipedInputStream.
     */
    @Test
    public void readLines_fromUnconnectedPipe_throwsIOException() {
        // Arrange: Create a reader from a pipe that has no writer connected to it.
        // Any attempt to read from this pipe is expected to fail immediately.
        PipedInputStream unconnectedPipe = new PipedInputStream();
        Reader reader = new InputStreamReader(unconnectedPipe);

        // Act & Assert: Attempt to read lines and verify the expected exception is thrown.
        try {
            CharStreams.readLines(reader);
            fail("Expected an IOException to be thrown for an unconnected pipe.");
        } catch (IOException e) {
            // The underlying PipedInputStream is responsible for this specific error.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}