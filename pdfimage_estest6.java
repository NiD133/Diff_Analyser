package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link PdfImage} class.
 */
public class PdfImageTest {

    /**
     * Verifies that when a PdfImage is created from a JBIG2 image,
     * the {@link PdfImage#getImage()} method returns the original, unmodified Image instance.
     *
     * <p>Specifically, it checks that the 'deflated' property of the image is not altered.
     * This is because JBIG2 images use their own compression type (JBIG2Decode) in PDF
     * and should not be additionally Flate-compressed by the PdfImage constructor.</p>
     */
    @Test
    public void getImage_fromJbig2Image_returnsOriginalUnmodifiedInstance() throws BadPdfFormatException {
        // Arrange: Create a source JBIG2 image and other required parameters.
        // JBIG2 is a bilevel image format. We use empty byte arrays for image data for this test.
        int width = 257;
        int height = 97;
        byte[] emptyImageData = new byte[0];
        Image sourceImage = new ImgJBIG2(width, height, emptyImageData, emptyImageData);

        // A non-null mask reference is used to ensure we test this specific constructor path.
        PdfIndirectReference maskReference = new PdfIndirectReference(0, 0);
        String imageName = "TestImage";

        // Act: Create the PdfImage wrapper and then retrieve the underlying Image object.
        PdfImage pdfImage = new PdfImage(sourceImage, imageName, maskReference);
        Image retrievedImage = pdfImage.getImage();

        // Assert: Verify that the retrieved image is the same instance and its state is unchanged.
        assertSame("getImage() should return the same instance that was passed to the constructor.",
                sourceImage, retrievedImage);

        assertFalse("The 'isDeflated' flag of the original image should not be modified.",
                retrievedImage.isDeflated());
    }
}