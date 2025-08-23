package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that calling the write(byte[]) method with a null buffer
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void writeWithNullBufferShouldThrowNullPointerException() throws IOException {
        // Arrange: Create a GzipCompressorOutputStream that writes to a dummy byte array.
        OutputStream dummyOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(dummyOutputStream);

        // Act: Attempt to write a null byte array.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        gzipOutputStream.write(null);
    }
}