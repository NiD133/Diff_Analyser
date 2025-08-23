package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.FdfWriter;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfPSXObject;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class PdfImage_ESTest extends PdfImage_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Test(timeout = TIMEOUT)
    public void testFlateCompressAndImportAll() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(257, (byte) 97, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(-1, 1700, 0);
        PdfImage pdfImage = new PdfImage(imgJBIG2, "", pdfIndirectReference);

        pdfImage.flateCompress();
        pdfImage.importAll(pdfImage);

        assertFalse("PdfImage should not be a string", pdfImage.isString());
    }

    @Test(timeout = TIMEOUT)
    public void testImportAllFromAnotherPdfImage() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(484, 484, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(4, 0, 6);
        PdfImage pdfImage0 = new PdfImage(imgJBIG2, "UnicodeBig", pdfIndirectReference);
        PdfImage pdfImage1 = new PdfImage(imgJBIG2, "", pdfIndirectReference);

        pdfImage0.importAll(pdfImage1);

        assertFalse("PdfImage should not be an array", pdfImage0.isArray());
    }

    @Test(timeout = TIMEOUT)
    public void testTransferBytesWithEmptyInput() throws Throwable {
        byte[] byteArray = new byte[2];
        ByteBuffer byteBuffer = new ByteBuffer();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        PdfImage.transferBytes(byteArrayInputStream, byteBuffer, (byte) 48);

        assertEquals("InputStream should be empty", 0, byteArrayInputStream.available());
        assertEquals("ByteBuffer should have 2 bytes", 2, byteBuffer.size());
    }

    @Test(timeout = TIMEOUT)
    public void testTransferBytesWithFullInput() throws Throwable {
        ByteBuffer byteBuffer = new ByteBuffer();
        byte[] byteArray = new byte[8];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        PdfImage.transferBytes(byteArrayInputStream, byteBuffer, (byte) 0);

        assertEquals("InputStream should have 8 bytes available", 8, byteArrayInputStream.available());
        assertEquals("ByteBuffer should be empty", 0, byteBuffer.size());
    }

    @Test(timeout = TIMEOUT)
    public void testImageWidth() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(484, 484, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        imgJBIG2.makeMask();
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(9496, 29, 5);
        PdfImage pdfImage = new PdfImage(imgJBIG2, "", pdfIndirectReference);
        Image image = pdfImage.getImage();

        assertEquals("Image width should be 484.0", 484.0F, image.getWidth(), 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void testImageNotDeflated() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(257, (byte) 97, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(0, 2147418112, 1);
        ImgJBIG2 imgJBIG2_1 = new ImgJBIG2(2535, 0, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        imgJBIG2.scaleToFit((Rectangle) imgJBIG2_1);
        PdfImage pdfImage = new PdfImage(imgJBIG2, "com.itextpdf.text.pdf.PdfImage", pdfIndirectReference);
        Image image = pdfImage.getImage();

        assertFalse("Image should not be deflated", image.isDeflated());
    }

    @Test(timeout = TIMEOUT)
    public void testImageLeftPosition() throws Throwable {
        byte[] byteArray = new byte[8];
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(1106, 1106, byteArray, byteArray);
        imgJBIG2.setPaddingTop(1106);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(0, 1, -208);
        PdfImage pdfImage = new PdfImage(imgJBIG2, "yw$,.", pdfIndirectReference);
        Image image = pdfImage.getImage();

        assertEquals("Image left position should be 0.0", 0.0F, image.getLeft(), 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void testImageAbsoluteY() throws Throwable {
        byte[] byteArray = new byte[3];
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(25, 25, byteArray, byteArray);
        imgJBIG2.setIndentationRight(-1.0F);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(25, 6);
        PdfImage pdfImage = new PdfImage(imgJBIG2, "UnicodeBig", pdfIndirectReference);
        Image image = pdfImage.getImage();

        assertEquals("Image absolute Y should be NaN", Float.NaN, image.getAbsoluteY(), 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void testImageOriginalNone() throws Throwable {
        byte[] byteArray = new byte[1];
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(27, 27, byteArray, byteArray);
        imgJBIG2.setIndentationLeft(27);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference();
        PdfImage pdfImage = new PdfImage(imgJBIG2, "UnicodeBig", pdfIndirectReference);
        Image image = pdfImage.getImage();

        assertEquals("Image original type should be ORIGINAL_NONE", 0, Image.ORIGINAL_NONE);
    }

    @Test(timeout = TIMEOUT)
    public void testImageScaleToFitLineWhenOverflow() throws Throwable {
        byte[] byteArray = new byte[1];
        Image image = Image.getInstance(4145, -84, byteArray, byteArray);
        PdfImage pdfImage = new PdfImage(image, "", null);
        Image image1 = pdfImage.getImage();

        assertFalse("Image should not scale to fit line when overflow", image1.isScaleToFitLineWhenOverflow());
    }

    @Test(timeout = TIMEOUT)
    public void testTransferBytesWithNullInputStream() throws Throwable {
        ByteBuffer byteBuffer = new ByteBuffer();
        try {
            PdfImage.transferBytes(null, byteBuffer, -1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.PdfImage", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testTransferBytesWithOverflow() throws Throwable {
        ByteBuffer byteBuffer = new ByteBuffer();
        byteBuffer.count = 4092;
        byte[] byteArray = new byte[8];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try {
            PdfImage.transferBytes(byteArrayInputStream, byteBuffer, (byte) 48);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.ByteBuffer", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testTransferBytesWithUnconnectedPipe() throws Throwable {
        ByteBuffer byteBuffer = new ByteBuffer();
        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            PdfImage.transferBytes(pipedInputStream, byteBuffer, 2594);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testImportAllWithNullPdfImage() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(257, (byte) 97, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(0, 2147418112, 1);
        PdfImage pdfImage = new PdfImage(imgJBIG2, "com.itextpdf.text.pdf.PdfImage", pdfIndirectReference);
        try {
            pdfImage.importAll(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.PdfImage", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCreatePdfImageWithNullImage() throws Throwable {
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(508, 508);
        try {
            new PdfImage(null, "", pdfIndirectReference);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.PdfImage", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testTransferBytesWithBufferedInputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(pipedInputStream, 1291);
        MockPrintStream mockPrintStream = new MockPrintStream(pipedOutputStream);
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        Object[] objectArray = new Object[3];
        PrintStream printStream = mockPrintStream.printf(locale, ">E&1|", objectArray);

        PdfImage.transferBytes(bufferedInputStream, printStream, 4);

        assertEquals("PipedInputStream should have 4 bytes available", 4, pipedInputStream.available());
    }

    @Test(timeout = TIMEOUT)
    public void testCreatePdfImageWithTemplate() throws Throwable {
        ByteBuffer byteBuffer = new ByteBuffer();
        FdfWriter fdfWriter = new FdfWriter();
        FdfWriter.Wrt fdfWriterWrt = new FdfWriter.Wrt(byteBuffer, fdfWriter);
        PdfTemplate pdfTemplate = PdfTemplate.createTemplate((PdfWriter) fdfWriterWrt, 1.0F, 2330.0F, fdfWriterWrt.PDF_VERSION_1_4);
        ImgTemplate imgTemplate = new ImgTemplate(pdfTemplate);
        int[] intArray = new int[1];
        imgTemplate.setTransparency(intArray);
        try {
            new PdfImage(imgTemplate, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testPdfImageType() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(484, 484, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        imgJBIG2.makeMask();
        int[] intArray = new int[0];
        imgJBIG2.setTransparency(intArray);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(4096, 4096, -1013);
        PdfImage pdfImage = new PdfImage(imgJBIG2, null, pdfIndirectReference);

        assertEquals("PdfImage type should be 7", 7, pdfImage.type());
    }

    @Test(timeout = TIMEOUT)
    public void testPdfImageIsNotNull() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(484, 484, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        int[] intArray = new int[0];
        imgJBIG2.setTransparency(intArray);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(4096, 4096, -1013);
        PdfImage pdfImage = new PdfImage(imgJBIG2, null, pdfIndirectReference);

        assertFalse("PdfImage should not be null", pdfImage.isNull());
    }

    @Test(timeout = TIMEOUT)
    public void testCreatePdfImageWithPdfTemplate() throws Throwable {
        PdfPSXObject pdfPSXObject = new PdfPSXObject();
        Image image = Image.getInstance((PdfTemplate) pdfPSXObject);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(2, 3);
        image.setInterpolation(true);
        try {
            new PdfImage(image, null, pdfIndirectReference);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testImageBX() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(-3134, 484, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(1014, 1, 4);
        PdfImage pdfImage = new PdfImage(imgJBIG2, "", pdfIndirectReference);
        Image image = pdfImage.getImage();

        assertEquals("Image BX should be 2", 2, Image.BX);
    }

    @Test(timeout = TIMEOUT)
    public void testPdfImageName() throws Throwable {
        ImgJBIG2 imgJBIG2 = new ImgJBIG2(484, 484, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference pdfIndirectReference = new PdfIndirectReference(4096, 4096, -1013);
        PdfImage pdfImage = new PdfImage(imgJBIG2, null, pdfIndirectReference);
        PdfName pdfName = pdfImage.name();

        assertEquals("PdfObject NULL should be 8", 8, PdfObject.NULL);
    }
}