package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import static org.junit.Assert.*;

public class MergedStream_ESTestTest15 extends MergedStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void read_whenPrefixBufferIsExhausted_delegatesToFailingUnderlyingStream_andThrowsIOException() throws IOException {
        // Arrange: Set up a MergedStream with a small prefix buffer and an underlying
        // stream that is designed to fail.
        byte[] prefixData = new byte[] { 42 };

        // An unconnected PipedInputStream will throw an IOException ("Pipe not connected") on read.
        // This simulates a failing underlying stream to test exception propagation.
        InputStream failingUnderlyingStream = new PipedInputStream();
        
        // Create a MergedStream with a one-byte prefix.
        // Note: The original test created an empty buffer ([1, 1)), which was likely a bug,
        // as the first read would fail immediately. This version corrects it to be non-empty.
        MergedStream mergedStream = new MergedStream(null, failingUnderlyingStream, prefixData, 0, prefixData.length);
        
        byte[] readDestination = new byte[10];

        // Act & Assert (Part 1): The first read should succeed, consuming data from the prefix buffer.
        int bytesRead = mergedStream.read(readDestination);
        
        assertEquals("Should have read exactly one byte from the prefix buffer", 1, bytesRead);
        assertEquals("The byte read should match the prefix data", 42, readDestination[0]);

        // Act & Assert (Part 2): The second read should fail, as the prefix buffer is now empty
        // and the read attempt delegates to the failing underlying stream.
        try {
            mergedStream.read(readDestination);
            fail("Expected an IOException when reading from the underlying stream after the buffer was exhausted.");
        } catch (IOException e) {
            // Verify that the exception from the underlying stream was propagated correctly.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}