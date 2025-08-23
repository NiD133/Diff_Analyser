package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.Image;
import org.junit.Test;

/**
 * This test case focuses on the constructor behavior of the {@link PdfImage} class.
 */
public class PdfImage_ESTestTest15 {

    /**
     * Verifies that the PdfImage constructor throws a NullPointerException
     * when the provided Image object is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullImage() throws BadPdfFormatException {
        // Arrange
        Image nullImage = null;
        String dummyName = "ImageName";
        // The indirect reference is a required parameter, but its value is irrelevant for this test.
        PdfIndirectReference dummyMaskRef = new PdfIndirectReference(0, 0);

        // Act
        // This call is expected to throw a NullPointerException because the first argument is null.
        new PdfImage(nullImage, dummyName, dummyMaskRef);

        // Assert is handled by the @Test(expected=...) annotation.
    }
}