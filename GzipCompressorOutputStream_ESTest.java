package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import org.apache.commons.compress.compressors.gzip.ExtraField;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;

/**
 * Test suite for GzipCompressorOutputStream functionality.
 * Tests cover construction, writing data, error handling, and stream lifecycle.
 */
public class GzipCompressorOutputStream_ESTest {

    // === Constructor Tests ===
    
    @Test
    public void shouldCreateGzipStreamWithDefaultParameters() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        
        assertNotNull(gzipStream);
        assertFalse(gzipStream.isClosed());
        // Verify GZIP header is written (10 bytes for default header)
        assertEquals(10, output.size());
    }
    
    @Test
    public void shouldCreateGzipStreamWithCustomParameters() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setCompressionLevel(9); // Maximum compression
        
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output, params);
        
        assertNotNull(gzipStream);
        assertFalse(gzipStream.isClosed());
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenOutputStreamIsNull() throws IOException {
        new GzipCompressorOutputStream(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenParametersAreNull() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new GzipCompressorOutputStream(output, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidDeflateStrategy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setDeflateStrategy(14); // Invalid strategy value
        
        new GzipCompressorOutputStream(output, params);
    }
    
    @Test(expected = IOException.class)
    public void shouldThrowExceptionForUnconnectedPipe() throws IOException {
        PipedOutputStream pipe = new PipedOutputStream(); // Not connected
        new GzipCompressorOutputStream(pipe);
    }

    // === Writing Data Tests ===
    
    @Test
    public void shouldWriteEmptyByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        
        gzipStream.write(new byte[0]);
        
        // Should only contain GZIP header at this point
        assertEquals(10, output.size());
    }
    
    @Test
    public void shouldWriteSingleByte() throws IOException {
        MockFileOutputStream output = new MockFileOutputStream("test.gz");
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        
        gzipStream.write(123);
        
        assertFalse(gzipStream.isClosed());
    }
    
    @Test
    public void shouldWriteByteArrayWithZeroLength() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        byte[] data = new byte[6];
        
        gzipStream.write(data, 0, 0); // Write 0 bytes
        
        assertEquals(10, output.size()); // Only header written
    }

    // === Error Handling Tests ===
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenWritingNullByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        
        gzipStream.write((byte[]) null);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenWritingNullByteArrayWithOffset() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        
        gzipStream.write(null, 10, 10);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowExceptionForInvalidArrayAccess() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        byte[] emptyArray = new byte[0];
        
        gzipStream.write(emptyArray, 2860, 2860); // Invalid offset/length
    }

    // === Closed Stream Tests ===
    
    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenWritingToClosedStream_ByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        gzipStream.close();
        
        gzipStream.write(new byte[0]);
    }
    
    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenWritingToClosedStream_SingleByte() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        gzipStream.close();
        
        gzipStream.write(520);
    }
    
    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenWritingToClosedStream_WithOffset() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        gzipStream.close();
        
        gzipStream.write(new byte[0], 0, 0);
    }

    // === Stream Lifecycle Tests ===
    
    @Test
    public void shouldFinishStreamAndWriteTrailer() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        
        gzipStream.finish();
        
        // Header (10 bytes) + trailer (10 bytes) = 20 bytes total
        assertEquals(20, output.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenFinishingClosedStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        gzipStream.close();
        
        gzipStream.finish(); // Should fail - deflater is closed
    }
    
    @Test
    public void shouldAllowMultipleCloseOperations() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        
        gzipStream.close();
        gzipStream.close(); // Should not throw exception
        
        assertTrue(gzipStream.isClosed());
    }
    
    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenUnderlyingStreamFails() throws IOException {
        MockFileOutputStream output = new MockFileOutputStream("test.gz");
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output);
        output.close(); // Close underlying stream first
        
        gzipStream.finish(); // Should fail
    }

    // === Parameter Configuration Tests ===
    
    @Test
    public void shouldConfigureCompressionLevel() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setCompressionLevel(1); // Fast compression
        
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output, params);
        
        assertEquals(10, output.size());
        // Verify compression level is reflected in header flags
        String headerContent = output.toString();
        assertTrue("Header should contain compression level indicator", 
                   headerContent.contains("\u0004")); // Fast compression flag
    }
    
    @Test
    public void shouldConfigureHeaderCRC() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setHeaderCRC(true);
        
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output, params);
        
        // Header with CRC should be 12 bytes (10 + 2 for CRC)
        assertEquals(12, output.size());
    }
    
    @Test
    public void shouldConfigureExtraField() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setExtraField(new ExtraField());
        
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(output, params);
        
        // Should still create valid stream
        assertNotNull(gzipStream);
        assertEquals(10, output.size()); // Empty extra field doesn't change header size
    }

    // === Integration Tests ===
    
    @Test
    public void shouldWorkAsWrappedStream() throws IOException {
        MockFileOutputStream fileOutput = new MockFileOutputStream("test.gz");
        GzipCompressorOutputStream gzipStream = new GzipCompressorOutputStream(fileOutput);
        
        // Should be able to wrap with other streams
        java.io.ObjectOutputStream objectStream = new java.io.ObjectOutputStream(gzipStream);
        
        assertNotNull(objectStream);
    }
}