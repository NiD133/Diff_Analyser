package com.itextpdf.text.pdf;

import com.itextpdf.text.BadPdfFormatException;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import com.itextpdf.text.ImgTemplate;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link PdfImage} class.
 */
public class PdfImageTest {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    // --- Constructor Tests ---

    @Test
    public void constructor_withValidJbig2Image_setsPdfDictionaryProperties() throws BadPdfFormatException {
        // Arrange
        Image jbig2Image = new ImgJBIG2(100, 200, EMPTY_BYTE_ARRAY, null);
        String imageName = "MyImage";

        // Act
        PdfImage pdfImage = new PdfImage(jbig2Image, imageName, null);

        // Assert
        assertEquals(PdfName.XOBJECT, pdfImage.get(PdfName.TYPE));
        assertEquals(PdfName.IMAGE, pdfImage.get(PdfName.SUBTYPE));
        assertEquals(new PdfNumber(100), pdfImage.get(PdfName.WIDTH));
        assertEquals(new PdfNumber(200), pdfImage.get(PdfName.HEIGHT));
        assertEquals(new PdfName(imageName), pdfImage.name());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullImage_throwsNullPointerException() throws BadPdfFormatException {
        // Act
        new PdfImage(null, "someName", null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withImageFromTemplate_throwsExceptionDueToMissingUrl() throws BadPdfFormatException {
        // Arrange: An ImgTemplate does not have a URL, which the PdfImage constructor expects for this image type.
        PdfTemplate template = new PdfTemplate();
        Image imageFromTemplate = new ImgTemplate(template);

        // Act
        new PdfImage(imageFromTemplate, "templateImage", null);
    }

    // --- Method: getImage() ---

    @Test
    public void getImage_returnsTheOriginalImageObject() throws BadPdfFormatException {
        // Arrange
        Image originalImage = new ImgJBIG2(50, 50, EMPTY_BYTE_ARRAY, null);
        PdfImage pdfImage = new PdfImage(originalImage, "imageName", null);

        // Act
        Image retrievedImage = pdfImage.getImage();

        // Assert
        assertSame("The retrieved image should be the same instance as the original.", originalImage, retrievedImage);
    }

    // --- Method: importAll() ---

    @Test
    public void importAll_copiesAllPropertiesFromSource() throws BadPdfFormatException {
        // Arrange
        Image initialImage = new ImgJBIG2(10, 20, EMPTY_BYTE_ARRAY, null);
        PdfImage targetImage = new PdfImage(initialImage, "Target", null);

        Image sourceDataImage = new ImgJBIG2(30, 40, new byte[]{1, 2, 3}, null);
        PdfImage sourceImage = new PdfImage(sourceDataImage, "Source", null);
        sourceImage.put(PdfName.AUTHOR, new PdfString("SourceAuthor"));

        // Act
        targetImage.importAll(sourceImage);

        // Assert
        assertEquals("Name should be copied from the source.", sourceImage.name(), targetImage.name());
        assertSame("Image object should be copied from the source.", sourceImage.getImage(), targetImage.getImage());
        assertArrayEquals("Byte data should be copied from the source.", sourceImage.getBytes(), targetImage.getBytes());
        assertEquals("Dictionary properties should be copied from the source.",
                new PdfString("SourceAuthor"), targetImage.get(PdfName.AUTHOR));
    }

    @Test(expected = NullPointerException.class)
    public void importAll_withNullSource_throwsNullPointerException() throws BadPdfFormatException {
        // Arrange
        Image image = new ImgJBIG2(10, 10, EMPTY_BYTE_ARRAY, null);
        PdfImage pdfImage = new PdfImage(image, "anyName", null);

        // Act
        pdfImage.importAll(null);
    }

    // --- Static Method: transferBytes() ---

    @Test
    public void transferBytes_whenLengthIsLargerThanStream_transfersAllBytes() throws IOException {
        // Arrange
        byte[] sourceData = {10, 20, 30};
        InputStream in = new ByteArrayInputStream(sourceData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int lengthToTransfer = 1024; // A length greater than the available data

        // Act
        PdfImage.transferBytes(in, out, lengthToTransfer);

        // Assert
        assertArrayEquals(sourceData, out.toByteArray());
        assertEquals(0, in.available());
    }

    @Test
    public void transferBytes_whenLengthIsSmallerThanStream_transfersSpecifiedBytes() throws IOException {
        // Arrange
        byte[] sourceData = {10, 20, 30, 40, 50};
        InputStream in = new ByteArrayInputStream(sourceData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int lengthToTransfer = 3;

        // Act
        PdfImage.transferBytes(in, out, lengthToTransfer);

        // Assert
        byte[] expectedData = {10, 20, 30};
        assertArrayEquals(expectedData, out.toByteArray());
        assertEquals(2, in.available());
    }

    @Test
    public void transferBytes_whenLengthIsZero_transfersNoBytes() throws IOException {
        // Arrange
        byte[] sourceData = {10, 20, 30};
        InputStream in = new ByteArrayInputStream(sourceData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act
        PdfImage.transferBytes(in, out, 0);

        // Assert
        assertEquals(0, out.size());
        assertEquals(3, in.available());
    }

    @Test(expected = NullPointerException.class)
    public void transferBytes_withNullInputStream_throwsNullPointerException() throws IOException {
        // Act
        PdfImage.transferBytes(null, new ByteArrayOutputStream(), 100);
    }

    @Test(expected = IOException.class)
    public void transferBytes_withUnconnectedPipe_throwsIOException() throws IOException {
        // Arrange
        PipedInputStream unconnectedInputStream = new PipedInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act
        PdfImage.transferBytes(unconnectedInputStream, out, 100);
    }
}