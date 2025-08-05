package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.Deflater;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StreamCompressorTest extends StreamCompressor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testDeflateWithEmptyEnumeration() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create(0, null);
        Enumeration<DataInputStream> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(false);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);

        compressor.deflate(sequenceInputStream, 0);

        assertEquals(0L, compressor.getBytesWrittenForLastEntry());
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testWriteAndDeflateWithPipedStreams() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);
        byte[] byteArray = new byte[2];

        compressor.write(byteArray, 1, 1, 4);

        try {
            compressor.deflate(pipedInputStream, -102);
            fail("Expecting IOException due to pipe not being connected");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testWriteCountedBytes() throws Throwable {
        MockFileOutputStream mockOutputStream = new MockFileOutputStream("testFile");
        StreamCompressor compressor = StreamCompressor.create(mockOutputStream);
        byte[] byteArray = new byte[4];

        compressor.writeCounted(byteArray, 2, 2);

        assertEquals(2L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testResetCompressor() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        compressor.reset();

        assertEquals(0L, compressor.getBytesWrittenForLastEntry());
        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testWriteCountedSingleByte() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);
        byte[] byteArray = new byte[1];

        compressor.writeCounted(byteArray);

        assertEquals(1L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testWriteZeroBytes() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);
        byte[] byteArray = new byte[2];

        long bytesWritten = compressor.write(byteArray, 0, 0, 1213);

        assertEquals(0L, compressor.getTotalBytesWritten());
        assertEquals(0L, compressor.getBytesRead());
        assertEquals(0L, bytesWritten);
    }

    @Test(timeout = 4000)
    public void testWriteSingleByteWithCRC() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);
        byte[] byteArray = new byte[2];

        compressor.write(byteArray, 1, 1, 1871);

        assertEquals(3523407757L, compressor.getCrc32());
        assertEquals(1L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testWriteWithInvalidIndex() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);
        byte[] byteArray = new byte[2];

        try {
            compressor.writeCounted(byteArray, 1, 8);
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithNegativeIndex() throws Throwable {
        MockFileOutputStream mockOutputStream = new MockFileOutputStream("testFile");
        StreamCompressor compressor = StreamCompressor.create(mockOutputStream);
        byte[] byteArray = new byte[4];

        try {
            compressor.writeCounted(byteArray, -2345, 33);
            fail("Expecting ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteNullByteArray() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        try {
            compressor.writeCounted(null, 1682, 935);
            fail("Expecting IOException due to pipe not being connected");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testFlushDeflaterWithNullBackingStore() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create(0, null);

        try {
            compressor.flushDeflater();
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFlushDeflaterWithUnconnectedPipe() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        try {
            compressor.flushDeflater();
            fail("Expecting IOException due to pipe not being connected");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testDeflateWithInvalidInputStream() throws Throwable {
        StreamCompressor compressor = StreamCompressor.create(0, null);
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, -1129, 754);

        try {
            compressor.deflate(byteArrayInputStream, 754);
            fail("Expecting ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDeflateWithUnconnectedPipe() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);
        PipedInputStream pipedInputStream = new PipedInputStream();

        try {
            compressor.deflate(pipedInputStream, 2573);
            fail("Expecting IOException due to pipe not being connected");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testDeflateWithDataOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(pipedOutputStream);
        Deflater deflater = new Deflater();
        StreamCompressor compressor = StreamCompressor.create(dataOutputStream, deflater);

        try {
            compressor.deflate();
            fail("Expecting IOException due to pipe not being connected");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateCompressorWithNegativeCompressionLevel() throws Throwable {
        try {
            StreamCompressor.create(-2410, null);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCloseCompressor() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        compressor.close();

        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testDeflateWithEmptySequenceInputStream() throws Throwable {
        Enumeration<MockFileInputStream> enumeration = mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(false);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        StreamCompressor compressor = StreamCompressor.create(8, null);

        try {
            compressor.deflate(sequenceInputStream, 8);
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetTotalBytesWritten() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        assertEquals(0L, compressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testGetBytesWrittenForLastEntry() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        assertEquals(0L, compressor.getBytesWrittenForLastEntry());
    }

    @Test(timeout = 4000)
    public void testGetCrc32() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        assertEquals(0L, compressor.getCrc32());
    }

    @Test(timeout = 4000)
    public void testGetBytesRead() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);

        assertEquals(0L, compressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testWriteWithUnconnectedPipe() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(pipedOutputStream);
        byte[] byteArray = new byte[2];

        try {
            compressor.write(byteArray, 1, 1, 4);
            fail("Expecting IOException due to pipe not being connected");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}