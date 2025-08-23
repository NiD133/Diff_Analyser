package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.InputStream;

/**
 * This test suite verifies the behavior of the static utility methods in the PdfImage class.
 * Note: The class name is from an auto-generated test suite. A more descriptive name like
 * 'PdfImageTest' would be preferable in a real-world scenario.
 */
public class PdfImage_ESTestTest11 extends PdfImage_ESTest_scaffolding {

    /**
     * Verifies that calling transferBytes with a null InputStream
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void transferBytes_whenInputStreamIsNull_throwsNullPointerException() {
        // Arrange: Prepare the arguments for the method call.
        // The InputStream is intentionally null to trigger the exception.
        InputStream nullInputStream = null;
        ByteBuffer outputBuffer = new ByteBuffer();
        final int readUntilEndOfStream = -1;

        // Act & Assert: Call the method and expect a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        PdfImage.transferBytes(nullInputStream, outputBuffer, readUntilEndOfStream);
    }
}