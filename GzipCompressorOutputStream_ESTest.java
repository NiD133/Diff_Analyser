package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for GzipCompressorOutputStream.
 *
 * Notes:
 * - Avoids EvoSuite-specific mocks.
 * - Uses simple helper OutputStream implementations where needed.
 * - Verifies bytes structurally (magic bytes, flags, trailer) rather than using String toString().
 */
public class GzipCompressorOutputStreamTest {

    // GZIP constants
    private static final int GZIP_MAGIC_0 = 0x1f;
    private static final int GZIP_MAGIC_1 = 0x8b;
    private static final int CM_DEFLATE = 8;
    private static final int FLG_FHCRC = 0x02;
    private static final int FLG_FEXTRA = 0x04;

    @Test
    public void writesGzipHeaderOnConstruction_withDefaultParameters() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new GzipCompressorOutputStream(out); // header is written in constructor

        byte[] bytes = out.toByteArray();
        assertEquals("Default GZIP header size", 10, bytes.length);
        assertEquals(GZIP_MAGIC_0, Byte.toUnsignedInt(bytes[0]));
        assertEquals(GZIP_MAGIC_1, Byte.toUnsignedInt(bytes[1]));
        assertEquals("Compression method must be deflate", CM_DEFLATE, Byte.toUnsignedInt(bytes[2]));
        assertEquals("Default flags should be 0", 0, Byte.toUnsignedInt(bytes[3]));
    }

    @Test
    public void writeEmptyArray_keepsOnlyHeader() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        gzip.write(new byte[0]);
        assertEquals(10, out.size()); // only the header so far
    }

    @Test
    public void finishWithNoData_writesValidTrailer_crcAndSizeAreZero() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        gzip.finish();

        byte[] bytes = out.toByteArray();
        assertTrue("Header (10) + trailer (8) at least", bytes.length >= 18);

        int crcLE = readLittleEndianInt(bytes, bytes.length - 8);
        int isizeLE = readLittleEndianInt(bytes, bytes.length - 4);
        assertEquals(0, crcLE);
        assertEquals(0, isizeLE);
    }

    @Test
    public void finishAfterWritingData_trailerMatchesCrc32AndUncompressedSize() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        byte[] data = "hello world".getBytes(StandardCharsets.US_ASCII);
        gzip.write(data);
        gzip.finish();

        byte[] bytes = out.toByteArray();

        CRC32 crc = new CRC32();
        crc.update(data);
        long expectedCrc = crc.getValue();
        long expectedISize = data.length & 0xFFFFFFFFL;

        long actualCrc = Integer.toUnsignedLong(readLittleEndianInt(bytes, bytes.length - 8));
        long actualISize = Integer.toUnsignedLong(readLittleEndianInt(bytes, bytes.length - 4));

        assertEquals(expectedCrc, actualCrc);
        assertEquals(expectedISize, actualISize);
    }

    @Test
    public void headerWithHeaderCRC_setsFlagAndExtendsHeader() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setHeaderCRC(true);

        new GzipCompressorOutputStream(out, params);

        byte[] bytes = out.toByteArray();
        assertEquals("Header with FHCRC should be 12 bytes", 12, bytes.length);
        int flags = Byte.toUnsignedInt(bytes[3]);
        assertTrue((flags & FLG_FHCRC) != 0);
    }

    @Test
    public void headerFlagsReflectExtraField() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setExtraField(new ExtraField());

        new GzipCompressorOutputStream(out, params);

        byte[] bytes = out.toByteArray();
        assertEquals(GZIP_MAGIC_0, Byte.toUnsignedInt(bytes[0]));
        assertEquals(GZIP_MAGIC_1, Byte.toUnsignedInt(bytes[1]));
        int flags = Byte.toUnsignedInt(bytes[3]);
        assertTrue("FEXTRA flag should be set", (flags & FLG_FEXTRA) != 0);
    }

    @Test
    public void writeZeroLengthRange_isNoOp() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        byte[] buf = new byte[6];
        gzip.write(buf, 3, 0); // legal: offset inside array, length 0

        assertEquals("No data written beyond header", 10, out.size());
    }

    @Test
    public void writeNullBuffer_throwsNPE() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        assertThrows(NullPointerException.class, () -> gzip.write((byte[]) null));
        assertThrows(NullPointerException.class, () -> gzip.write(null, 0, 0));
    }

    @Test
    public void writeWithOutOfBoundsOffsets_throwsArrayIndexOutOfBounds() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        byte[] empty = new byte[0];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> gzip.write(empty, 1, 1));
    }

    @Test
    public void writeAfterClose_throwsIOException() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        gzip.close();
        assertThrows(IOException.class, () -> gzip.write(1));
        assertThrows(IOException.class, () -> gzip.write(new byte[0]));
        assertThrows(IOException.class, () -> gzip.write(new byte[1], 0, 1));
    }

    @Test
    public void closeIsIdempotent_andSetsClosedFlag() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(out);

        gzip.close();
        gzip.close(); // should not throw
        assertTrue("Stream should report closed", gzip.isClosed());
    }

    @Test
    public void constructorRejectsNullOutputStream() {
        assertThrows(NullPointerException.class, () -> new GzipCompressorOutputStream(null));
    }

    @Test
    public void constructorRejectsNullParameters() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertThrows(NullPointerException.class, () -> new GzipCompressorOutputStream(out, null));
    }

    @Test
    public void constructorWithUnconnectedPipedOutputStream_throwsIOException() {
        PipedOutputStream unconnected = new PipedOutputStream();
        assertThrows(IOException.class, () -> new GzipCompressorOutputStream(unconnected));
    }

    @Test
    public void finishPropagatesIOException_whenUnderlyingStreamIsClosed() throws Exception {
        StrictOutputStream strict = new StrictOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(strict);

        strict.close(); // simulate external close
        assertThrows(IOException.class, gzip::finish);
    }

    @Test
    public void closePropagatesIOException_whenUnderlyingStreamIsClosed() throws Exception {
        StrictOutputStream strict = new StrictOutputStream();
        GzipCompressorOutputStream gzip = new GzipCompressorOutputStream(strict);

        strict.close(); // simulate external close
        assertThrows(IOException.class, gzip::close);
    }

    @Test
    public void constructorFailsIfUnderlyingStreamAlreadyClosed() {
        StrictOutputStream strict = new StrictOutputStream();
        // Close before passing to constructor, header write should fail
        try {
            strict.close();
        } catch (IOException e) {
            fail("Unexpected IOException closing StrictOutputStream for setup");
        }
        assertThrows(IOException.class, () -> new GzipCompressorOutputStream(strict));
    }

    // Helpers

    private static int readLittleEndianInt(byte[] array, int offset) {
        int b0 = Byte.toUnsignedInt(array[offset]);
        int b1 = Byte.toUnsignedInt(array[offset + 1]);
        int b2 = Byte.toUnsignedInt(array[offset + 2]);
        int b3 = Byte.toUnsignedInt(array[offset + 3]);
        return (b0) | (b1 << 8) | (b2 << 16) | (b3 << 24);
    }

    /**
     * OutputStream that throws IOException on write/flush after it has been closed.
     * Useful to simulate underlying stream failures deterministically.
     */
    private static final class StrictOutputStream extends OutputStream {
        private boolean closed;

        @Override
        public void write(int b) throws IOException {
            ensureOpen();
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            ensureOpen();
        }

        @Override
        public void flush() throws IOException {
            ensureOpen();
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        private void ensureOpen() throws IOException {
            if (closed) {
                throw new IOException("Underlying stream is closed");
            }
        }
    }
}