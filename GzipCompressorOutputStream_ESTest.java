/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

/**
 * Understandable and maintainable tests for {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamTest {

    // region Constructor Tests

    @Test
    public void constructor_shouldWriteDefaultHeader() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] expectedHeader = {
            (byte) 0x1f, (byte) 0x8b, // Magic number
            (byte) 0x08,              // Compression method (DEFLATE)
            (byte) 0x00,              // Flags
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Modification time (unused)
            (byte) 0x00,              // Extra flags
            (byte) 0xff               // Operating system (UNKNOWN)
        };

        // Act
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);

        // Assert
        assertArrayEquals("Default GZIP header is incorrect", expectedHeader, baos.toByteArray());
        gcos.close();
    }

    @Test(expected = NullPointerException.class)
    public void constructor_shouldThrowNullPointerException_forNullOutputStream() throws IOException {
        // Act
        new GzipCompressorOutputStream(null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_shouldThrowNullPointerException_forNullGzipParameters() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Act
        new GzipCompressorOutputStream(baos, null);
    }

    @Test(expected = IOException.class)
    public void constructor_shouldPropagateIOExceptionFromUnderlyingStream() throws IOException {
        // Arrange
        OutputStream failingStream = new FailingOutputStream(new IOException("Test Exception"));

        // Act
        new GzipCompressorOutputStream(failingStream);
    }

    // endregion

    // region GzipParameters Tests

    @Test
    public void constructor_shouldApplyCompressionLevelParameter() throws IOException {
        // Arrange
        ByteArrayOutputStream baosFast = new ByteArrayOutputStream();
        GzipParameters paramsFast = new GzipParameters();
        paramsFast.setCompressionLevel(Deflater.BEST_SPEED);

        ByteArrayOutputStream baosBest = new ByteArrayOutputStream();
        GzipParameters paramsBest = new GzipParameters();
        paramsBest.setCompressionLevel(Deflater.BEST_COMPRESSION);

        // Act
        GzipCompressorOutputStream gcosFast = new GzipCompressorOutputStream(baosFast, paramsFast);
        GzipCompressorOutputStream gcosBest = new GzipCompressorOutputStream(baosBest, paramsBest);

        // Assert
        // XFL flag (byte 8) indicates compressor speed. 4=fastest, 2=best.
        assertEquals("XFL byte for BEST_SPEED should be 4", 4, baosFast.toByteArray()[8]);
        assertEquals("XFL byte for BEST_COMPRESSION should be 2", 2, baosBest.toByteArray()[8]);

        gcosFast.close();
        gcosBest.close();
    }

    @Test
    public void constructor_shouldApplyHeaderCrcParameter() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setHeaderCRC(true);

        // Act
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos, params);

        // Assert
        // Header CRC adds 2 bytes to the header and sets the FHCRC flag (bit 1) in the FLG byte (offset 3).
        assertEquals("Header with CRC should be 12 bytes long", 12, baos.size());
        assertEquals("FLG byte should have FHCRC bit set", 0x02, baos.toByteArray()[3]);

        gcos.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldThrowIllegalArgumentException_forInvalidDeflateStrategy() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipParameters params = new GzipParameters();
        params.setDeflateStrategy(999); // Invalid strategy

        // Act
        new GzipCompressorOutputStream(baos, params);
    }

    // endregion

    // region Write Method Tests

    @Test
    public void write_shouldCompressDataAndWriteCorrectTrailer() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);
        byte[] data = "test".getBytes("UTF-8");

        // Act
        gcos.write(data);
        gcos.finish();

        // Assert
        byte[] compressedData = baos.toByteArray();
        assertTrue("Compressed output should be larger than just header and trailer", compressedData.length > 18);

        // Trailer is the last 8 bytes: 4 for CRC32, 4 for input size.
        ByteBuffer trailer = ByteBuffer.wrap(compressedData, compressedData.length - 8, 8);
        trailer.order(ByteOrder.LITTLE_ENDIAN);

        CRC32 crc = new CRC32();
        crc.update(data);
        long expectedCrc = crc.getValue();
        long expectedISize = data.length;

        assertEquals("Trailer CRC32 is incorrect", expectedCrc, trailer.getInt() & 0xffffffffL);
        assertEquals("Trailer ISIZE is incorrect", expectedISize, trailer.getInt() & 0xffffffffL);
    }

    @Test
    public void write_shouldDoNothing_forZeroLengthWrite() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);
        byte[] header = baos.toByteArray();
        assertEquals("GZIP header should be 10 bytes", 10, header.length);

        // Act
        gcos.write(new byte[]{1, 2, 3}, 1, 0); // Write zero bytes
        gcos.write(new byte[0]); // Write an empty array

        // Assert
        assertArrayEquals("Stream content should not change after writing zero bytes",
                          header, baos.toByteArray());
        gcos.close();
    }

    @Test(expected = IOException.class)
    public void write_shouldThrowIOException_whenStreamIsClosed() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);
        gcos.close();

        // Act
        gcos.write(42);
    }

    @Test(expected = NullPointerException.class)
    public void write_shouldThrowNullPointerException_forNullBuffer() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);

        // Act
        gcos.write(null, 0, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void write_shouldThrowIndexOutOfBounds_forInvalidOffsetAndLength() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);

        // Act
        gcos.write(new byte[5], 10, 1);
    }

    // endregion

    // region Finish and Close Tests

    @Test
    public void finish_shouldWriteTrailerAndMakeStreamUnwritable() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);

        // Act
        gcos.finish();

        // Assert
        // Header (10) + Empty Deflate Block (2) + Trailer (8) = 20 bytes
        assertEquals("Finished stream for empty input should be 20 bytes long", 20, baos.size());

        try {
            gcos.write(42);
            fail("Should not be able to write to a finished stream");
        } catch (IOException e) {
            // Expected
            assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    public void close_shouldBeIdempotent() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);

        // Act & Assert
        gcos.close();
        try {
            gcos.close(); // Second call should not throw
        } catch (Exception e) {
            fail("Subsequent calls to close() should not throw an exception.");
        }
    }

    @Test(expected = IOException.class)
    public void close_shouldPropagateIOExceptionFromUnderlyingStream() throws IOException {
        // Arrange
        OutputStream failingStream = new FailingOutputStream(new IOException("Test Exception"));
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(failingStream);

        // Act
        gcos.close();
    }

    @Test(expected = NullPointerException.class)
    public void finish_shouldThrowException_whenAlreadyClosed() throws IOException {
        // Arrange
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(baos);
        gcos.close();

        // Act
        // This throws NPE because the internal deflater is set to null on close.
        gcos.finish();
    }

    // endregion

    /**
     * A helper output stream that throws a specified IOException on any operation.
     */
    private static class FailingOutputStream extends OutputStream {
        private final IOException exception;

        FailingOutputStream(IOException e) {
            this.exception = e;
        }

        @Override
        public void write(int b) throws IOException {
            throw exception;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            throw exception;
        }

        @Override
        public void close() throws IOException {
            throw exception;
        }
    }
}