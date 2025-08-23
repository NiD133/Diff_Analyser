package com.itextpdf.text.pdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link PdfImage} class.
 * This refactored test focuses on the behavior of the getImage() method.
 */
// Note: The original test class name and inheritance are preserved for compatibility.
public class PdfImage_ESTestTest7 extends PdfImage_ESTest_scaffolding {

    /**
     * Verifies that the getImage() method returns the exact same Image instance
     * that was used to construct the PdfImage object.
     */
    @Test
    public void getImage_shouldReturnTheOriginalImageInstance() throws BadPdfFormatException {
        // Arrange: Create the necessary objects for PdfImage construction.
        // The specific values used here are for instantiation purposes and are not critical to the test's logic.
        byte[] dummyImageData = new byte[8];
        Image originalImage = new ImgJBIG2(1106, 1106, dummyImageData, dummyImageData);
        
        // A name and an indirect reference are required by the constructor signature.
        String imageName = "TestImage";
        PdfIndirectReference dummyMaskReference = new PdfIndirectReference(0, 1, -208);

        PdfImage pdfImage = new PdfImage(originalImage, imageName, dummyMaskReference);

        // Act: Retrieve the image from the PdfImage wrapper object.
        Image retrievedImage = pdfImage.getImage();

        // Assert: The retrieved image should be the same instance as the original image.
        // This is a stronger and more intention-revealing assertion than checking a single property.
        assertSame("The getImage() method should return the original Image object.", originalImage, retrievedImage);
    }
}