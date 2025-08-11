/*
 * Refactored test suite for StreamCompressor with improved readability
 * Changes include:
 *   - Meaningful test method names
 *   - Explanatory comments for each test case
 *   - Clear separation of test scenarios
 *   - Descriptive variable names
 */
package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.Enumeration;
import java.util.zip.Deflater;
import org.apache.commons.compress.archivers.zip.StreamCompressor;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class StreamCompressor_ESTest extends StreamCompressor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testDeflateWithEmptySequenceInputStream_UpdatesByteCounters() throws Throwable {
        // Setup empty input stream
        StreamCompressor compressor = StreamCompressor.create(0, null);
        Enumeration<DataInputStream> emptyEnum = mock(Enumeration.class);
        when(emptyEnum.hasMoreElements()).thenReturn(false);
        SequenceInputStream emptyStream = new SequenceInputStream(emptyEnum);
        
        // Execute deflation
        compressor.deflate(emptyStream, 0);
        
        // Verify counters remain zero
        assertEquals(0L, compressor.getBytesWrittenForLastEntry());
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testWriteWithInvalidDeflateParameters_ThrowsException() throws Throwable {
        // Setup compressor with piped stream
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        // Prepare test data
        byte[] testData = new byte[2];
        compressor.write(testData, 1, 1, 4);  // Write 1 byte
        
        // Verify invalid deflate throws exception
        try {
            compressor.deflate(pipeIn, (byte) -102);
            fail("Expected exception during deflation with invalid parameters");
        } catch (Exception e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testWriteCounted_UpdatesTotalBytesWritten() throws Throwable {
        // Setup compressor with file output
        MockFileOutputStream fileOut = new MockFileOutputStream("testFile");
        StreamCompressor compressor = StreamCompressor.create(fileOut);
        
        // Write partial buffer
        byte[] data = new byte[4];
        compressor.writeCounted(data, 2, 2);
        
        // Verify byte count
        assertEquals(2L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testReset_ResetsEntryByteCounter() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        compressor.reset();
        
        assertEquals(0L, compressor.getBytesWrittenForLastEntry());
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testWriteFullBuffer_UpdatesTotalBytes() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        byte[] fullBuffer = new byte[1];
        compressor.writeCounted(fullBuffer);
        
        assertEquals(1L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testWriteZeroLength_DoesNotUpdateCounters() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        byte[] data = new byte[2];
        long bytesWritten = compressor.write(data, 0, 0, 1213);
        
        assertEquals(0L, bytesWritten);
        assertEquals(0L, compressor.getTotalBytesWritten());
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testWriteSingleByte_UpdatesCrcAndByteCount() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        byte[] data = new byte[2];
        compressor.write(data, 1, 1, 1871);
        
        assertEquals(1L, compressor.getTotalBytesWritten());
        assertEquals(3523407757L, compressor.getCrc32());
    }

    @Test(timeout = 4000)
    public void testWriteData_UpdatesCrc32() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        byte[] data = new byte[2];
        compressor.write(data, 1, 1, 4);
        
        assertEquals(3523407757L, compressor.getCrc32());
    }

    @Test(timeout = 4000)
    public void testWriteCountedWithInvalidLength_ThrowsIndexOutOfBounds() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        byte[] data = new byte[2];
        try {
            compressor.writeCounted(data, 1, 8);  // Length exceeds buffer
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testWriteCountedWithNegativeOffset_ThrowsArrayIndexOutOfBounds() throws Throwable {
        MockFileOutputStream fileOut = new MockFileOutputStream("testFile");
        StreamCompressor compressor = StreamCompressor.create(fileOut);
        
        byte[] data = new byte[4];
        try {
            compressor.writeCounted(data, -2345, 33);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testWriteCountedToNullData_ThrowsIOException() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        try {
            compressor.writeCounted(null, 1682, 935);
            fail("Expected IOException for null data");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testWriteCountedNullBuffer_ThrowsNullPointerException() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create(8, null);
        
        try {
            compressor.writeCounted(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testWriteNullBuffer_ThrowsNullPointerException() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        try {
            compressor.write(null, 2, 2, 2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithNegativeOffset_ThrowsArrayIndexOutOfBounds() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        byte[] emptyData = new byte[0];
        try {
            compressor.write(emptyData, -822, -822, -822);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testFlushDeflaterWithNullBackingStore_ThrowsNullPointerException() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create(0, null);
        
        try {
            compressor.flushDeflater();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testFlushDeflaterOnUnconnectedPipe_ThrowsIOException() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        try {
            compressor.flushDeflater();
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testDeflateWithInvalidBufferOffset_ThrowsArrayIndexOutOfBounds() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create(0, null);
        byte[] data = new byte[1];
        
        // Create stream with invalid read parameters
        ByteArrayInputStream invalidStream = new ByteArrayInputStream(data, -1129, 754);
        
        try {
            compressor.deflate(invalidStream, 754);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testDeflateOnUnconnectedPipe_ThrowsIOException() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        PipedInputStream unconnectedPipe = new PipedInputStream();  // Not connected
        
        try {
            compressor.deflate(unconnectedPipe, 2573);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testDeflateFinalBlock_ThrowsIOExceptionWhenPipeDisconnected() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        DataOutputStream dataOut = new DataOutputStream(pipeOut);
        Deflater deflater = new Deflater();
        StreamCompressor compressor = StreamCompressor.create(dataOut, deflater);
        
        try {
            compressor.deflate();  // Final deflation block
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testCreateWithInvalidCompressionLevel_ThrowsIllegalArgumentException() throws Throwable {
        try {
            StreamCompressor.create(-2410, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testWriteCountedWithNullBackingStore_ThrowsNullPointerException() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create(0, null);
        byte[] emptyData = new byte[0];
        
        try {
            compressor.writeCounted(emptyData, 0, 2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testDeflateWithNullDeflater_ThrowsNullPointerException() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut, null);
        
        try {
            compressor.deflate();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testFlushDeflater_UpdatesLastEntryByteCount() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        compressor.flushDeflater();
        
        assertEquals(2L, compressor.getBytesWrittenForLastEntry());
    }

    @Test(timeout = 4000)
    public void testDeflateEmptyStream_DoesNotUpdateCounters() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        // Create empty input streams chained together
        byte[] emptyData = new byte[0];
        ByteArrayInputStream emptyStream1 = new ByteArrayInputStream(emptyData);
        ByteArrayInputStream emptyStream2 = new ByteArrayInputStream(emptyData);
        SequenceInputStream chainedStreams = new SequenceInputStream(emptyStream1, emptyStream2);
        
        compressor.deflate(chainedStreams, 8192);
        
        assertEquals(0L, compressor.getBytesWrittenForLastEntry());
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testDeflateFinalBlock_UpdatesTotalBytes() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        compressor.deflate();  // Finalize deflation
        
        assertEquals(0L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testDeflateWithNullBackingStore_ThrowsNullPointerException() throws Throwable {
        Enumeration<MockFileInputStream> emptyEnum = mock(Enumeration.class);
        when(emptyEnum.hasMoreElements()).thenReturn(false);
        SequenceInputStream emptyStream = new SequenceInputStream(emptyEnum);
        StreamCompressor compressor = StreamCompressor.create(8, null);
        
        try {
            compressor.deflate(emptyStream, 8);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected outcome
        }
    }

    @Test(timeout = 4000)
    public void testClose_ResetsByteReadCounter() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        compressor.close();
        
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testDeflateFinalBlockWithObjectStream_UpdatesByteCount() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        Deflater deflater = new Deflater();
        ObjectOutputStream objOut = new ObjectOutputStream(pipeOut);
        StreamCompressor compressor = StreamCompressor.create(objOut, deflater);
        
        compressor.deflate();  // Finalize deflation
        
        assertEquals(2, deflater.getTotalOut());
        assertEquals(2L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testNewCompressor_HasZeroTotalBytesWritten() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        
        assertEquals(0L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testCreateWithNullBackingStore_HasZeroTotalBytes() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        assertEquals(0L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testNewCompressor_HasZeroBytesForLastEntry() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        assertEquals(0L, compressor.getBytesWrittenForLastEntry());
    }

    @Test(timeout = 4000)
    public void testWriteCountedToUnconnectedPipe_ThrowsIOException() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        byte[] data = new byte[1];
        
        try {
            compressor.writeCounted(data);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testNewCompressor_HasZeroCrc32() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        assertEquals(0L, compressor.getCrc32());
    }

    @Test(timeout = 4000)
    public void testNewCompressor_HasZeroBytesRead() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testCreateWithSeekableChannel_HasZeroBytesRead() throws Throwable {
        Deflater deflater = new Deflater();
        StreamCompressor compressor = StreamCompressor.create(null, deflater);
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testWriteToUnconnectedPipe_ThrowsIOException() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipeOut);
        byte[] data = new byte[2];
        
        try {
            compressor.write(data, 1, 1, 4);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Pipe not connected"));
        }
    }
}