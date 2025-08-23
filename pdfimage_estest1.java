package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for the {@link PdfImage} class.
 */
public class PdfImage_ESTestTest1 extends PdfImage_ESTest_scaffolding {

    /**
     * Tests that calling `importAll` with the object itself as an argument
     * does not corrupt the object's fundamental type, even after stream compression.
     *
     * <p>The `importAll` method is intended to copy properties from another PdfImage.
     * This test covers the edge case of passing the object to itself, which should
     * effectively be a no-op on the dictionary's content. The assertion confirms
     * that the object remains a valid stream-based type.</p>
     *
     * @throws BadPdfFormatException if the PdfImage cannot be constructed.
     */
    @Test(timeout = 4000)
    public void importAllWithSelfShouldNotAlterObjectType() throws BadPdfFormatException {
        // Arrange: Set up a PdfImage instance.
        // We use a JBIG2 image, which is a specific monochrome image format.
        // The image data itself is empty for this test's purpose.
        byte[] emptyImageData = new byte[0];
        ImgJBIG2 jbig2Image = new ImgJBIG2(257, 97, emptyImageData, emptyImageData);

        // A PdfIndirectReference can be used for features like image masks.
        PdfIndirectReference maskReference = new PdfIndirectReference(-1, 1700, 0);

        // Create the PdfImage object to be tested.
        PdfImage pdfImage = new PdfImage(jbig2Image, "ImageName", maskReference);

        // Act: Perform a sequence of operations on the PdfImage.
        // 1. Compress the image stream data.
        pdfImage.flateCompress();

        // 2. Call importAll with the object itself.
        pdfImage.importAll(pdfImage);

        // Assert: Verify the object's type has not been corrupted.
        // A PdfImage is a type of PdfStream, so isString() should always be false.
        // This assertion serves as a basic sanity check.
        assertFalse("A PdfImage should not be classified as a string type after operations.", pdfImage.isString());
    }
}