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

/**
 * Contains tests for the {@link UTF8Writer} class.
 */
public class UTF8WriterTest {

    /**
     * Tests that the close() method propagates an IOException thrown by the
     * underlying output stream when flushing the buffer.
     */
    @Test
    public void close_whenUnderlyingStreamIsFaulty_shouldPropagateIOException() throws IOException {
        // ARRANGE
        // 1. IOContext is a required dependency for UTF8Writer.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.redacted(),
                true);

        // 2. Use an unconnected PipedOutputStream to simulate a faulty stream.
        //    Any attempt to write or flush to it will throw an IOException.
        PipedOutputStream faultyOutputStream = new PipedOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, faultyOutputStream);

        // 3. Write a character to the writer's internal buffer. This ensures that
        //    the subsequent close() call will attempt to flush the buffer.
        utf8Writer.write('A');

        // ACT & ASSERT
        try {
            // The close() method will try to flush the buffered content ('A')
            // to the faulty stream, triggering the exception.
            utf8Writer.close();
            fail("Expected an IOException because the underlying pipe is not connected.");
        } catch (IOException e) {
            // Verify that the exception is the one we expect from the PipedOutputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}