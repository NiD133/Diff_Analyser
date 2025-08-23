package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * This class contains tests for the {@link PdfImage} class.
 * This refactored test focuses on the transferBytes method.
 */
public class PdfImage_ESTestTest16 extends PdfImage_ESTest_scaffolding {

    /**
     * Tests that {@link PdfImage#transferBytes(InputStream, OutputStream, int)}
     * copies exactly the specified number of bytes from the input stream to the output stream.
     *
     * @throws IOException if an I/O error occurs during the test.
     */
    @Test(timeout = 4000)
    public void transferBytes_withFixedLength_copiesCorrectAmountOfData() throws IOException {
        // Arrange: Prepare a source byte array and the streams for the test.
        byte[] sourceData = {10, 20, 30, 40, 50, 60, 70, 80};
        InputStream inputStream = new ByteArrayInputStream(sourceData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesToTransfer = 4;

        // Act: Call the method to transfer a subset of the bytes.
        PdfImage.transferBytes(inputStream, outputStream, bytesToTransfer);

        // Assert: Verify that the output stream contains the correct data.
        // 1. Check if the number of bytes transferred is as expected.
        assertEquals("The number of bytes written to the output stream should match the specified length.",
                bytesToTransfer, outputStream.size());

        // 2. Check if the content of the transferred bytes is correct.
        byte[] expectedOutput = Arrays.copyOf(sourceData, bytesToTransfer);
        assertArrayEquals("The byte content transferred to the output stream should be correct.",
                expectedOutput, outputStream.toByteArray());
    }
}