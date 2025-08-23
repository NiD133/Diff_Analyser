package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class PdfImage_ESTestTest9 { // Note: Test class name kept for context.

    /**
     * Tests that the getImage() method returns the same Image instance
     * that was used to construct the PdfImage object.
     */
    @Test
    public void getImageShouldReturnTheOriginalImageInstance() throws BadPdfFormatException {
        // Arrange: Create a sample image and its dependencies for the PdfImage constructor.
        byte[] imageData = new byte[1];
        // ImgJBIG2 is a specific type of Image.
        Image originalImage = new ImgJBIG2(27, 27, imageData, imageData);
        originalImage.setIndentationLeft(27); // Set a property to verify its preservation.

        String imageName = "MyTestImage";
        PdfIndirectReference maskReference = new PdfIndirectReference();

        // Act: Create a PdfImage wrapper and then retrieve the image from it.
        PdfImage pdfImage = new PdfImage(originalImage, imageName, maskReference);
        Image retrievedImage = pdfImage.getImage();

        // Assert: Verify that the retrieved image is the exact same object as the original.
        assertSame("The retrieved image should be the same instance as the original.", originalImage, retrievedImage);
        assertEquals("Properties of the original image should be preserved.", 27.0f, retrievedImage.getIndentationLeft(), 0.0f);
    }
}