package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This test verifies the behavior of the {@link PdfImage#transferBytes} method
 * when the destination buffer does not have enough capacity.
 */
public class PdfImage_ESTestTest12 extends PdfImage_ESTest_scaffolding {

    /**
     * Tests that transferBytes throws an ArrayIndexOutOfBoundsException when attempting
     * to write more data into a ByteBuffer than its remaining capacity allows.
     */
    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void transferBytesShouldThrowExceptionWhenDestinationBufferIsTooSmall() throws IOException {
        // Arrange: Create a destination buffer that is almost full.
        // The iText ByteBuffer's default capacity is 4096 bytes. By setting its internal
        // count, we simulate it being nearly full, leaving very little remaining space.
        final int bufferCapacity = 4096; // Assumed default capacity of iText's ByteBuffer
        final int initialContentSize = 4092;
        final int remainingCapacity = bufferCapacity - initialContentSize; // Only 4 bytes left

        ByteBuffer destinationBuffer = new ByteBuffer();
        destinationBuffer.count = initialContentSize;

        // Arrange: Create a source input stream with more data than the buffer's remaining capacity.
        byte[] sourceData = new byte[8];
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        
        // Arrange: Define the number of bytes to transfer, which exceeds the remaining capacity.
        int bytesToTransfer = 48;
        assert bytesToTransfer > remainingCapacity; // This condition is what triggers the exception.

        // Act: Attempt to transfer the bytes. This call is expected to throw the exception
        // because it will try to write past the destination buffer's capacity.
        PdfImage.transferBytes(inputStream, destinationBuffer, bytesToTransfer);
        
        // Assert: The test will pass if the expected ArrayIndexOutOfBoundsException is thrown.
        // This is handled by the `expected` attribute of the @Test annotation.
    }
}