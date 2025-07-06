package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.NonWritableChannelException;
import java.util.Enumeration;
import java.util.zip.Deflater;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StreamCompressorTest extends StreamCompressor_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final int BUFFER_SIZE = 4;
    private static final int LARGE_BUFFER_SIZE = 8192;
    private static final int INVALID_INDEX = -5758;
    private static final int INVALID_LENGTH = -29;
    private static final int INVALID_METHOD = 2075;

    @Test(timeout = TIMEOUT)
    public void testDeflateWithEmptyEnumeration() throws Throwable {
        Enumeration<PushbackInputStream> enumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration).hasMoreElements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        FileChannel fileChannel = mockFileInputStream.getChannel();
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create(fileChannel, deflater);
        streamCompressor.deflate(sequenceInputStream, 0);
        assertEquals(0L, streamCompressor.getBytesWrittenForLastEntry());
        assertEquals(0L, streamCompressor.getBytesRead());
    }

    @Test(timeout = TIMEOUT)
    public void testResetStreamCompressor() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        streamCompressor.reset();
        assertEquals(0L, streamCompressor.getBytesRead());
        assertEquals(0L, streamCompressor.getBytesWrittenForLastEntry());
    }

    @Test(timeout = TIMEOUT)
    public void testCreateStreamCompressorWithByteLevel() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((int) (byte) 1, (ScatterGatherBackingStore) null);
        assertEquals(0L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testFlushDeflater() throws Throwable {
        MockFile mockFile = new MockFile("M35HN>M");
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream(mockFile);
        FileChannel fileChannel = mockFileOutputStream.getChannel();
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create(fileChannel, deflater);
        streamCompressor.flushDeflater();
        assertEquals(8, deflater.getTotalOut());
        assertEquals(8L, mockFile.length());
    }

    @Test(timeout = TIMEOUT)
    public void testWriteCountedBytes() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
        StreamCompressor streamCompressor = StreamCompressor.create(pipedOutputStream);
        byte[] byteArray = new byte[BUFFER_SIZE];
        streamCompressor.writeCounted(byteArray);
        assertEquals(BUFFER_SIZE, pipedInputStream.available());
        assertEquals((long) BUFFER_SIZE, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testDeflateWithPushbackInputStream() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create(objectOutputStream, deflater);
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, (byte) 91);
        streamCompressor.deflate(pushbackInputStream, (byte) 91);
        assertEquals(4282505490L, streamCompressor.getCrc32());
        assertEquals(3L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testWriteCountedWithNullArray() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        try {
            streamCompressor.writeCounted(null, LARGE_BUFFER_SIZE, LARGE_BUFFER_SIZE);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testWriteCountedWithInvalidLength() {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create(objectOutputStream, deflater);
        byte[] byteArray = new byte[7];
        try {
            streamCompressor.writeCounted(byteArray, 3, INVALID_LENGTH);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.io.ObjectOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testWriteWithNullArray() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[1];
        try {
            streamCompressor.write(byteArray, 0, 0, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testWriteWithInvalidIndex() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[0];
        try {
            streamCompressor.write(byteArray, INVALID_INDEX, INVALID_INDEX, 477);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.util.zip.CRC32", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testResetAfterClose() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        streamCompressor.close();
        try {
            streamCompressor.reset();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.zip.Deflater", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testFlushDeflaterWithNullBackingStore() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        try {
            streamCompressor.flushDeflater();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testDeflateWithInvalidInputStream() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[7];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, -1381, 37);
        try {
            streamCompressor.deflate(byteArrayInputStream, 4096);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testDeflateWithUnconnectedPipe() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            streamCompressor.deflate(pipedInputStream, -2716);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testDeflateWithUnconnectedPipedOutputStream() {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create(pipedOutputStream, deflater);
        try {
            streamCompressor.deflate();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCloseWithNullOutputStream() {
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) null, (Deflater) null);
        try {
            streamCompressor.close();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testDeflateTwice() throws Throwable {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((OutputStream) null);
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create(bufferedOutputStream, deflater);
        streamCompressor.deflate();
        streamCompressor.deflate();
        assertEquals(2L, deflater.getBytesWritten());
        assertEquals(2L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testWriteCountedWithZeroLength() throws Throwable {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((OutputStream) null);
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create(bufferedOutputStream, deflater);
        byte[] byteArray = new byte[8];
        streamCompressor.writeCounted(byteArray, 0, 0);
        assertEquals(0L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testFlushDeflaterWithUnconnectedPipe() {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        Deflater deflater = new Deflater(0);
        StreamCompressor streamCompressor = StreamCompressor.create(pipedOutputStream, deflater);
        try {
            streamCompressor.flushDeflater();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCreateWithInvalidCompressionLevel() {
        try {
            StreamCompressor.create(INVALID_METHOD, (ScatterGatherBackingStore) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.zip.Deflater", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testWriteWithZeroLength() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[1];
        streamCompressor.write(byteArray, 0, 0, 8);
        assertEquals(0L, streamCompressor.getBytesRead());
    }

    @Test(timeout = TIMEOUT)
    public void testFlushDeflaterWithNonWritableChannel() {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        FileChannel fileChannel = mockFileInputStream.getChannel();
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create(fileChannel, deflater);
        try {
            streamCompressor.flushDeflater();
            fail("Expecting exception: NonWritableChannelException");
        } catch (NonWritableChannelException e) {
            verifyException("org.evosuite.runtime.mock.java.io.EvoFileChannel", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetTotalBytesWritten() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create(objectOutputStream, deflater);
        assertEquals(0L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = TIMEOUT)
    public void testGetBytesWrittenForLastEntry() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create(objectOutputStream, deflater);
        assertEquals(0L, streamCompressor.getBytesWrittenForLastEntry());
    }

    @Test(timeout = TIMEOUT)
    public void testWriteCountedWithNullBackingStore() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[BUFFER_SIZE];
        try {
            streamCompressor.writeCounted(byteArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetCrc32() throws Throwable {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        assertEquals(0L, streamCompressor.getCrc32());
    }

    @Test(timeout = TIMEOUT)
    public void testGetBytesRead() throws Throwable {
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);
        StreamCompressor streamCompressor = StreamCompressor.create(objectOutputStream, deflater);
        assertEquals(0L, streamCompressor.getBytesRead());
    }

    @Test(timeout = TIMEOUT)
    public void testDeflateWithNullBackingStore() {
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try {
            streamCompressor.deflate(byteArrayInputStream, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.StreamCompressor$ScatterGatherBackingStoreCompressor", e);
        }
    }
}