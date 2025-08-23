package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Test suite for the {@link PdfDictionary} class.
 */
public class PdfDictionaryTest {

    /**
     * Verifies that calling the toPdf() method with a null OutputStream
     * throws a NullPointerException.
     * <p>
     * This test ensures that the method correctly handles invalid input by
     * failing fast, which is a critical contract for methods that perform I/O operations.
     */
    @Test(expected = NullPointerException.class)
    public void toPdf_whenOutputStreamIsNull_throwsNullPointerException() throws IOException {
        // Arrange
        // PdfResources is a concrete subclass of PdfDictionary, suitable for testing.
        PdfDictionary dictionary = new PdfResources();
        
        // A PdfWriter instance is required by the method signature. Since the test
        // triggers an error before the writer is used, a default instance is sufficient.
        PdfWriter writer = new PdfWriter();

        // Act & Assert
        // The method is called with a null OutputStream.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        dictionary.toPdf(writer, null);
    }
}