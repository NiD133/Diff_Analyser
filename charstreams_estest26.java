package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.Test;

/**
 * This test suite contains an improved version of a test for the CharStreams class.
 * The original test was automatically generated and lacked clarity. This version
 * focuses on being self-explanatory and robust.
 */
public class CharStreamsTest {

    /**
     * Verifies that CharStreams.copy() correctly propagates an IOException
     * thrown by the source Reader.
     */
    @Test
    public void copy_whenReaderThrowsIOException_propagatesException() {
        // Arrange: Create a custom Reader that is designed to fail by throwing
        // an IOException as soon as its read() method is called.
        final IOException simulatedException = new IOException("Simulated I/O error from source reader");
        Reader faultyReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw simulatedException;
            }

            @Override
            public void close() {
                // No-op for this test double.
            }
        };

        Writer destinationWriter = new StringWriter();

        // Act & Assert: Attempt the copy operation and verify that the exact
        // exception we simulated is thrown and propagated.
        try {
            CharStreams.copy(faultyReader, destinationWriter);
            fail("Expected an IOException to be thrown, but the copy operation completed without error.");
        } catch (IOException actualException) {
            // Verify that the caught exception is the same instance we created,
            // ensuring no other unexpected exception occurred.
            assertEquals("The propagated exception should be the one thrown by the reader",
                simulatedException, actualException);
        }
    }
}