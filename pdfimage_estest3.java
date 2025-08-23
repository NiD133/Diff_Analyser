package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the utility methods within the {@link PdfImage} class.
 */
public class PdfImageTest {

    /**
     * Verifies that transferBytes copies all available bytes from an input stream
     * when the requested transfer length is greater than the number of bytes available.
     */
    @Test
    public void transferBytes_shouldCopyAllAvailableBytes_whenRequestedLengthExceedsStreamSize() throws IOException {
        // Arrange: Set up an input stream with a known, small amount of data.
        byte[] inputData = { 10, 20 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteBuffer outputBuffer = new ByteBuffer(); // iText's ByteBuffer extends ByteArrayOutputStream

        // Define a requested length that is deliberately larger than the input data.
        int requestedLength = 1024;

        // Act: Call the method to transfer bytes.
        PdfImage.transferBytes(inputStream, outputBuffer, requestedLength);

        // Assert: Confirm that only the available bytes were transferred and the stream is now empty.
        assertEquals("The input stream should be fully consumed.", 0, inputStream.available());
        assertEquals("The output buffer's size should match the actual amount of data read.", inputData.length, outputBuffer.size());
        assertArrayEquals("The data in the output buffer should exactly match the input data.", inputData, outputBuffer.toByteArray());
    }
}