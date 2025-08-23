package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link MergedStream} class, focusing on its behavior
 * when transitioning from its internal buffer to the underlying stream.
 */
public class MergedStreamTest {

    /**
     * Tests that after the prefixed buffer is fully read, the MergedStream
     * correctly switches to reading from the underlying InputStream.
     * It also verifies that if the underlying stream throws an IOException,
     * that exception is propagated to the caller.
     */
    @Test
    public void whenPrefixBufferIsExhausted_thenReadsFromUnderlyingStreamAndPropagatesException() throws IOException {
        // Arrange
        // 1. An underlying input stream that is guaranteed to fail upon reading.
        //    An unconnected PipedInputStream is a simple way to achieve this.
        InputStream underlyingFailingStream = new PipedInputStream();

        // 2. A prefix buffer with a single byte of data to be read first.
        byte[] prefixBuffer = new byte[]{(byte) 0xFF};
        int prefixStart = 0;
        int prefixEnd = 1; // Represents a buffer segment of length 1.

        // 3. A minimal, valid IOContext.
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);

        MergedStream mergedStream = new MergedStream(ioContext, underlyingFailingStream, prefixBuffer, prefixStart, prefixEnd);

        // Act & Assert: First read should succeed by consuming the single byte from the prefix buffer.
        int firstByte = mergedStream.read();
        assertEquals(0xFF, firstByte);

        // Act & Assert: The prefix buffer is now exhausted. The next read should attempt to use the
        // underlying stream, which will throw an IOException.
        try {
            mergedStream.read();
            fail("Expected an IOException because the underlying stream should fail.");
        } catch (IOException e) {
            // Verify that the exception is the one we expect from the PipedInputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}