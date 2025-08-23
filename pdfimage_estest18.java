package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link PdfImage} constructor.
 */
// The original class name is kept for context, but in a real scenario,
// it would be renamed to something like PdfImageTest.
public class PdfImage_ESTestTest18 extends PdfImage_ESTest_scaffolding {

    /**
     * Tests that the PdfImage constructor correctly processes a JBIG2 image
     * that is both an image mask itself and has an external mask reference provided.
     *
     * <p>It should result in a PDF dictionary containing both the /ImageMask flag
     * and a /Mask entry pointing to the external reference.</p>
     */
    @Test
    public void constructor_withJbig2ImageMaskAndExternalMaskRef_setsCorrectPdfProperties() throws BadPdfFormatException {
        // Arrange
        // 1. Create a JBIG2 image, which is a 1-bit-per-component image type.
        byte[] emptyImageData = new byte[0];
        ImgJBIG2 jbig2Image = new ImgJBIG2(100, 100, emptyImageData, null);

        // 2. Configure the image to be an image mask.
        jbig2Image.makeMask();

        // 3. Create a PDF indirect reference to act as an external mask.
        PdfIndirectReference externalMaskReference = new PdfIndirectReference(1, 0);

        // Act
        // Create the PdfImage, passing the image mask and the external mask reference.
        PdfImage pdfImage = new PdfImage(jbig2Image, null, externalMaskReference);

        // Assert
        // The resulting PdfImage object should be a dictionary.
        assertEquals(PdfObject.DICTIONARY, pdfImage.type());

        // Verify that the /ImageMask property is set to true, as the source image is a mask
        // with 1 bit per component.
        assertEquals(PdfBoolean.PDFTRUE, pdfImage.get(PdfName.IMAGEMASK));

        // Verify that the /Mask property points to the provided external mask reference.
        assertEquals(externalMaskReference, pdfImage.get(PdfName.MASK));
    }
}