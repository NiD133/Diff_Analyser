package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link MergedStream} class, focusing on its behavior
 * when transitioning from its internal buffer to the underlying stream.
 */
public class MergedStreamTest {

    /**
     * Verifies that after the internal buffer is fully read, MergedStream correctly
     * delegates subsequent read calls to the underlying input stream.
     */
    @Test
    public void shouldReadFromUnderlyingStreamAfterInternalBufferIsExhausted() {
        // Arrange:
        // 1. Create an underlying stream that is guaranteed to fail upon reading.
        //    An unconnected PipedInputStream is perfect for this scenario.
        PipedInputStream failingUnderlyingStream = new PipedInputStream();

        // 2. Define a buffer to be "pre-pended" by the MergedStream.
        //    The range [start, end] is inclusive, so start=1 and end=1 defines a 1-byte buffer.
        byte[] internalBuffer = new byte[10];
        int bufferStart = 1;
        int bufferEnd = 1;

        // 3. Instantiate the MergedStream with the buffer and the failing stream.
        //    The IOContext is not relevant for this test path, so it can be null.
        MergedStream mergedStream = new MergedStream(null, failingUnderlyingStream, internalBuffer, bufferStart, bufferEnd);

        byte[] destination = new byte[10];

        // Act & Assert - Part 1: The first read should succeed by consuming the internal buffer.
        try {
            int bytesRead = mergedStream.read(destination, 0, 1);
            assertEquals("Should read exactly one byte from the internal buffer", 1, bytesRead);
        } catch (IOException e) {
            fail("Reading from the pre-pended buffer should not cause an IOException: " + e.getMessage());
        }

        // Act & Assert - Part 2: The second read should fail.
        // The internal buffer is now exhausted, so MergedStream must delegate to the
        // underlying stream, which we designed to throw an exception.
        try {
            mergedStream.read(destination, 0, 1);
            fail("Expected an IOException when attempting to read from the underlying stream");
        } catch (IOException e) {
            // Verify that the expected exception from the PipedInputStream was thrown.
            assertTrue(
                "Exception message should indicate a connection problem",
                e.getMessage().contains("Pipe not connected")
            );
        }
    }
}