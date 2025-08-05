package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.Before;

/**
 * Test suite for StreamCompressor functionality.
 * Tests various compression scenarios, error conditions, and state management.
 */
public class StreamCompressor_ESTest {

    private static final int STORED_METHOD = ZipEntry.STORED;
    private static final int DEFLATED_METHOD = ZipEntry.DEFLATED;
    private static final int DEFAULT_COMPRESSION_LEVEL = Deflater.DEFAULT_COMPRESSION;
    
    private PipedOutputStream connectedPipedOutput;
    private PipedInputStream connectedPipedInput;
    
    @Before
    public void setUp() throws IOException {
        // Set up connected pipes for tests that need working I/O
        connectedPipedOutput = new PipedOutputStream();
        connectedPipedInput = new PipedInputStream(connectedPipedOutput);
    }

    // ========== Factory Method Tests ==========
    
    @Test
    public void testCreateWithScatterGatherBackingStore_DefaultCompression() {
        StreamCompressor compressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        
        assertInitialState(compressor);
    }
    
    @Test
    public void testCreateWithScatterGatherBackingStore_CustomCompressionLevel() {
        StreamCompressor compressor = StreamCompressor.create(DEFAULT_COMPRESSION_LEVEL, (ScatterGatherBackingStore) null);
        
        assertInitialState(compressor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidCompressionLevel_ShouldThrowException() {
        StreamCompressor.create(-2410, (ScatterGatherBackingStore) null);
    }
    
    @Test
    public void testCreateWithOutputStream() {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        
        assertInitialState(compressor);
    }
    
    @Test
    public void testCreateWithDataOutput() throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(connectedPipedOutput);
        Deflater deflater = new Deflater();
        
        StreamCompressor compressor = StreamCompressor.create(dataOutput, deflater);
        
        assertInitialState(compressor);
    }

    // ========== Basic Write Operations Tests ==========
    
    @Test
    public void testWriteCountedByteArray_UpdatesTotalBytesWritten() throws IOException {
        MockFileOutputStream mockOutput = new MockFileOutputStream("test-file");
        StreamCompressor compressor = StreamCompressor.create(mockOutput);
        byte[] testData = new byte[4];
        
        compressor.writeCounted(testData, 2, 2);
        
        assertEquals("Should track total bytes written", 2L, compressor.getTotalBytesWritten());
    }
    
    @Test
    public void testWriteCountedFullArray() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        byte[] testData = new byte[1];
        
        compressor.writeCounted(testData);
        
        assertEquals("Should write entire array", 1L, compressor.getTotalBytesWritten());
    }
    
    @Test
    public void testWriteWithZeroLength_NoDataWritten() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        byte[] testData = new byte[2];
        
        long bytesWritten = compressor.write(testData, 0, 0, STORED_METHOD);
        
