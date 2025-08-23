package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Tests that {@link CharStreams#copyReaderToWriter(Reader, Writer)} propagates an IOException
     * when the target writer is in an invalid state (e.g., an unconnected pipe).
     */
    @Test
    public void copy_toUnconnectedWriter_throwsIOException() {
        // Arrange: Create a reader with some data and a PipedWriter that is not connected.
        Reader reader = new CharArrayReader(new char[]{'a', 'b', 'c'});
        Writer unconnectedWriter = new PipedWriter();

        // Act & Assert: Attempting to copy to the unconnected writer should throw an IOException.
        try {
            CharStreams.copyReaderToWriter(reader, unconnectedWriter);
            fail("Expected an IOException to be thrown when writing to an unconnected PipedWriter.");
        } catch (IOException expected) {
            // Verify that the exception is the one thrown by the underlying writer.
            assertEquals("Pipe not connected", expected.getMessage());
        }
    }
}