package com.fasterxml.jackson.core.io;

import java.io.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonEncoding;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MergedStream, which combines buffered data with an underlying InputStream.
 * MergedStream first returns data from a pre-filled buffer, then continues reading
 * from the underlying stream once the buffer is exhausted.
 */
class MergedStreamTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    void testMergedStreamReadOperations() throws Exception {
        // Setup: Create a MergedStream with buffered data "ABCDE" and stream data "FGHIJ"
        MergedStream mergedStream = createMergedStreamWithTestData();
        
        // Test 1: Initial state - should have 5 bytes available from buffer
        assertEquals(5, mergedStream.available(), "Should have 5 bytes available from buffer");
        
        // Test 2: Mark/reset not supported when buffered data exists
        assertFalse(mergedStream.markSupported(), "Mark should not be supported with buffered data");
        mergedStream.mark(1); // Should not throw exception, but has no effect
        
        // Test 3: Read single byte from buffer
        assertEquals((byte) 'A', mergedStream.read(), "First byte should be 'A' from buffer");
        
        // Test 4: Skip bytes within buffer
        assertEquals(3, mergedStream.skip(3), "Should skip 3 bytes (B, C, D)");
        
        // Test 5: Read into byte array - last byte from buffer
        byte[] readBuffer = new byte[5];
        int bytesRead = mergedStream.read(readBuffer, 1, 3);
        assertEquals(1, bytesRead, "Should read 1 byte (last from buffer)");
        assertEquals((byte) 'E', readBuffer[1], "Should read 'E' (last buffer byte)");
        
        // Test 6: Read from underlying stream after buffer is exhausted
        bytesRead = mergedStream.read(readBuffer, 0, 3);
        assertEquals(3, bytesRead, "Should read 3 bytes from underlying stream");
        assertEquals((byte) 'F', readBuffer[0], "First stream byte should be 'F'");
        assertEquals((byte) 'G', readBuffer[1], "Second stream byte should be 'G'");
        assertEquals((byte) 'H', readBuffer[2], "Third stream byte should be 'H'");
        
        // Test 7: Remaining bytes in underlying stream
        assertEquals(2, mergedStream.available(), "Should have 2 bytes remaining in stream");
        
        // Test 8: Skip remaining bytes (requesting more than available)
        assertEquals(2, mergedStream.skip(200), "Should skip only 2 remaining bytes");
        
        mergedStream.close();
    }
    
    /**
     * Creates a MergedStream for testing with:
     * - Buffer containing "ABCDE" starting at position 99
     * - Underlying stream containing "FGHIJ"
     */
    private MergedStream createMergedStreamWithTestData() throws Exception {
        IOContext ioContext = createTestIOContext();
        
        // Prepare buffered data: "ABCDE" at position 99 in recyclable buffer
        byte[] bufferWithData = ioContext.allocReadIOBuffer();
        byte[] firstData = "ABCDE".getBytes("UTF-8");
        int bufferStartPos = 99;
        System.arraycopy(firstData, 0, bufferWithData, bufferStartPos, firstData.length);
        
        // Prepare stream data: "FGHIJ"
        byte[] streamData = "FGHIJ".getBytes("UTF-8");
        InputStream underlyingStream = new ByteArrayInputStream(streamData);
        
        return new MergedStream(ioContext, underlyingStream, 
                               bufferWithData, bufferStartPos, bufferStartPos + firstData.length);
    }
    
    /**
     * Creates and configures an IOContext for testing
     */
    private IOContext createTestIOContext() {
        IOContext context = testIOContext();
        
        // Verify initial state
        assertNull(context.contentReference().getRawContent(), "Content reference should be null initially");
        assertFalse(context.isResourceManaged(), "Resource should not be managed initially");
        
        context.setEncoding(JsonEncoding.UTF8);
        context.close(); // Close context as it's no longer needed after MergedStream creation
        
        return context;
    }
}