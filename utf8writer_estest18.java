package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The original test class name 'UTF8Writer_ESTestTest18' suggests it was auto-generated.
// A more descriptive name would be 'UTF8WriterTest'.
public class UTF8Writer_ESTestTest18_Refactored {

    /**
     * Verifies that if the underlying OutputStream throws an IOException during a flush,
     * the UTF8Writer correctly propagates this exception.
     */
    @Test(timeout = 4000)
    public void flushShouldPropagateIOExceptionFromUnderlyingStream() {
        // Arrange: Set up a writer with an underlying stream that is guaranteed to fail on flush.
        // A PipedOutputStream that is not connected to a PipedInputStream will throw
        // an IOException("Pipe not connected") when flushed or closed.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.redacted(),
                false); // 'false' for not managing the resource

        PipedOutputStream failingOutputStream = new PipedOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, failingOutputStream);

        try {
            // Write a character to the internal buffer to ensure flush() has work to do.
            utf8Writer.write('!');

            // Act: Attempt to flush the writer. This should trigger the IOException from
            // the underlying unconnected PipedOutputStream.
            utf8Writer.flush();

            // This line should not be reached.
            fail("Expected an IOException to be thrown due to the unconnected pipe.");

        } catch (IOException e) {
            // Assert: Verify that the expected exception was caught and has the correct message.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}