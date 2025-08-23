package com.itextpdf.text.pdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This test verifies the behavior of the PdfImage class, specifically its ability
 * to wrap an existing iText Image object and preserve its properties.
 */
public class PdfImageTest {

    /**
     * Tests that the getImage() method correctly returns the original Image object
     * from which the PdfImage was created, preserving its properties like width.
     */
    @Test
    public void getImage_shouldReturnOriginalImageWithCorrectWidth() throws Exception {
        // Arrange
        final int expectedWidth = 484;
        final int expectedHeight = 484;
        final byte[] emptyImageData = new byte[0];

        // Create a JBIG2 image instance to be wrapped by PdfImage.
        // This image is also configured as a mask to test a specific constructor path.
        Image sourceImage = new ImgJBIG2(expectedWidth, expectedHeight, emptyImageData, emptyImageData);
        sourceImage.makeMask();

        // The PdfImage constructor requires a mask reference when the image is a mask.
        // We provide a dummy reference as its specific values are not relevant to this test.
        PdfIndirectReference dummyMaskReference = new PdfIndirectReference(0, 0);

        // Act
        // Create the PdfImage instance, which is the object under test.
        PdfImage pdfImage = new PdfImage(sourceImage, "testImageName", dummyMaskReference);
        Image retrievedImage = pdfImage.getImage();

        // Assert
        assertNotNull("The retrieved image should not be null.", retrievedImage);
        assertEquals("The width of the retrieved image should match the source image.",
                (float) expectedWidth, retrievedImage.getWidth(), 0.01f);
    }
}