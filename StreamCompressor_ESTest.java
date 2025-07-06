package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutput;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Enumeration;
import java.util.zip.Deflater;
import org.apache.commons.compress.archivers.zip.StreamCompressor;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

/**
 * Test class for StreamCompressor.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class StreamCompressorTest {

    /**
     * Test StreamCompressor with an empty SequenceInputStream.
     */
    @Test(timeout = 4000)
    public void testEmptySequenceInputStream() throws Throwable {
        Enumeration<PushbackInputStream> enumeration = (Enumeration<PushbackInputStream>) mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration).hasMoreElements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        FileChannel fileChannel = mockFileInputStream.getChannel();
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create((SeekableByteChannel) fileChannel, deflater);
        streamCompressor.deflate(sequenceInputStream, 0);
        assertEquals(0L, streamCompressor.getBytesWrittenForLastEntry());
        assertEquals(0L, streamCompressor.getBytesRead());
    }

    /**
     * Test StreamCompressor with null ScatterGatherBackingStore.
     */
    @Test(timeout = 4000)
    public void testNullScatterGatherBackingStore() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        streamCompressor.reset();
        assertEquals(0L, streamCompressor.getBytesRead());
        assertEquals(0L, streamCompressor.getBytesWrittenForLastEntry());
    }

    /**
     * Test StreamCompressor with ScatterGatherBackingStore and invalid compression level.
     */
    @Test(timeout = 4000)
    public void testInvalidCompressionLevel() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((int) (byte)1, (ScatterGatherBackingStore) null);
        assertEquals(0L, streamCompressor.getTotalBytesWritten());
    }

    /**
     * Test StreamCompressor with file channel and deflater.
     */
    @Test(timeout = 4000)
    public void testFileChannelAndDeflater() throws Throwable {
        MockFile mockFile = new MockFile("M35HN>M");
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream(mockFile);
        FileChannel fileChannel = mockFileOutputStream.getChannel();
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create((SeekableByteChannel) fileChannel, deflater);
        streamCompressor.flushDeflater();
        assertEquals(8, deflater.getTotalOut());
        assertEquals(8L, mockFile.length());
    }

    /**
     * Test writeCounted method with PipedInputStream and PipedOutputStream.
     */
    @Test(timeout = 4000)
    public void testWriteCounted() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) pipedOutputStream);
        byte[] byteArray = new byte[4];
        streamCompressor.writeCounted(byteArray);
        assertEquals(4, pipedInputStream.available());
        assertEquals(4L, streamCompressor.getTotalBytesWritten());
    }

    /**
     * Test StreamCompressor with DataOutput and deflater.
     */
    @Test(timeout = 4000)
    public void testDataOutputAndDeflater() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, (byte)91);
        streamCompressor.deflate(pushbackInputStream, (byte)91);
        long written = streamCompressor.getTotalBytesWritten();
        assertEquals(4282505490L, streamCompressor.getCrc32());
        assertEquals(3L, written);
    }

    /**
     * Test bytes written for last entry.
     */
    @Test(timeout = 4000)
    public void testBytesWrittenForLastEntry() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, (byte)91);
        streamCompressor.deflate(pushbackInputStream, (byte)91);
        long written = streamCompressor.getBytesWrittenForLastEntry();
        assertEquals(4282505490L, streamCompressor.getCrc32());
        assertEquals(3L, written);
    }

    /**
     * Test writeCounted with null bytes.
     */
    @Test(timeout = 4000)
    public void testWriteCountedNullBytes() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        try {
            streamCompressor.writeCounted((byte[]) null, 8192, 8192);
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    /**
     * Test writeCounted with invalid indices.
     */
    @Test(timeout = 4000)
    public void testWriteCountedInvalidIndices() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);
        byte[] byteArray = new byte[7];
        try {
            streamCompressor.writeCounted(byteArray, (int) (byte)3, (int) (byte) (-29));
            fail("Expecting IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.io.ObjectOutputStream", e);
        }
    }

    /**
     * Test write method with null bytes.
     */
    @Test(timeout = 4000)
    public void testWriteNullBytes() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[1];
        try {
            streamCompressor.write(byteArray, 0, 0, 0);
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    /**
     * Test write method with invalid indices.
     */
    @Test(timeout = 4000)
    public void testWriteInvalidIndices() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[0];
        try {
            streamCompressor.write(byteArray, (-5758), (-5758), 477);
            fail("Expecting ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.util.zip.CRC32", e);
        }
    }

    /**
     * Test close and reset methods.
     */
    @Test(timeout = 4000)
    public void testCloseAndReset() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        streamCompressor.close();
        try {
            streamCompressor.reset();
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.zip.Deflater", e);
        }
    }

    /**
     * Test flushDeflater method.
     */
    @Test(timeout = 4000)
    public void testFlushDeflater() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        try {
            streamCompressor.flushDeflater();
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    /**
     * Test deflate method with invalid input.
     */
    @Test(timeout = 4000)
    public void testDeflateInvalidInput() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[7], (-1381), (byte)37);
        try {
            streamCompressor.deflate(byteArrayInputStream, 4096);
            fail("Expecting ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }

    /**
     * Test deflate method with unconnected PipedInputStream.
     */
    @Test(timeout = 4000)
    public void testDeflateUnconnectedPipedInputStream() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            streamCompressor.deflate(pipedInputStream, (-2716));
            fail("Expecting IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    /**
     * Test deflate method with unconnected PipedOutputStream.
     */
    @Test(timeout = 4000)
    public void testDeflateUnconnectedPipedOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) pipedOutputStream, deflater);
        try {
            streamCompressor.deflate();
            fail("Expecting IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    /**
     * Test close method with null output stream.
     */
    @Test(timeout = 4000)
    public void testCloseNullOutputStream() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) null, (Deflater) null);
        try {
            streamCompressor.close();
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor", e);
        }
    }

    /**
     * Test writeCounted method with null output stream.
     */
    @Test(timeout = 4000)
    public void testWriteCountedNullOutputStream() throws Throwable {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((OutputStream) null);
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) bufferedOutputStream, deflater);
        streamCompressor.deflate();
        streamCompressor.deflate();
        assertEquals(2L, deflater.getBytesWritten());
        assertEquals(2L, streamCompressor.getTotalBytesWritten());
    }

    /**
     * Test writeCounted method with invalid length.
     */
    @Test(timeout = 4000)
    public void testWriteCountedInvalidLength() throws Throwable {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((OutputStream) null);
        Deflater deflater = new Defler(1);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) bufferedOutputStream, deflater);
        byte[] byteArray = new byte[8];
        streamCompressor.writeCounted(byteArray, 0, 0);
        assertEquals(0L, streamCompressor.getTotalBytesWritten());
    }

    /**
     * Test flushDeflater method with unconnected PipedOutputStream.
     */
    @Test(timeout = 4000)
    public void testFlushDeflaterUnconnectedPipedOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        Deflater deflater = new Deflater(0);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) pipedOutputStream, deflater);
        try {
            streamCompressor.flushDeflater();
            fail("Expecting IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    /**
     * Test create method with invalid compression level.
     */
    @Test(timeout = 4000)
    public void testCreateInvalidCompressionLevel() throws Throwable {
        try {
            StreamCompressor.create(2075, (ScatterGatherBackingStore) null);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.zip.Deflater", e);
        }
    }

    /**
     * Test write method with invalid length.
     */
    @Test(timeout = 4000)
    public void testWriteInvalidLength() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[1];
        streamCompressor.write(byteArray, 0, 0, 8);
        assertEquals(0L, streamCompressor.getBytesRead());
    }

    /**
     * Test flushDeflater method with non-writable channel.
     */
    @Test(timeout = 4000)
    public void testFlushDeflaterNonWritableChannel() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        FileChannel fileChannel = mockFileInputStream.getChannel();
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create((SeekableByteChannel) fileChannel, deflater);
        try {
            streamCompressor.flushDeflater();
            fail("Expecting NonWritableChannelException");
        } catch (NonWritableChannelException e) {
            verifyException("org.evosuite.runtime.mock.java.io.EvoFileChannel", e);
        }
    }

    /**
     * Test getBytesRead method.
     */
    @Test(timeout = 4000)
    public void testGetBytesRead() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, (byte)91);
        streamCompressor.deflate(pushbackInputStream, (byte)91);
        long read = streamCompressor.getBytesRead();
        assertEquals(4282505490L, streamCompressor.getCrc32());
        assertEquals(3L, read);
    }

    /**
     * Test getBytesWrittenForLastEntry method.
     */
    @Test(timeout = 4000)
    public void testGetBytesWrittenForLastEntry() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);
        long written = streamCompressor.getBytesWrittenForLastEntry();
        assertEquals(0L, written);
    }

    /**
     * Test getTotalBytesWritten method.
     */
    @Test(timeout = 4000)
    public void testGetTotalBytesWritten() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);
        long written = streamCompressor.getTotalBytesWritten();
        assertEquals(0L, written);
    }

    /**
     * Test writeCounted method with null bytes.
     */
    @Test(timeout = 4000)
    public void testWriteCountedNullBytes() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[4];
        try {
            streamCompressor.writeCounted(byteArray);
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    /**
     * Test getCrc32 method.
     */
    @Test(timeout = 400