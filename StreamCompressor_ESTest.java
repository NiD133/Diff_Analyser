package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.parallel.ScatterGatherBackingStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link StreamCompressor}.
 */
public class StreamCompressorTest {

    // =================================================================
    // Initial State and Reset Tests
    // =================================================================

    @Test
    public void newlyCreatedCompressorShouldHaveZeroedCounters() {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);

        // Assert
        assertEquals("Initial CRC should be 0", 0L, compressor.getCrc32());
        assertEquals("Initial bytes read should be 0", 0L, compressor.getBytesRead());
        assertEquals("Initial bytes written for last entry should be 0", 0L, compressor.getBytesWrittenForLastEntry());
        assertEquals("Initial total bytes written should be 0", 0L, compressor.getTotalBytesWritten());
    }

    @Test
    public void resetShouldClearPerEntryCounters() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        byte[] data = {1, 2, 3};
        compressor.write(data, 0, data.length, ZipEntry.DEFLATED);
        assertTrue("Pre-condition: Total bytes written should be > 0", compressor.getTotalBytesWritten() > 0);

        // Act
        compressor.reset();

        // Assert
        assertEquals("CRC should be 0 after reset", 0L, compressor.getCrc32());
        assertEquals("Bytes read should be 0 after reset", 0L, compressor.getBytesRead());
        assertEquals("Bytes written for last entry should be 0 after reset", 0L, compressor.getBytesWrittenForLastEntry());
        // Note: getTotalBytesWritten is cumulative across entries and should not be reset.
        assertTrue("Total bytes written should be preserved after reset", compressor.getTotalBytesWritten() > 0);
    }

    // =================================================================
    // Write Operation Tests
    // =================================================================

    @Test
    public void writeCountedShouldUpdateTotalBytesWritten() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        byte[] data = {1, 2, 3, 4, 5};

        // Act & Assert for full array write
        compressor.writeCounted(data);
        assertEquals(5L, compressor.getTotalBytesWritten());
        assertEquals(5, baos.size());

        // Act & Assert for partial array write
        compressor.writeCounted(data, 1, 2); // write bytes {2, 3}
        assertEquals(7L, compressor.getTotalBytesWritten());
        assertEquals(7, baos.size());
    }

    @Test
    public void writeShouldUpdateCrcAndBytesWritten() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        byte[] data = {'a'}; // A single byte to check CRC

        // Act: Using STORED method writes uncompressed data, making byte counts predictable.
        long bytesWritten = compressor.write(data, 0, 1, ZipEntry.STORED);

        // Assert
        assertEquals(1L, bytesWritten);
        assertEquals(1L, compressor.getTotalBytesWritten());
        assertEquals("For STORED, bytesRead should equal bytesWritten", 1L, compressor.getBytesRead());
        assertEquals("CRC32 of 'a' should be correct", 0x765E7680L, compressor.getCrc32());
    }

    @Test
    public void writeShouldDoNothingWhenLengthIsZero() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        byte[] data = {1, 2, 3};

        // Act
        long bytesWritten = compressor.write(data, 0, 0, ZipEntry.DEFLATED);

        // Assert
        assertEquals(0L, bytesWritten);
        assertEquals(0L, compressor.getTotalBytesWritten());
        assertEquals(0L, compressor.getBytesRead());
        assertEquals(0L, compressor.getCrc32());
    }

    // =================================================================
    // Deflate Operation Tests
    // =================================================================

    @Test
    public void deflateShouldProcessZeroBytesForEmptyInputStream() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);

        // Act
        compressor.deflate(emptyStream, ZipEntry.DEFLATED);

        // Assert
        assertEquals(0L, compressor.getBytesRead());
        // Deflating an empty stream still writes an empty deflate block to the output.
        assertTrue("Bytes written should be > 0 for empty deflate block", compressor.getBytesWrittenForLastEntry() > 0);
    }

    @Test
    public void deflateEmptyShouldWriteEndOfStreamMarker() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        StreamCompressor compressor = StreamCompressor.create(new DataOutputStream(baos), deflater);

        // Act
        compressor.deflate(); // Finishes the stream

        // Assert
        // An empty deflater stream produces a 2-byte end-of-stream marker.
        assertEquals(2, deflater.getTotalOut());
        assertEquals(2L, compressor.getTotalBytesWritten());
        assertEquals(2, baos.size());
    }

    @Test
    public void flushDeflaterShouldWriteSyncFlushMarker() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);

        // Act
        compressor.flushDeflater();

        // Assert
        // A SYNC_FLUSH on an empty deflater writes a 2-byte marker (0x00 0x00).
        assertEquals(2L, compressor.getBytesWrittenForLastEntry());
        assertEquals(2, baos.size());
    }

    @Test
    public void closeShouldNotAffectCounters() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);

        // Act
        compressor.close();

        // Assert
        assertEquals(0L, compressor.getBytesRead());
        assertEquals(0L, compressor.getTotalBytesWritten());
    }

    // =================================================================
    // Exception Tests
    // =================================================================

    @Test(expected = IllegalArgumentException.class)
    public void createShouldThrowIllegalArgumentExceptionForInvalidCompressionLevel() {
        // Act
        StreamCompressor.create(-5, (ScatterGatherBackingStore) null);
    }

    @Test(expected = IOException.class)
    public void deflateShouldThrowIOExceptionForInvalidCompressionMethod() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        int invalidMethod = -99;

        // Act
        compressor.deflate(inputStream, invalidMethod);
    }

    @Test(expected = NullPointerException.class)
    public void writeCountedShouldThrowNullPointerExceptionForNullData() throws IOException {
        // Arrange
        StreamCompressor compressor = StreamCompressor.create((OutputStream) null);

        // Act
        compressor.writeCounted(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void writeCountedShouldThrowIndexOutOfBoundsExceptionForInvalidLength() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        byte[] data = new byte[5];

        // Act
        compressor.writeCounted(data, 0, 10); // length=10 > data.length
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void writeCountedShouldThrowIndexOutOfBoundsExceptionForNegativeOffset() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos);
        byte[] data = new byte[5];

        // Act
        compressor.writeCounted(data, -1, 2); // offset=-1
    }

    @Test(expected = NullPointerException.class)
    public void scatterGatherCompressorShouldThrowNullPointerExceptionWhenBackingStoreIsNull() throws IOException {
        // Arrange: This compressor type requires a non-null backing store for write operations.
        StreamCompressor compressor = StreamCompressor.create(Deflater.DEFAULT_COMPRESSION, (ScatterGatherBackingStore) null);
        byte[] data = {1, 2, 3};

        // Act
        compressor.writeCounted(data);
    }

    @Test(expected = NullPointerException.class)
    public void deflateShouldThrowNullPointerExceptionWhenDeflaterIsNull() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamCompressor compressor = StreamCompressor.create(baos, null); // Pass null deflater

        // Act
        compressor.deflate();
    }

    // --- IO Exception Tests for Unconnected Pipes ---

    @Test(expected = IOException.class)
    public void writeCountedShouldThrowIOExceptionOnUnconnectedPipe() throws IOException {
        // Arrange
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(unconnectedPipe);
        byte[] data = {1, 2, 3};

        // Act
        compressor.writeCounted(data);
    }

    @Test(expected = IOException.class)
    public void flushDeflaterShouldThrowIOExceptionOnUnconnectedPipe() throws IOException {
        // Arrange
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(unconnectedPipe);

        // Act
        compressor.flushDeflater();
    }

    @Test(expected = IOException.class)
    public void deflateEmptyShouldThrowIOExceptionOnUnconnectedPipe() throws IOException {
        // Arrange
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(unconnectedPipe);

        // Act
        compressor.deflate();
    }
}