package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import static org.junit.Assert.assertSame;

// The class name and scaffolding are kept to match the original structure.
public class PdfImage_ESTestTest8 extends PdfImage_ESTest_scaffolding {

    /**
     * Verifies that the getImage() method returns the exact same Image instance
     * that was provided to the PdfImage constructor.
     */
    @Test
    public void getImage_shouldReturnTheSameInstancePassedToConstructor() throws BadPdfFormatException {
        // Arrange: Create a dummy image and other required parameters for the PdfImage constructor.
        final int width = 25;
        final int height = 25;
        byte[] dummyRawData = new byte[3];
        byte[] dummyGlobals = new byte[3]; // ImgJBIG2 requires a "globals" byte array.
        Image originalImage = new ImgJBIG2(width, height, dummyRawData, dummyGlobals);

        String imageName = "TestImage";
        // The constructor accepts a null mask reference, which simplifies the test setup.
        PdfIndirectReference maskRef = null;

        // Act: Create a PdfImage object and then retrieve the underlying Image.
        PdfImage pdfImage = new PdfImage(originalImage, imageName, maskRef);
        Image retrievedImage = pdfImage.getImage();

        // Assert: The retrieved image should be the same instance as the original one.
        assertSame("The retrieved image should be the same instance as the one passed to the constructor.",
                originalImage, retrievedImage);
    }
}