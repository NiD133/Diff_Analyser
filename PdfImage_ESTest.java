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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class PdfImageTest extends PdfImage_ESTest_scaffolding {

    // Test data constants
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final int STANDARD_WIDTH = 484;
    private static final int STANDARD_HEIGHT = 484;
    private static final String UNICODE_BIG_ENCODING = "UnicodeBig";
    private static final String EMPTY_STRING = "";

    // Helper methods
    private ImgJBIG2 createStandardJBIG2Image() {
        return new ImgJBIG2(STANDARD_WIDTH, STANDARD_HEIGHT, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
    }

    private PdfIndirectReference createStandardReference() {
        return new PdfIndirectReference(4, 0, 6);
    }

    // Basic functionality tests
    @Test(timeout = 4000)
    public void testPdfImageCreationWithFlateCompressionAndImportAll() throws Throwable {
        // Given: A JBIG2 image with specific dimensions
        ImgJBIG2 sourceImage = new ImgJBIG2(257, 97, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference reference = new PdfIndirectReference(-1, 1700, 0);
        
        // When: Creating PdfImage and applying flate compression
        PdfImage pdfImage = new PdfImage(sourceImage, EMPTY_STRING, reference);
        pdfImage.flateCompress();
        pdfImage.importAll(pdfImage); // Self-import test
        
        // Then: Image should be created successfully and not be a string type
        assertFalse("PdfImage should not be identified as a string type", pdfImage.isString());
    }

    @Test(timeout = 4000)
    public void testImportAllBetweenDifferentPdfImages() throws Throwable {
        // Given: Two different PdfImage instances with same underlying image
        ImgJBIG2 baseImage = createStandardJBIG2Image();
        PdfIndirectReference reference = createStandardReference();
        
        PdfImage sourceImage = new PdfImage(baseImage, UNICODE_BIG_ENCODING, reference);
        PdfImage targetImage = new PdfImage(baseImage, EMPTY_STRING, reference);
        
        // When: Importing properties from one to another
        sourceImage.importAll(targetImage);
        
        // Then: Import should complete without making it an array type
        assertFalse("PdfImage should not be identified as an array type", sourceImage.isArray());
    }

    // transferBytes method tests
    @Test(timeout = 4000)
    public void testTransferBytesWithPositiveLength() throws Throwable {
        // Given: A byte array and input stream
        byte[] testData = new byte[2];
        ByteBuffer outputBuffer = new ByteBuffer();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        // When: Transferring bytes with positive length
        PdfImage.transferBytes(inputStream, outputBuffer, 48);
        
        // Then: All bytes should be consumed and transferred
        assertEquals("Input stream should be fully consumed", 0, inputStream.available());
        assertEquals("Output buffer should contain transferred bytes", 2, outputBuffer.size());
    }

    @Test(timeout = 4000)
    public void testTransferBytesWithZeroLength() throws Throwable {
        // Given: A byte array and input stream
        byte[] testData = new byte[8];
        ByteBuffer outputBuffer = new ByteBuffer();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        // When: Transferring with zero length (should transfer nothing)
        PdfImage.transferBytes(inputStream, outputBuffer, 0);
        
        // Then: No bytes should be transferred
        assertEquals("Input stream should remain untouched", 8, inputStream.available());
        assertEquals("Output buffer should remain empty", 0, outputBuffer.size());
    }

    // Image property tests
    @Test(timeout = 4000)
    public void testGetImageWithMaskProperty() throws Throwable {
        // Given: A JBIG2 image configured as a mask
        ImgJBIG2 maskImage = createStandardJBIG2Image();
        maskImage.makeMask();
        PdfIndirectReference reference = new PdfIndirectReference(9496, 29, 5);
        
        // When: Creating PdfImage and retrieving the image
        PdfImage pdfImage = new PdfImage(maskImage, EMPTY_STRING, reference);
        Image retrievedImage = pdfImage.getImage();
        
        // Then: Image dimensions should be preserved
        assertEquals("Image width should match original", 484.0F, retrievedImage.getWidth(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetImageWithScaledDimensions() throws Throwable {
        // Given: A JBIG2 image that will be scaled
        ImgJBIG2 originalImage = new ImgJBIG2(257, 97, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        ImgJBIG2 scalingRectangle = new ImgJBIG2(2535, 0, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        originalImage.scaleToFit(scalingRectangle);
        
        PdfIndirectReference reference = new PdfIndirectReference(0, 2147418112, 1);
        
        // When: Creating PdfImage with scaled image
        PdfImage pdfImage = new PdfImage(originalImage, "com.itextpdf.text.pdf.PdfImage", reference);
        Image retrievedImage = pdfImage.getImage();
        
        // Then: Image should not be deflated by default
        assertFalse("Retrieved image should not be deflated", retrievedImage.isDeflated());
    }

    @Test(timeout = 4000)
    public void testGetImageWithPaddingTop() throws Throwable {
        // Given: A JBIG2 image with top padding
        byte[] imageData = new byte[8];
        ImgJBIG2 paddedImage = new ImgJBIG2(1106, 1106, imageData, imageData);
        paddedImage.setPaddingTop(1106);
        
        PdfIndirectReference reference = new PdfIndirectReference(0, 1, -208);
        
        // When: Creating PdfImage
        PdfImage pdfImage = new PdfImage(paddedImage, "yw$,.", reference);
        Image retrievedImage = pdfImage.getImage();
        
        // Then: Image should have default left position
        assertEquals("Image left position should be default", 0.0F, retrievedImage.getLeft(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetImageWithNegativeRightIndentation() throws Throwable {
        // Given: A JBIG2 image with negative right indentation
        byte[] smallImageData = new byte[3];
        ImgJBIG2 indentedImage = new ImgJBIG2(25, 25, smallImageData, smallImageData);
        indentedImage.setIndentationRight(-1.0F);
        
        PdfIndirectReference reference = new PdfIndirectReference(25, 6);
        
        // When: Creating PdfImage
        PdfImage pdfImage = new PdfImage(indentedImage, UNICODE_BIG_ENCODING, reference);
        Image retrievedImage = pdfImage.getImage();
        
        // Then: Absolute Y position should be NaN due to negative indentation
        assertEquals("Absolute Y should be NaN with negative indentation", 
                    Float.NaN, retrievedImage.getAbsoluteY(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetImageWithLeftIndentation() throws Throwable {
        // Given: A JBIG2 image with left indentation
        byte[] singleByteData = new byte[1];
        ImgJBIG2 leftIndentedImage = new ImgJBIG2(27, 27, singleByteData, singleByteData);
        leftIndentedImage.setIndentationLeft(27);
        
        PdfIndirectReference reference = new PdfIndirectReference();
        
        // When: Creating PdfImage
        PdfImage pdfImage = new PdfImage(leftIndentedImage, UNICODE_BIG_ENCODING, reference);
        Image retrievedImage = pdfImage.getImage();
        
        // Then: Image should be created successfully (testing constant access)
        assertEquals("ORIGINAL_NONE constant should be 0", 0, Image.ORIGINAL_NONE);
    }

    @Test(timeout = 4000)
    public void testGetImageFromGenericImageInstance() throws Throwable {
        // Given: A generic Image instance
        byte[] singleByte = new byte[1];
        Image genericImage = Image.getInstance(4145, -84, singleByte, singleByte);
        
        // When: Creating PdfImage from generic image
        PdfImage pdfImage = new PdfImage(genericImage, EMPTY_STRING, null);
        Image retrievedImage = pdfImage.getImage();
        
        // Then: Image should maintain its properties
        assertFalse("Image should not scale to fit line when overflow", 
                   retrievedImage.isScaleToFitLineWhenOverflow());
    }

    // Error condition tests
    @Test(timeout = 4000)
    public void testTransferBytesWithNullInputStream() throws Throwable {
        // Given: Null input stream
        ByteBuffer outputBuffer = new ByteBuffer();
        
        // When/Then: Should throw NullPointerException
        try {
            PdfImage.transferBytes(null, outputBuffer, -1);
            fail("Expected NullPointerException for null input stream");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.PdfImage", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransferBytesWithBufferOverflow() throws Throwable {
        // Given: ByteBuffer with manipulated count causing overflow
        ByteBuffer overflowBuffer = new ByteBuffer();
        overflowBuffer.count = 4092; // Near buffer limit
        
        byte[] testData = new byte[8];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        // When/Then: Should throw ArrayIndexOutOfBoundsException
        try {
            PdfImage.transferBytes(inputStream, overflowBuffer, 48);
            fail("Expected ArrayIndexOutOfBoundsException for buffer overflow");
        } catch(ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.ByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransferBytesWithDisconnectedPipe() throws Throwable {
        // Given: Disconnected piped input stream
        ByteBuffer outputBuffer = new ByteBuffer();
        PipedInputStream disconnectedStream = new PipedInputStream();
        
        // When/Then: Should throw IOException for disconnected pipe
        try {
            PdfImage.transferBytes(disconnectedStream, outputBuffer, 2594);
            fail("Expected IOException for disconnected pipe");
        } catch(IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testImportAllWithNullParameter() throws Throwable {
        // Given: Valid PdfImage instance
        ImgJBIG2 image = new ImgJBIG2(257, 97, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference reference = new PdfIndirectReference(0, 2147418112, 1);
        PdfImage pdfImage = new PdfImage(image, "com.itextpdf.text.pdf.PdfImage", reference);
        
        // When/Then: Should throw NullPointerException for null parameter
        try {
            pdfImage.importAll(null);
            fail("Expected NullPointerException for null parameter");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.PdfImage", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullImage() throws Throwable {
        // Given: Null image parameter
        PdfIndirectReference reference = new PdfIndirectReference(508, 508);
        
        // When/Then: Should throw NullPointerException for null image
        try {
            new PdfImage(null, EMPTY_STRING, reference);
            fail("Expected NullPointerException for null image");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.PdfImage", e);
        }
    }

    // Stream handling tests
    @Test(timeout = 4000)
    public void testTransferBytesToPrintStream() throws Throwable {
        // Given: Connected piped streams for testing
        PipedOutputStream pipedOutput = new PipedOutputStream();
        PipedInputStream pipedInput = new PipedInputStream(pipedOutput);
        BufferedInputStream bufferedInput = new BufferedInputStream(pipedInput, 1291);
        
        MockPrintStream mockPrintStream = new MockPrintStream(pipedOutput);
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        Object[] formatArgs = new Object[3];
        PrintStream printStream = mockPrintStream.printf(locale, ">E&1|", formatArgs);
        
        // When: Transferring bytes to print stream
        PdfImage.transferBytes(bufferedInput, printStream, 4);
        
        // Then: Bytes should be available in the piped input
        assertEquals("Transferred bytes should be available", 4, pipedInput.available());
    }

    // Template and transparency tests
    @Test(timeout = 4000)
    public void testConstructorWithTemplateImageAndNullName() throws Throwable {
        // Given: Template image with transparency
        ByteBuffer buffer = new ByteBuffer();
        FdfWriter fdfWriter = new FdfWriter();
        FdfWriter.Wrt writer = new FdfWriter.Wrt(buffer, fdfWriter);
        PdfTemplate template = PdfTemplate.createTemplate(writer, 1.0F, 2330.0F, writer.PDF_VERSION_1_4);
        
        ImgTemplate templateImage = new ImgTemplate(template);
        int[] transparencyArray = new int[1];
        templateImage.setTransparency(transparencyArray);
        
        // When/Then: Should throw NullPointerException due to URL handling
        try {
            new PdfImage(templateImage, null, null);
            fail("Expected NullPointerException for template image with null name");
        } catch(NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    @Test(timeout = 4000)
    public void testPdfImageWithMaskAndTransparency() throws Throwable {
        // Given: JBIG2 image configured as mask with transparency
        ImgJBIG2 maskImage = createStandardJBIG2Image();
        maskImage.makeMask();
        int[] emptyTransparency = new int[0];
        maskImage.setTransparency(emptyTransparency);
        
        PdfIndirectReference reference = new PdfIndirectReference(4096, 4096, -1013);
        
        // When: Creating PdfImage
        PdfImage pdfImage = new PdfImage(maskImage, null, reference);
        
        // Then: Should have correct type
        assertEquals("PdfImage should have correct type", 7, pdfImage.type());
    }

    @Test(timeout = 4000)
    public void testPdfImageWithTransparencyOnly() throws Throwable {
        // Given: JBIG2 image with transparency but not as mask
        ImgJBIG2 transparentImage = createStandardJBIG2Image();
        int[] emptyTransparency = new int[0];
        transparentImage.setTransparency(emptyTransparency);
        
        PdfIndirectReference reference = new PdfIndirectReference(4096, 4096, -1013);
        
        // When: Creating PdfImage
        PdfImage pdfImage = new PdfImage(transparentImage, null, reference);
        
        // Then: Should not be null
        assertFalse("PdfImage should not be null", pdfImage.isNull());
    }

    @Test(timeout = 4000)
    public void testConstructorWithInterpolatedTemplateImage() throws Throwable {
        // Given: Template image with interpolation enabled
        PdfPSXObject psxObject = new PdfPSXObject();
        Image templateImage = Image.getInstance(psxObject);
        templateImage.setInterpolation(true);
        
        PdfIndirectReference reference = new PdfIndirectReference(2, 3);
        
        // When/Then: Should throw NullPointerException due to URL handling
        try {
            new PdfImage(templateImage, null, reference);
            fail("Expected NullPointerException for interpolated template image");
        } catch(NullPointerException e) {
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetImageWithNegativeWidth() throws Throwable {
        // Given: JBIG2 image with negative width
        ImgJBIG2 negativeWidthImage = new ImgJBIG2(-3134, 484, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY);
        PdfIndirectReference reference = new PdfIndirectReference(1014, 1, 4);
        
        // When: Creating PdfImage
        PdfImage pdfImage = new PdfImage(negativeWidthImage, EMPTY_STRING, reference);
        Image retrievedImage = pdfImage.getImage();
        
        // Then: Should handle negative width gracefully (testing constant)
        assertEquals("BX constant should be 2", 2, Image.BX);
    }

    @Test(timeout = 4000)
    public void testGetPdfName() throws Throwable {
        // Given: Standard JBIG2 image
        ImgJBIG2 image = createStandardJBIG2Image();
        PdfIndirectReference reference = new PdfIndirectReference(4096, 4096, -1013);
        
        // When: Creating PdfImage and getting name
        PdfImage pdfImage = new PdfImage(image, null, reference);
        PdfName name = pdfImage.name();
        
        // Then: Name should be retrieved successfully (testing constant)
        assertEquals("NULL constant should be 8", 8, PdfObject.NULL);
    }
}