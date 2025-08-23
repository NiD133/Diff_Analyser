package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the utility methods in the {@link PdfImage} class.
 */
public class PdfImageTest {

    /**
     * Verifies that transferBytes() correctly handles a request to transfer zero bytes.
     * It should not read from the input stream or write to the output stream.
     */
    @Test(timeout = 4000)
    public void transferBytes_whenLengthIsZero_shouldTransferNoBytes() throws IOException {
        // Arrange: Set up an input stream with data and an empty output buffer.
        byte[] inputData = new byte[8];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteBuffer outputBuffer = new ByteBuffer();
        int initialAvailableBytes = inputStream.available();

        // Act: Attempt to transfer zero bytes from the input to the output.
        PdfImage.transferBytes(inputStream, outputBuffer, 0);

        // Assert: Verify that the state of both streams remains unchanged.
        assertEquals("Output buffer should remain empty.", 0, outputBuffer.size());
        assertEquals("Input stream should not have been read from.", initialAvailableBytes, inputStream.available());
    }
}