        assertEquals("No bytes should be written for zero length", 0L, bytesWritten);
        assertEquals("Total bytes written should remain zero", 0L, compressor.getTotalBytesWritten());
        assertEquals("Bytes read should remain zero", 0L, compressor.getBytesRead());
    }
    
    @Test
    public void testWriteWithData_UpdatesCountersAndCrc() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        byte[] testData = new byte[2];
        
        compressor.write(testData, 1, 1, STORED_METHOD);
        
        assertEquals("Should update total bytes written", 1L, compressor.getTotalBytesWritten());
        assertEquals("Should calculate CRC32", 3523407757L, compressor.getCrc32());
    }

    // ========== Deflation Tests ==========
    
    @Test
    public void testDeflateEmptyStream_NoDataProcessed() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(DEFAULT_COMPRESSION_LEVEL, (ScatterGatherBackingStore) null);
        @SuppressWarnings("unchecked")
        Enumeration<ByteArrayInputStream> emptyEnumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        when(emptyEnumeration.hasMoreElements()).thenReturn(false);
        SequenceInputStream emptyStream = new SequenceInputStream(emptyEnumeration);
        
        compressor.deflate(emptyStream, STORED_METHOD);
        
        assertEquals("No bytes should be written for empty stream", 0L, compressor.getBytesWrittenForLastEntry());
        assertEquals("No bytes should be read from empty stream", 0L, compressor.getBytesRead());
    }
    
    @Test
    public void testDeflateEmptyByteArray() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        byte[] emptyData = new byte[0];
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(emptyData);
        SequenceInputStream sequenceStream = new SequenceInputStream(emptyStream, emptyStream);
        
        compressor.deflate(sequenceStream, 8192);
        
        assertEquals("No bytes should be written for empty data", 0L, compressor.getBytesWrittenForLastEntry());
        assertEquals("No bytes should be read from empty data", 0L, compressor.getBytesRead());
    }
    
    @Test
    public void testDeflateWithoutInput() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        
        compressor.deflate();
        
        assertEquals("Should not write any data without input", 0L, compressor.getTotalBytesWritten());
    }

    // ========== State Management Tests ==========
    
    @Test
    public void testReset_ClearsCounters() {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        
        compressor.reset();
        
        assertEquals("Bytes written for last entry should be zero after reset", 0L, compressor.getBytesWrittenForLastEntry());
        assertEquals("Bytes read should be zero after reset", 0L, compressor.getBytesRead());
    }
    
    @Test
    public void testFlushDeflater_UpdatesBytesWritten() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        
        compressor.flushDeflater();
        
        assertEquals("Should write deflater footer", 2L, compressor.getBytesWrittenForLastEntry());
    }
    
    @Test
    public void testClose_CompletesSuccessfully() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        
        compressor.close();
        
        assertEquals("Bytes read should remain accessible after close", 0L, compressor.getBytesRead());
    }

    // ========== Error Condition Tests ==========
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteCountedWithInvalidLength_ShouldThrowException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        byte[] testData = new byte[2];
        
        compressor.writeCounted(testData, 1, 8); // length > available data
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testWriteCountedWithNegativeOffset_ShouldThrowException() throws IOException {
        MockFileOutputStream mockOutput = new MockFileOutputStream("test-file");
        StreamCompressor compressor = StreamCompressor.create(mockOutput);
        byte[] testData = new byte[4];
        
        compressor.writeCounted(testData, -2345, 33);
    }
    
    @Test(expected = NullPointerException.class)
    public void testWriteCountedWithNullArray_ShouldThrowException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(DEFAULT_COMPRESSION_LEVEL, (ScatterGatherBackingStore) null);
        
        compressor.writeCounted(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testWriteWithNullArray_ShouldThrowException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        
        compressor.write(null, 2, 2, STORED_METHOD);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testWriteWithNegativeIndices_ShouldThrowException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        byte[] emptyData = new byte[0];
        
        compressor.write(emptyData, -822, -822, -822);
    }
    
    @Test(expected = IOException.class)
    public void testWriteCountedWithDisconnectedPipe_ShouldThrowIOException() throws IOException {
        PipedOutputStream disconnectedPipe = new PipedOutputStream(); // Not connected
        StreamCompressor compressor = StreamCompressor.create(disconnectedPipe);
        
        compressor.writeCounted(null, 1682, 935);
    }
    
    @Test(expected = NullPointerException.class)
    public void testFlushDeflaterWithNullBackingStore_ShouldThrowException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(DEFAULT_COMPRESSION_LEVEL, (ScatterGatherBackingStore) null);
        
        compressor.flushDeflater();
    }
    
    @Test(expected = IOException.class)
    public void testFlushDeflaterWithDisconnectedPipe_ShouldThrowIOException() throws IOException {
        PipedOutputStream disconnectedPipe = new PipedOutputStream(); // Not connected
        StreamCompressor compressor = StreamCompressor.create(disconnectedPipe);
        
        compressor.flushDeflater();
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDeflateWithInvalidByteArrayBounds_ShouldThrowException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(DEFAULT_COMPRESSION_LEVEL, (ScatterGatherBackingStore) null);
        byte[] testData = new byte[1];
        ByteArrayInputStream invalidStream = new ByteArrayInputStream(testData, -1129, 754);
        
        compressor.deflate(invalidStream, 754);
    }
    
    @Test(expected = IOException.class)
    public void testDeflateWithDisconnectedPipe_ShouldThrowIOException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        PipedInputStream disconnectedInput = new PipedInputStream(); // Not connected
        
        compressor.deflate(disconnectedInput, 2573);
    }
    
    @Test(expected = NullPointerException.class)
    public void testDeflateWithNullDeflater_ShouldThrowException() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput, null);
        
        compressor.deflate();
    }

    // ========== Integration Tests ==========
    
    @Test
    public void testCompleteWorkflow_WithDataOutput() throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(connectedPipedOutput);
        Deflater deflater = new Deflater();
        StreamCompressor compressor = StreamCompressor.create(dataOutput, deflater);
        
        compressor.deflate();
        
        assertEquals("Deflater should output compressed data", 2, deflater.getTotalOut());
        assertEquals("Compressor should track total bytes written", 2L, compressor.getTotalBytesWritten());
    }
    
    @Test
    public void testWriteAndDeflateSequence_ProducesExpectedResults() throws IOException {
        StreamCompressor compressor = StreamCompressor.create(connectedPipedOutput);
        byte[] testData = new byte[2];
        
        // This test demonstrates a potential issue where deflate is called after write
        // but with incompatible stream states
        compressor.write(testData, 1, 1, DEFLATED_METHOD);
        
        try {
            compressor.deflate(connectedPipedInput, -102);
            fail("Expected an exception due to stream state conflict");
        } catch (Exception e) {
            // Expected - the test demonstrates error condition
            assertTrue("Should throw an exception", true);
        }
    }

    // ========== Helper Methods ==========
    
    private void assertInitialState(StreamCompressor compressor) {
        assertEquals("Initial bytes read should be zero", 0L, compressor.getBytesRead());
        assertEquals("Initial total bytes written should be zero", 0L, compressor.getTotalBytesWritten());
        assertEquals("Initial bytes written for last entry should be zero", 0L, compressor.getBytesWrittenForLastEntry());
        assertEquals("Initial CRC32 should be zero", 0L, compressor.getCrc32());
    }
}