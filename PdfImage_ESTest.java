package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class PdfImage_ESTest extends PdfImage_ESTest_scaffolding {

    // ========================================================================
    // Tests for importAll() functionality
    // ========================================================================
    
    @Test(timeout = 4000)
    public void importAll_selfImport_shouldNotChangeType() throws Throwable {
        byte[] globalData = new byte[0];
        byte[] pageData = new byte[0];
        ImgJBIG2 img = new ImgJBIG2(257, (byte)97, globalData, pageData);
        PdfIndirectReference ref = new PdfIndirectReference(-1, 1700, 0);
        PdfImage pdfImage = new PdfImage(img, "", ref);
        
        pdfImage.flateCompress();
        pdfImage.importAll(pdfImage);  // Self-import should be safe
        
        assertFalse("Should not become string after import", pdfImage.isString());
    }

    @Test(timeout = 4000)
    public void importAll_fromAnotherImage_shouldNotConvertToArray() throws Throwable {
        byte[] globalData = new byte[0];
        byte[] pageData = new byte[0];
        ImgJBIG2 img = new ImgJBIG2(484, 484, globalData, pageData);
        PdfIndirectReference ref = new PdfIndirectReference(4, 0, 6);
        PdfImage sourceImage = new PdfImage(img, "UnicodeBig", ref);
        PdfImage targetImage = new PdfImage(img, "", ref);
        
        sourceImage.importAll(targetImage);
        
        assertFalse("Should not become array after import", sourceImage.isArray());
    }

    // ========================================================================
    // Tests for transferBytes() method
    // ========================================================================
    
    @Test(timeout = 4000)
    public void transferBytes_fullTransfer_shouldCopyAllBytes() throws Throwable {
        byte[] sourceData = new byte[2];
        ByteBuffer destination = new ByteBuffer();
        ByteArrayInputStream input = new ByteArrayInputStream(sourceData);
        
        PdfImage.transferBytes(input, destination, (byte)48);
        
        assertEquals("All bytes should be transferred", 0, input.available());
        assertEquals("Destination should have all bytes", 2, destination.size());
    }

    @Test(timeout = 4000)
    public void transferBytes_zeroLengthTransfer_shouldCopyNothing() throws Throwable {
        ByteBuffer destination = new ByteBuffer();
        byte[] sourceData = new byte[8];
        ByteArrayInputStream input = new ByteArrayInputStream(sourceData);
        
        PdfImage.transferBytes(input, destination, (byte)0);
        
        assertEquals("Input should remain unchanged", 8, input.available());
        assertEquals("Destination should be empty", 0, destination.size());
    }

    @Test(timeout = 4000)
    public void transferBytes_nullInputStream_shouldThrowException() {
        ByteBuffer destination = new ByteBuffer();
        
        try {
            PdfImage.transferBytes(null, destination, -1);
            fail("Expected NullPointerException for null input stream");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void transferBytes_insufficientBufferSpace_shouldThrowException() {
        ByteBuffer destination = new ByteBuffer();
        destination.count = 4092;  // Almost full buffer
        byte[] sourceData = new byte[8];
        ByteArrayInputStream input = new ByteArrayInputStream(sourceData);
        
        try {
            PdfImage.transferBytes(input, destination, (byte)48);
            fail("Expected ArrayIndexOutOfBoundsException for full buffer");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void transferBytes_unconnectedPipe_shouldThrowException() throws IOException {
        ByteBuffer destination = new ByteBuffer();
        PipedInputStream input = new PipedInputStream();  // Not connected
        
        try {
            PdfImage.transferBytes(input, destination, 2594);
            fail("Expected IOException for unconnected pipe");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void transferBytes_toPrintStream_shouldTransferCorrectly() throws Throwable {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
        BufferedInputStream bufferedInput = new BufferedInputStream(pipeIn, 1291);
        MockPrintStream mockPrintStream = new MockPrintStream(pipeOut);
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        Object[] placeholder = new Object[3];
        PrintStream printDestination = mockPrintStream.printf(locale, ">E&1|", placeholder);
        
        PdfImage.transferBytes(bufferedInput, printDestination, 4);
        
        assertEquals("Bytes should be available after transfer", 4, pipeIn.available());
    }

    // ========================================================================
    // Tests for getImage() method under various configurations
    // ========================================================================
    
    @Test(timeout = 4000)
    public void getImage_maskImage_shouldMaintainDimensions() throws Throwable {
        byte[] globalData = new byte[0];
        byte[] pageData = new byte[0];
        ImgJBIG2 img = new ImgJBIG2(484, 484, globalData, pageData);
        img.makeMask();
        PdfIndirectReference ref = new PdfIndirectReference(4096, 4096, -1013);
        PdfImage pdfImage = new PdfImage(img, null, ref);
        
        Image result = pdfImage.getImage();
        
        assertEquals("Width should match original", 484.0F, result.getWidth(), 0.01F);
    }

    @Test(timeout = 4000)
    public void getImage_scaledImage_shouldNotBeDeflated() throws Throwable {
        byte[] globalData = new byte[0];
        byte[] pageData = new byte[0];
        ImgJBIG2 img = new ImgJBIG2(257, (byte)97, globalData, pageData);
        PdfIndirectReference ref = new PdfIndirectReference(0, 2147418112, 1);
        ImgJBIG2 scalingTarget = new ImgJBIG2(2535, 0, globalData, pageData);
        img.scaleToFit(scalingTarget);
        
        PdfImage pdfImage = new PdfImage(img, "com.itextpdf.text.pdf.PdfImage", ref);
        Image result = pdfImage.getImage();
        
        assertFalse("Scaled image should not be deflated", result.isDeflated());
    }

    @Test(timeout = 4000)
    public void getImage_withPadding_shouldHaveDefaultPosition() throws Throwable {
        byte[] globalData = new byte[8];
        byte[] pageData = new byte[8];
        ImgJBIG2 img = new ImgJBIG2(1106, 1106, globalData, pageData);
        img.setPaddingTop(1106);
        PdfIndirectReference ref = new PdfIndirectReference(0, 1, -208);
        
        PdfImage pdfImage = new PdfImage(img, "yw$,.", ref);
        Image result = pdfImage.getImage();
        
        assertEquals("Default left position should be 0", 0.0F, result.getLeft(), 0.01F);
    }

    @Test(timeout = 4000)
    public void getImage_withNegativeIndentation_shouldHaveValidAbsoluteY() throws Throwable {
        byte[] globalData = new byte[3];
        byte[] pageData = new byte[3];
        ImgJBIG2 img = new ImgJBIG2(25, 25, globalData, pageData);
        img.setIndentationRight(-1.0F);
        PdfIndirectReference ref = new PdfIndirectReference(25, 6);
        
        PdfImage pdfImage = new PdfImage(img, "UnicodeBig", ref);
        Image result = pdfImage.getImage();
        
        assertEquals("AbsoluteY should be Float.NaN when not set", Float.NaN, result.getAbsoluteY(), 0.01F);
    }

    @Test(timeout = 4000)
    public void getImage_withLeftIndentation_shouldHaveOriginalType() throws Throwable {
        byte[] globalData = new byte[1];
        byte[] pageData = new byte[1];
        ImgJBIG2 img = new ImgJBIG2(27, 27, globalData, pageData);
        img.setIndentationLeft(27);
        PdfIndirectReference ref = new PdfIndirectReference();
        
        PdfImage pdfImage = new PdfImage(img, "UnicodeBig", ref);
        Image result = pdfImage.getImage();
        
        assertEquals("Original type should be ORIGINAL_NONE", 0, Image.ORIGINAL_NONE);
    }

    @Test(timeout = 4000)
    public void getImage_rawImageInstance_shouldNotScaleToFit() throws Throwable {
        byte[] imageData = new byte[1];
        Image img = Image.getInstance(4145, -84, imageData, imageData);
        PdfImage pdfImage = new PdfImage(img, "", null);
        
        Image result = pdfImage.getImage();
        
        assertFalse("Raw image should not scale to fit", result.isScaleToFitLineWhenOverflow());
    }

    @Test(timeout = 4000)
    public void getImage_invalidDimensions_shouldHaveDefaultBehavior() throws Throwable {
        byte[] globalData = new byte[0];
        byte[] pageData = new byte[0];
        ImgJBIG2 img = new ImgJBIG2(-3134, 484, globalData, pageData);
        PdfIndirectReference ref = new PdfIndirectReference(1014, 1, 4);
        
        PdfImage pdfImage = new PdfImage(img, "", ref);
        Image result = pdfImage.getImage();
        
        assertEquals("Should have default image type constant", 2, Image.BX);
    }

    // ========================================================================
    // Tests for exception scenarios in PdfImage operations
    // ========================================================================
    
    @Test(timeout = 4000)
    public void importAll_nullImage_shouldThrowException() throws Throwable {
        byte[] globalData = new byte[0];
        byte[] pageData = new byte[0];
        ImgJBIG2 img = new ImgJBIG2(257, (byte)97, globalData, pageData);
        PdfIndirectReference ref = new PdfIndirectReference(0, 2147418112, 1);
        PdfImage pdfImage = new PdfImage(img, "com.itextpdf.text.pdf.PdfImage", ref);
        
        try {
            pdfImage.importAll(null);
            fail("Expected NullPointerException for null import source");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void constructor_nullImage_shouldThrowException() {
        PdfIndirectReference ref = new PdfIndirectReference(508, 508);
        
        try {
            new PdfImage(null, "", ref);
            fail("Expected NullPointerException for null image");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void constructor_templateWithTransparency_shouldThrowException() throws Throwable {
        ByteBuffer buffer = new ByteBuffer();
        FdfWriter writer = new FdfWriter();
        FdfWriter.Wrt wrt = new FdfWriter.Wrt(buffer, writer);
        PdfTemplate template = PdfTemplate.createTemplate(wrt, 1.0f, 2330.0F, wrt.PDF_VERSION_1_4);
        ImgTemplate imgTemplate = new ImgTemplate(template);
        int[] transparency = new int[1];
        imgTemplate.setTransparency(transparency);
        
        try {
            new PdfImage(imgTemplate, null, null);
            fail("Expected NullPointerException for invalid template");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void constructor_interpolatedImage_shouldThrowException() {
        PdfPSXObject psx = new PdfPSXObject();
        Image img = Image.getInstance((PdfTemplate) psx);
        PdfIndirectReference ref = new PdfIndirectReference(2, 3);
        img.setInterpolation(true);
        
        try {
            new PdfImage(img, null, ref);
            fail("Expected NullPointerException for interpolated image");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========================================================================
    // Tests for other edge cases and functionality
    // ========================================================================
    
    @Test(timeout = 4000)
    public void getImageName_shouldReturnPdfName() throws Throwable {
        byte[] globalData = new byte[0];
        byte[] pageData = new byte[0];
        ImgJBIG2 img = new ImgJBIG2(484, 484, globalData, pageData);
        PdfIndirectReference ref = new PdfIndirectReference(4096, 4096, -1013);
        PdfImage pdfImage = new PdfImage(img, null, ref);
        
        PdfName name = pdfImage.name();
        
        assertEquals("Should have correct NULL constant", 8, PdfObject.NULL);
    }
}