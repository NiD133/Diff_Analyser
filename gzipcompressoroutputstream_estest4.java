package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains improved tests for the GzipCompressorOutputStream class.
 */
public class GzipCompressorOutputStreamImprovedTest {

    /**
     * Tests that calling write() with an offset and length that are out of bounds for the
     * source buffer correctly throws an ArrayIndexOutOfBoundsException.
     *
     * This is expected behavior, as the call is delegated to java.util.zip.Deflater,
     * which performs these boundary checks.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void writeWithOutOfBoundsParametersShouldThrowException() throws IOException {
        // Arrange: Set up a GzipCompressorOutputStream and an empty data buffer.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(outputStream);
        byte[] emptyBuffer = new byte[0];

        // Define an offset and length that are clearly invalid for the empty buffer.
        int invalidOffset = 1;
        int invalidLength = 1;

        // Act: Attempt to write from the buffer using the out-of-bounds parameters.
        // This action is expected to throw an exception.
        gzipOutputStream.write(emptyBuffer, invalidOffset, invalidLength);

        // Assert: The test expects an ArrayIndexOutOfBoundsException, which is
        // declared in the @Test annotation. If no exception is thrown, the test will fail.
    }
}