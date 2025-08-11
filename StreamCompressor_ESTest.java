package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Readable tests for StreamCompressor focused on:
 * - basic state and counters,
 * - writing raw (STORED) data,
 * - deflating from an InputStream,
 * - argument validation and error handling.
 *
 * These tests deliberately avoid complicated stream setups and mocking
 * to keep intent clear and assertions straightforward.
 */
public class StreamCompressorReadableTest {

    /**
     * Helper that creates a compressor writing to a ByteArrayOutputStream so we
     * can easily assert what was written without relying on pipes or the file system.
     */
    private static class InMemoryCompressor {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final StreamCompressor compressor = StreamCompressor.create(out);
    }

    @Test
    public void initialState_isZeroed() {
        InMemoryCompressor h = new InMemoryCompressor();

        assertEquals("total bytes written", 0L, h.compressor.getTotalBytesWritten());
        assertEquals("bytes written for last entry", 0L, h.compressor.getBytesWrittenForLastEntry());
        assertEquals("bytes read", 0L, h.compressor.getBytesRead());
        assertEquals("CRC", 0L, h.compressor.getCrc32());
    }

    @Test
    public void writeCounted_writesExactlyRequestedBytes_andUpdatesTotal() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        byte[] data = {1, 2, 3, 4, 5};
        h.compressor.writeCounted(data, 1, 3); // write {2,3,4}

        assertArrayEquals("bytes written to underlying stream",
                new byte[]{2, 3, 4}, h.out.toByteArray());
        assertEquals("total bytes written", 3L, h.compressor.getTotalBytesWritten());
    }

    @Test
    public void writeStored_updatesCRC_andWritesRawBytes() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        byte[] data = {10, 20, 30, 40};
        int offset = 1, length = 2; // write {20,30}
        long written = h.compressor.write(data, offset, length, ZipEntry.STORED);

        // Underlying output receives the raw bytes for STORED entries.
        assertArrayEquals(new byte[]{20, 30}, h.out.toByteArray());
        assertEquals("bytes reported as written", 2L, written);
        assertEquals("total bytes written", 2L, h.compressor.getTotalBytesWritten());

        // CRC-32 should reflect the written slice.
        CRC32 expected = new CRC32();
        expected.update(data, offset, length);
        assertEquals("CRC of last entry", expected.getValue(), h.compressor.getCrc32());
    }

    @Test
    public void deflate_emptyInput_producesNoOutput_andDoesNotAdvanceCounters() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        ByteArrayInputStream empty = new ByteArrayInputStream(new byte[0]);
        h.compressor.deflate(empty, ZipEntry.DEFLATED);

        assertEquals("no bytes written for last entry", 0L, h.compressor.getBytesWrittenForLastEntry());
        assertEquals("no bytes read", 0L, h.compressor.getBytesRead());
        assertEquals("no output produced", 0, h.out.size());
    }

    @Test
    public void reset_clearsPerEntryState() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        // Write a small STORED slice to create non-zero per-entry state.
        byte[] oneZero = {0};
        h.compressor.write(oneZero, 0, 1, ZipEntry.STORED);

        // Sanity checks: we now have non-zero state
        long totalBefore = h.compressor.getTotalBytesWritten();
        long crcBefore = h.compressor.getCrc32();
        // Perform reset
        h.compressor.reset();

        // After reset, per-entry state is cleared. Total bytes written is an accumulation; do not assert it resets.
        assertEquals("CRC reset", 0L, h.compressor.getCrc32());
        assertEquals("bytes read reset", 0L, h.compressor.getBytesRead());
        assertEquals("bytes written for last entry reset", 0L, h.compressor.getBytesWrittenForLastEntry());

        // The cumulative total should remain as-is (documented behavior in Commons Compress).
        assertEquals("total bytes written unchanged by reset", totalBefore, h.compressor.getTotalBytesWritten());

        // CRC was non-zero before reset to ensure the assertion is meaningful.
        if (crcBefore == 0L) {
            fail("Expected non-zero CRC before reset to make the test meaningful");
        }
    }

    @Test
    public void writeCounted_nullArray_throwsNPE() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        try {
            h.compressor.writeCounted(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void write_nullArray_throwsNPE() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        try {
            h.compressor.write(null, 0, 1, ZipEntry.STORED);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void writeCounted_offsetLengthOutOfBounds_throwsIndexOutOfBounds() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        byte[] data = {1, 2};
        try {
            h.compressor.writeCounted(data, 1, 8);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }
    }

    @Test
    public void create_withInvalidCompressionLevel_throwsIAE() {
        try {
            StreamCompressor.create(-1, (ScatterGatherBackingStore) null);
            fail("Expected IllegalArgumentException for invalid compression level");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    @Test
    public void close_isIdempotent() throws Exception {
        InMemoryCompressor h = new InMemoryCompressor();

        h.compressor.close();
        // Closing again should not throw; idempotency is expected for Closeable implementations.
        h.compressor.close();
    }
}