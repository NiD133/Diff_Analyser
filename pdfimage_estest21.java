package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * This test suite contains tests for the {@link PdfImage} class.
 * Note: The original test class name 'PdfImage_ESTestTest21' was preserved for context.
 */
public class PdfImage_ESTestTest21 extends PdfImage_ESTest_scaffolding {

    /**
     * Tests that the getImage() method correctly returns the original Image object
     * that was passed to the PdfImage constructor.
     *
     * @throws BadPdfFormatException if the PdfImage constructor fails, which is not expected in this test.
     */
    @Test
    public void getImageShouldReturnTheOriginalImageInstance() throws BadPdfFormatException {
        // Arrange: Create the necessary objects to instantiate a PdfImage.
        // The specific image type and its data are not important for this test,
        // as we are only verifying the behavior of the getter method.
        Image originalImage = new ImgJBIG2(100, 200, new byte[0], new byte[0]);
        PdfIndirectReference maskReference = new PdfIndirectReference(1, 0);
        String imageName = "test_image";

        PdfImage pdfImage = new PdfImage(originalImage, imageName, maskReference);

        // Act: Call the method under test.
        Image retrievedImage = pdfImage.getImage();

        // Assert: Verify that the returned object is the exact same instance
        // as the one passed to the constructor.
        assertSame("The getImage() method should return the same image instance that was provided to the constructor.",
                originalImage, retrievedImage);
    }
}