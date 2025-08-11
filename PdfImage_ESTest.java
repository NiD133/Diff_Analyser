package com.itextpdf.text.pdf;

import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for PdfImage.
 *
 * These tests avoid EvoSuite scaffolding and focus on documenting the observable
 * behavior of PdfImage's constructor, accessors, and the transferBytes utility.
 */
public class PdfImageTest {

    // ---- Constructor behavior ------------------------------------------------

    @Test
    public void constructor_generatesResourceName_and_storesDimensions_forJbig2() throws Exception {
        // Given: a minimal JBIG2 image with known dimensions
        ImgJBIG2 img = new ImgJBIG2(10, 12, new byte[0], new byte[0]);

        // When: constructing a PdfImage without an explicit name
        PdfImage pdfImage = new PdfImage(img, null, null);

        // Then: a resource name is generated and basic keys are present
        assertNotNull("PdfImage.name() should not be null when name is omitted", pdfImage.name());
        assertSame("getImage() should return the same Image instance passed to the constructor", img, pdfImage.getImage());

        // And: the dictionary contains correct width/height and subtype
        assertEquals(10, ((PdfNumber) pdfImage.get(PdfName.WIDTH)).intValue());
        assertEquals(12, ((PdfNumber) pdfImage.get(PdfName.HEIGHT)).intValue());
        assertEquals(PdfName.IMAGE, pdfImage.get(PdfName.SUBTYPE));
    }

    @Test
    public void constructor_setsJbig2Filter_colorSpace_and_bitsPerComponent() throws Exception {
        // Given: a JBIG2 image (raw data present is fine; constructor handles both)
        ImgJBIG2 img = new ImgJBIG2(20, 20, new byte[0], new byte[0]);

        // When
        PdfImage pdfImage = new PdfImage(img, "MyImage", null);

        // Then
        assertEquals(PdfName.JBIG2DECODE, pdfImage.get(PdfName.FILTER));
        assertEquals(PdfName.DEVICEGRAY, pdfImage.get(PdfName.COLORSPACE));
        assertEquals(1, ((PdfNumber) pdfImage.get(PdfName.BITSPERCOMPONENT)).intValue());
    }

    @Test
    public void constructor_marksImageMask_whenImageIsMask() throws Exception {
        // Given: a mask image (JBIG2 with makeMask)
        ImgJBIG2 img = new ImgJBIG2(15, 15, new byte[0], new byte[0]);
        img.makeMask();

        // When
        PdfImage pdfImage = new PdfImage(img, null, null);

        // Then: IMAGEMASK key is set to true
        assertEquals(PdfBoolean.PDFTRUE, pdfImage.get(PdfName.IMAGEMASK));
    }

    @Test
    public void constructor_withNullImage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PdfImage(null, "AnyName", null));
    }

    // ---- importAll behavior --------------------------------------------------

    @Test
    public void importAll_withNull_throwsNullPointerException() throws Exception {
        ImgJBIG2 img = new ImgJBIG2(10, 10, new byte[0], new byte[0]);
        PdfImage pdfImage = new PdfImage(img, "Img", null);

        assertThrows(NullPointerException.class, () -> pdfImage.importAll(null));
    }

    // ---- transferBytes behavior ---------------------------------------------

    @Test
    public void transferBytes_lenPositive_readsUntilEofOrLenExceeded() throws Exception {
        // Given: an input with 2 bytes and a large requested length
        byte[] input = {1, 2};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // When: requesting to copy up to 48 bytes
        PdfImage.transferBytes(in, out, 48);

        // Then: only the available bytes are copied
        assertArrayEquals(input, out.toByteArray());
        assertEquals("All input bytes should be consumed", 0, in.available());
    }

    @Test
    public void transferBytes_lenZero_copiesNothing() throws Exception {
        byte[] input = {10, 20, 30, 40, 50, 60, 70, 80};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfImage.transferBytes(in, out, 0);

        assertEquals("No bytes should be written for len=0", 0, out.size());
        assertEquals("Input should remain untouched for len=0", input.length, in.available());
    }

    @Test
    public void transferBytes_lenNegative_copiesAll() throws Exception {
        byte[] input = {9, 8, 7, 6, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfImage.transferBytes(in, out, -1);

        assertArrayEquals(input, out.toByteArray());
        assertEquals(0, in.available());
    }

    @Test
    public void transferBytes_nullInput_throwsNullPointerException() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertThrows(NullPointerException.class, () -> PdfImage.transferBytes(null, out, -1));
    }

    @Test
    public void transferBytes_unconnectedPipedInput_throwsIOException() {
        PipedInputStream in = new PipedInputStream(); // Not connected on purpose
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertThrows(IOException.class, () -> PdfImage.transferBytes(in, out, 10));
    }
}