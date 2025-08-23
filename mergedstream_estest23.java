package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MergedStream_ESTestTest23 extends MergedStream_ESTest_scaffolding {

    /**
     * Tests that if the MergedStream's internal buffer is empty, a call to skip()
     * is delegated to the underlying stream, and any exception from that stream is
     * correctly propagated.
     */
    @Test
    public void skipWithEmptyBufferShouldPropagateExceptionFromUnderlyingStream() {
        // Arrange: Create an underlying input stream that is guaranteed to throw an
        // exception on any read or skip operation. A PipedInputStream that is not
        // connected to a PipedOutputStream serves this purpose perfectly.
        InputStream faultyUnderlyingStream = new PipedInputStream();

        // Arrange: Set up the MergedStream with an empty prefix buffer. This is achieved
        // by setting the start and end pointers to the same value. This forces any
        // operation to immediately fall back to the underlying stream.
        byte[] prefixBuffer = new byte[20];
        int start = 98;
        int end = 98; // When start == end, the prefix buffer is considered empty.
        MergedStream mergedStream = new MergedStream(null, faultyUnderlyingStream, prefixBuffer, start, end);

        // Act & Assert: Attempt to skip bytes. We expect this to fail because the call
        // will be delegated to the unconnected PipedInputStream.
        try {
            mergedStream.skip(98L);
            fail("Expected an IOException because the underlying stream should throw one.");
        } catch (IOException e) {
            // Assert: Verify that the propagated exception is the one we expect from the
            // unconnected pipe.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}