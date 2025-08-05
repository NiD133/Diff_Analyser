package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.MergedStream;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.*;

/**
 * Test suite for MergedStream functionality.
 * MergedStream is an InputStream that first returns data from a buffer,
 * then delegates to an underlying InputStream.
 */
public class MergedStreamTest {

    // Test Data Setup Helpers
    
    private IOContext createIOContext() {
        StreamReadConstraints readConstraints = StreamReadConstraints.defaults();
        StreamWriteConstraints writeConstraints = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorConfig = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentRef = ContentReference.rawReference(false, readConstraints);
        
        return new IOContext(readConstraints, writeConstraints, errorConfig, 
                           bufferRecycler, contentRef, false);
    }

    // Skip Operation Tests
    
    @Test
    public void testSkip_NegativeValue_ReturnsZero() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream(100);
        byte[] buffer = new byte[5];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 0, 2);
        
        long skipped = stream.skip(-10L);
        
        assertEquals("Negative skip should return 0", 0L, skipped);
    }

    @Test
    public void testSkip_ZeroValue_ReturnsZero() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream(100);
        byte[] buffer = new byte[5];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 0, 0);
        
        long skipped = stream.skip(0L);
        
        assertEquals("Zero skip should return 0", 0L, skipped);
    }

    @Test
    public void testSkip_WithBufferedData_SkipsFromBuffer() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream(100);
        byte[] buffer = new byte[10];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 1, 5);
        
        long skipped = stream.skip(2L);
        
        assertEquals("Should skip 2 bytes from buffer", 2L, skipped);
    }

    @Test
    public void testSkip_LargeValue_DelegatesToUnderlyingStream() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream(3000);
        byte[] buffer = new byte[8];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, -1, 50);
        
        long skipped = stream.skip(1000L);
        
        assertEquals("Should delegate large skip to underlying stream", 1000L, skipped);
    }

    // Read Operation Tests
    
    @Test
    public void testRead_SingleByte_FromBuffer() throws IOException {
        IOContext context = createIOContext();
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream input = new PipedInputStream(output, 1000);
        
        byte[] buffer = new byte[]{-25, 10}; // -25 becomes 231 when read as unsigned
        MergedStream stream = new MergedStream(context, input, buffer, 0, 2);
        
        int byteRead = stream.read();
        
        assertEquals("Should read first byte from buffer as unsigned", 231, byteRead);
    }

    @Test
    public void testRead_SingleByte_FromUnderlyingStreamWhenBufferEmpty() throws IOException {
        byte[] emptyBuffer = new byte[0];
        ByteArrayInputStream underlyingStream = new ByteArrayInputStream(new byte[0]);
        MergedStream stream = new MergedStream(null, underlyingStream, emptyBuffer, 0, 0);
        
        int result = stream.read();
        
        assertEquals("Should return -1 when both buffer and stream are empty", -1, result);
    }

    @Test
    public void testRead_ByteArray_FromBuffer() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream();
        byte[] sourceBuffer = new byte[]{1, 2, 3, 4, 5};
        MergedStream stream = new MergedStream(null, underlyingStream, sourceBuffer, 0, 3);
        
        byte[] readBuffer = new byte[5];
        int bytesRead = stream.read(readBuffer);
        
        assertEquals("Should read 3 bytes from buffer", 3, bytesRead);
        assertEquals("First byte should match", 1, readBuffer[0]);
        assertEquals("Second byte should match", 2, readBuffer[1]);
        assertEquals("Third byte should match", 3, readBuffer[2]);
    }

    @Test
    public void testRead_ByteArrayWithOffset_ZeroLength() throws IOException {
        IOContext context = createIOContext();
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream input = new PipedInputStream(output, 1000);
        byte[] buffer = new byte[2];
        MergedStream stream = new MergedStream(context, input, buffer, 0, 2);
        
        byte[] readBuffer = new byte[5];
        int bytesRead = stream.read(readBuffer, 0, 0);
        
        assertEquals("Reading zero bytes should return 0", 0, bytesRead);
    }

    // Available Bytes Tests
    
    @Test
    public void testAvailable_WithBufferedData() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream(3000);
        byte[] buffer = new byte[10];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 1, 3000);
        
        int available = stream.available();
        
        assertEquals("Should return buffer size minus current position plus underlying available", 
                    2999, available);
    }

    @Test
    public void testAvailable_WithNegativeBufferSize() throws IOException {
        IOContext context = createIOContext();
        PipedInputStream underlyingStream = new PipedInputStream();
        byte[] buffer = new byte[5];
        MergedStream stream = new MergedStream(context, underlyingStream, buffer, 2, -1000);
        
        int available = stream.available();
        
        assertEquals("Should handle negative buffer size correctly", -1002, available);
    }

    @Test
    public void testAvailable_EmptyBuffer() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream();
        byte[] buffer = new byte[1];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 1, 1);
        
        int available = stream.available();
        
        assertEquals("Should return 0 when buffer is empty", 0, available);
    }

    // Mark and Reset Tests
    
    @Test
    public void testMarkSupported_WithSupportingUnderlyingStream() throws IOException {
        IOContext context = createIOContext();
        PipedInputStream pipedStream = new PipedInputStream();
        BufferedInputStream bufferedStream = new BufferedInputStream(pipedStream);
        MergedStream stream = new MergedStream(context, bufferedStream, null, 1000, 3);
        
        boolean supported = stream.markSupported();
        
        assertTrue("Should support mark when underlying stream supports it", supported);
    }

    @Test
    public void testMarkSupported_WithNonSupportingUnderlyingStream() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream();
        MergedStream stream = new MergedStream(null, underlyingStream, null, -1, -1);
        
        boolean supported = stream.markSupported();
        
        assertFalse("Should not support mark when underlying stream doesn't support it", supported);
    }

    @Test
    public void testMark_DelegatesToUnderlyingStream() throws IOException {
        IOContext context = createIOContext();
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream input = new PipedInputStream(output, 500);
        MergedStream stream = new MergedStream(context, input, null, 100, 2);
        
        // Should not throw exception - delegates to underlying stream
        stream.mark(10);
    }

    @Test
    public void testReset_WithBufferedData() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream();
        byte[] buffer = new byte[5];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 1, 1);
        
        // Should not throw exception when buffer has data
        stream.reset();
    }

    @Test(expected = IOException.class)
    public void testReset_WithoutMarkSupport_ThrowsException() throws IOException {
        IOContext context = createIOContext();
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream input = new PipedInputStream(output, 1000);
        MergedStream stream = new MergedStream(context, input, null, 100, 2);
        
        stream.reset(); // Should throw IOException: "mark/reset not supported"
    }

    // Close Operation Tests
    
    @Test
    public void testClose_WithValidStream() throws IOException {
        IOContext context = createIOContext();
        ByteArrayInputStream underlyingStream = new ByteArrayInputStream(new byte[0]);
        MergedStream stream = new MergedStream(context, underlyingStream, null, 1, 100);
        
        // Should not throw exception
        stream.close();
    }

    // Error Condition Tests
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRead_InvalidBufferAccess_ThrowsException() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream();
        byte[] buffer = new byte[1];
        // Invalid: start position (98) is beyond buffer length (1)
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 98, 98);
        
        stream.read(); // Should throw ArrayIndexOutOfBoundsException
    }

    @Test(expected = IOException.class)
    public void testRead_DisconnectedPipe_ThrowsIOException() throws IOException {
        PipedInputStream disconnectedPipe = new PipedInputStream();
        byte[] buffer = new byte[5];
        MergedStream stream = new MergedStream(null, disconnectedPipe, buffer, 1, 1);
        
        // First read consumes buffer
        stream.read(new byte[5]);
        
        // Second read tries to use disconnected pipe
        stream.read(new byte[5]); // Should throw IOException: "Pipe not connected"
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_NegativeOffset_ThrowsException() throws IOException {
        PipedInputStream underlyingStream = new PipedInputStream();
        byte[] buffer = new byte[5];
        MergedStream stream = new MergedStream(null, underlyingStream, buffer, 1, 1);
        
        // Consume buffer first
        stream.read(new byte[5]);
        
        // Try to read with negative offset
        stream.read(new byte[5], -10, 0); // Should throw IndexOutOfBoundsException
    }
}