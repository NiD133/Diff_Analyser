package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

/**
 * This test suite focuses on the behavior of the PdfImage class,
 * specifically its exception handling.
 */
public class PdfImageTest {

    /**
     * Verifies that calling the importAll method with a null argument
     * correctly throws a NullPointerException.
     *
     * This ensures the method is robust against invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void importAll_whenGivenNullImage_shouldThrowNullPointerException() throws BadPdfFormatException {
        // Arrange: Create a valid PdfImage instance to work with.
        // The specific image details are not relevant to this test; we only need an
        // object to call the method on.
        byte[] dummyImageData = new byte[0];
        ImgJBIG2 jbig2Image = new ImgJBIG2(1, 1, dummyImageData, dummyImageData);
        PdfIndirectReference dummyMaskReference = new PdfIndirectReference();
        PdfImage pdfImage = new PdfImage(jbig2Image, "any-name", dummyMaskReference);

        // Act: Call the method under test with a null argument.
        // The @Test(expected=...) annotation will handle the assertion.
        pdfImage.importAll(null);
    }
}