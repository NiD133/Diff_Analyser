package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.mockito.Mockito;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Enumeration;
import java.util.zip.Deflater;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Test suite for the {@link StreamCompressor} class.  These tests cover various scenarios
 * of creating and using StreamCompressor with different output types (ScatterGatherBackingStore,
 * OutputStream, DataOutput, SeekableByteChannel) and compression levels. The tests also
 * verify the correct handling of edge cases like null inputs, exceptions during compression,
 * and interactions with underlying streams/channels.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StreamCompressorTest {

    @Test(timeout = 4000)
    public void testDeflateEmptyStreamToSeekableByteChannel() throws Throwable {
        // Mock an empty Enumeration for SequenceInputStream
        Enumeration<PushbackInputStream> enumeration = Mockito.mock(Enumeration.class);
        when(enumeration.hasMoreElements()).thenReturn(false);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);

        // Create a SeekableByteChannel from a FileDescriptor.  Important for testing channel-based output.
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        FileChannel fileChannel = mockFileInputStream.getChannel();

        // Initialize Deflater and StreamCompressor
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create((SeekableByteChannel) fileChannel, deflater);

        // Deflate the empty stream. Checks for handling empty input without errors.
        streamCompressor.deflate(sequenceInputStream, 0);

        // Verify the results:  No bytes should have been written or read.
        assertEquals(0L, streamCompressor.getBytesWrittenForLastEntry());
        assertEquals(0L, streamCompressor.getBytesRead());
    }

    @Test(timeout = 4000)
    public void testResetScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null in this case).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Resets the StreamCompressor. Check reset functionality.
        streamCompressor.reset();

        // Verifies that byte counts are reset. Important for reusing compressor instances.
        assertEquals(0L, streamCompressor.getBytesRead());
        assertEquals(0L, streamCompressor.getBytesWrittenForLastEntry());
    }

    @Test(timeout = 4000)
    public void testCreateWithCompressionLevelAndScatterGatherBackingStore() throws Throwable {
        // Creates a StreamCompressor with a specified compression level and ScatterGatherBackingStore.
        StreamCompressor streamCompressor = StreamCompressor.create((int) (byte) 1, (ScatterGatherBackingStore) null);

        // Verifies that the total bytes written is initialized to zero.
        assertEquals(0L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testFlushDeflaterToSeekableByteChannel() throws Throwable {
        // Creates a mock file and output stream
        MockFile mockFile = new MockFile("M35HN>M");
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream(mockFile);
        FileChannel fileChannel = mockFileOutputStream.getChannel();

        // Initializes Deflater and StreamCompressor with the SeekableByteChannel.
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create((SeekableByteChannel) fileChannel, deflater);

        // Flushes the deflater.  Validates that data is actually written to the underlying file/channel.
        streamCompressor.flushDeflater();

        // Asserts the file length is as expected after deflation.  Checks actual compression occurred.
        assertEquals(8, deflater.getTotalOut()); // Check total output from deflater
        assertEquals(8L, mockFile.length()); // Check file size after flush
    }

    @Test(timeout = 4000)
    public void testWriteCountedToOutputStream() throws Throwable {
        // Creates a piped input/output stream pair for inter-thread communication.
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);

        // Initializes StreamCompressor with an OutputStream.
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) pipedOutputStream);

        // Writes a byte array to the stream.  Checks data actually makes it to output.
        byte[] byteArray = new byte[4];
        streamCompressor.writeCounted(byteArray);

        // Asserts that the correct number of bytes is available in the input stream and written in total.
        assertEquals(4, pipedInputStream.available()); // Checks bytes written to the piped output stream.
        assertEquals(4L, streamCompressor.getTotalBytesWritten()); // Checks total bytes written by the compressor
    }

    @Test(timeout = 4000)
    public void testDeflateAndGetTotalBytesWrittenToDataOutput() throws Throwable {
        // Initializes Deflater and output streams
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);

        // Creates a StreamCompressor with DataOutput and Deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);

        // Creates input streams
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, (byte) 91);

        // Deflates data from the input stream.
        streamCompressor.deflate(pushbackInputStream, (byte) 91);

        // Gets and asserts the total number of bytes written to the stream.
        long totalBytesWritten = streamCompressor.getTotalBytesWritten();

        // Asserts the CRC and total bytes written
        assertEquals(4282505490L, streamCompressor.getCrc32()); // Checks CRC calculation.
        assertEquals(3L, totalBytesWritten); // Checks total bytes written to the output.
    }

    @Test(timeout = 4000)
    public void testDeflateAndGetBytesWrittenForLastEntryToDataOutput() throws Throwable {
        // Initializes Deflater and output streams
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);

        // Creates a StreamCompressor with DataOutput and Deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);

        // Creates input streams
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, (byte) 91);

        // Deflates data from the input stream.
        streamCompressor.deflate(pushbackInputStream, (byte) 91);

        // Gets and asserts the number of bytes written for the last entry.
        long bytesWrittenForLastEntry = streamCompressor.getBytesWrittenForLastEntry();

        // Asserts the CRC and bytes written for last entry
        assertEquals(4282505490L, streamCompressor.getCrc32()); // Checks CRC calculation.
        assertEquals(3L, bytesWrittenForLastEntry); // Checks bytes written for the last entry
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testWriteCountedNullArrayToScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Tries to write a null byte array.  Verifies handling of null input.
        streamCompressor.writeCounted((byte[]) null, 8192, 8192);
    }

    @Test(timeout = 4000, expected = IndexOutOfBoundsException.class)
    public void testWriteCountedIndexOutOfBoundsToDataOutput() throws Throwable {
        // Initializes Deflater and output streams
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);

        // Creates a StreamCompressor with DataOutput and Deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);

        // Tries to write a byte array with invalid offset and length.  Checks for boundary errors.
        byte[] byteArray = new byte[7];
        streamCompressor.writeCounted(byteArray, (int) (byte) 3, (int) (byte) (-29));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testWriteNullScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Tries to write to the compressor with null backing store.  Ensures null handling.
        byte[] byteArray = new byte[1];
        streamCompressor.write(byteArray, 0, 0, 0);
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testWriteArrayIndexOutOfBoundsScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Tries to write to the compressor with invalid array index. Checks for boundary condition
        byte[] byteArray = new byte[0];
        streamCompressor.write(byteArray, (-5758), (-5758), 477);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testResetClosedScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Closes the compressor. Check that closing works.
        streamCompressor.close();

        // Attempts to reset a closed compressor. Checks for exception after closing.
        streamCompressor.reset();
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testFlushDeflaterScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Attempts to flush the deflater. Check what happens when there is nothing to deflate.
        streamCompressor.flushDeflater();
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testDeflateArrayIndexOutOfBounds() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Tries to deflate with invalid array index
        byte[] byteArray = new byte[7];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, (-1381), (byte) 37);

        // Attempts to deflate with invalid array bounds. Checks for exceptions.
        streamCompressor.deflate(byteArrayInputStream, 4096);
    }

    @Test(timeout = 4000, expected = IOException.class)
    public void testDeflatePipeNotConnected() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Creates a piped input stream
        PipedInputStream pipedInputStream = new PipedInputStream();

        // Attempts to deflate with unconnected pipe. Checks IO exception is thrown
        streamCompressor.deflate(pipedInputStream, (-2716));
    }

    @Test(timeout = 4000, expected = IOException.class)
    public void testDeflateNoSourcePipeNotConnected() throws Throwable {
        // Creates a piped output stream
        PipedOutputStream pipedOutputStream = new PipedOutputStream();

        // Initializes Deflater and StreamCompressor.
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) pipedOutputStream, deflater);

        // Attempts to deflate without a source. Checks IO exception is thrown
        streamCompressor.deflate();
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCloseNullOutputStream() throws Throwable {
        // Creates a StreamCompressor with null output stream and deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) null, (Deflater) null);

        // Attempts to close the stream
        streamCompressor.close();
    }

    @Test(timeout = 4000)
    public void testDeflateNoSourceOutputStream() throws Throwable {
        // Create a BufferedOutputStream that does nothing for testing
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((OutputStream) null);

        // Initializes Deflater and StreamCompressor.
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) bufferedOutputStream, deflater);

        // Deflates the stream twice.
        streamCompressor.deflate();
        streamCompressor.deflate();

        // Checks bytes written and deflater state.
        assertEquals(2L, deflater.getBytesWritten());
        assertEquals(2L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000)
    public void testWriteCountedZeroLengthOutputStream() throws Throwable {
        // Create a BufferedOutputStream that does nothing for testing
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream((OutputStream) null);

        // Initializes Deflater and StreamCompressor.
        Deflater deflater = new Deflater(1);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) bufferedOutputStream, deflater);

        // Write 0 bytes
        byte[] byteArray = new byte[8];
        streamCompressor.writeCounted(byteArray, 0, 0);

        // Checks that nothing was written
        assertEquals(0L, streamCompressor.getTotalBytesWritten());
    }

    @Test(timeout = 4000, expected = IOException.class)
    public void testFlushDeflaterPipeNotConnectedOutputStream() throws Throwable {
        // Creates a piped output stream
        PipedOutputStream pipedOutputStream = new PipedOutputStream();

        // Initializes Deflater and StreamCompressor.
        Deflater deflater = new Deflater(0);
        StreamCompressor streamCompressor = StreamCompressor.create((OutputStream) pipedOutputStream, deflater);

        // Attempts to flush the deflater with unconnected stream
        streamCompressor.flushDeflater();
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testCreateWithInvalidCompressionLevel() throws Throwable {
        // Attempts to create a stream compressor with invalid compression level.
        StreamCompressor.create(2075, (ScatterGatherBackingStore) null);
    }

    @Test(timeout = 4000)
    public void testWriteZeroLengthScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Writes 0 bytes to the compressor
        byte[] byteArray = new byte[1];
        streamCompressor.write(byteArray, 0, 0, 8);

        // Checks bytes read.
        assertEquals(0L, streamCompressor.getBytesRead());
    }

    @Test(timeout = 4000, expected = NonWritableChannelException.class)
    public void testFlushDeflaterNonWritableChannel() throws Throwable {
        // Create a mock file and output stream
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        FileChannel fileChannel = mockFileInputStream.getChannel();

        // Initializes Deflater and StreamCompressor with the SeekableByteChannel.
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create((SeekableByteChannel) fileChannel, deflater);

        // Attempts to flush the deflater with non-writable channel
        streamCompressor.flushDeflater();
    }

    @Test(timeout = 4000)
    public void testGetBytesReadDataOutput() throws Throwable {
        // Initializes Deflater and output streams
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);

        // Creates a StreamCompressor with DataOutput and Deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);

        // Creates input streams
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, (byte) 91);

        // Deflates data from the input stream.
        streamCompressor.deflate(pushbackInputStream, (byte) 91);

        // Gets and asserts the total number of bytes read to the stream.
        long bytesRead = streamCompressor.getBytesRead();

        // Asserts the CRC and total bytes read
        assertEquals(4282505490L, streamCompressor.getCrc32()); // Checks CRC calculation.
        assertEquals(3L, bytesRead); // Checks bytes read from input.
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDeflateNoSourceClosedScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Closes the compressor.
        streamCompressor.close();

        // Attempts to deflate without a source. Check for exception being thrown.
        streamCompressor.deflate();
    }

    @Test(timeout = 4000)
    public void testGetTotalBytesWrittenDataOutput() throws Throwable {
        // Initializes Deflater and output streams
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);

        // Creates a StreamCompressor with DataOutput and Deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);

        // Gets and asserts the total number of bytes written to the stream.
        long totalBytesWritten = streamCompressor.getTotalBytesWritten();

        // Asserts the total bytes written
        assertEquals(0L, totalBytesWritten);
    }

    @Test(timeout = 4000)
    public void testGetBytesWrittenForLastEntryDataOutput() throws Throwable {
        // Initializes Deflater and output streams
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);

        // Creates a StreamCompressor with DataOutput and Deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);

        // Gets and asserts the total number of bytes written for the last entry.
        long bytesWrittenForLastEntry = streamCompressor.getBytesWrittenForLastEntry();

        // Asserts the total bytes written
        assertEquals(0L, bytesWrittenForLastEntry);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testWriteCountedScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Writes bytes to the compressor. Check that an exception is thrown.
        byte[] byteArray = new byte[4];
        streamCompressor.writeCounted(byteArray);
    }

    @Test(timeout = 4000)
    public void testGetCrc32ScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Gets and asserts the CRC value.
        long crc = streamCompressor.getCrc32();

        // Asserts the total bytes written
        assertEquals(0L, crc);
    }

    @Test(timeout = 4000)
    public void testGetBytesReadDataOutputDefault() throws Throwable {
        // Initializes Deflater and output streams
        Deflater deflater = new Deflater();
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("org.apache.commons.compress.archivers.zip.StreamCompressor$DataOutputCompressor", false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(mockFileOutputStream);

        // Creates a StreamCompressor with DataOutput and Deflater.
        StreamCompressor streamCompressor = StreamCompressor.create((DataOutput) objectOutputStream, deflater);

        // Gets and asserts the total number of bytes read.
        long bytesRead = streamCompressor.getBytesRead();

        // Asserts the total bytes written
        assertEquals(0L, bytesRead);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDeflateByteArrayInputStreamScatterGatherBackingStoreCompressor() throws Throwable {
        // Creates a StreamCompressor using a ScatterGatherBackingStore (null).
        StreamCompressor streamCompressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Writes bytes to the compressor. Check that an exception is thrown.
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        // Attempts to deflate with null backing store.
        streamCompressor.deflate(byteArrayInputStream, 8);
    }
}