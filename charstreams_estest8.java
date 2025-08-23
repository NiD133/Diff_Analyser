package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.PipedReader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    @Test
    public void toString_onUnconnectedReader_throwsIOException() {
        // Arrange: Create a PipedReader that is not connected to a PipedWriter.
        // Reading from such a reader is expected to fail.
        PipedReader unconnectedReader = new PipedReader();

        // Act & Assert: Verify that calling toString() on the unconnected reader throws an
        // IOException with a specific message.
        IOException exception = assertThrows(
            IOException.class,
            () -> CharStreams.toString(unconnectedReader)
        );

        assertEquals("Pipe not connected", exception.getMessage());
    }
}