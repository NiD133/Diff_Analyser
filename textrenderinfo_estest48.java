package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;

// The original class signature is maintained to preserve the test suite structure,
// but the test case itself has been rewritten for clarity.
public class TextRenderInfo_ESTestTest48 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getBaseline() throws IllegalCharsetNameException when the
     * TextRenderInfo is constructed with a PdfString that has an invalid encoding name.
     * <p>
     * The getBaseline() method's execution path involves decoding the PdfString,
     * which relies on Java's Charset.forName(). Providing an unrecognized encoding
     * name should result in the expected exception.
     */
    @Test(timeout = 4000, expected = IllegalCharsetNameException.class)
    public void getBaseline_whenPdfStringHasInvalidEncoding_throwsIllegalCharsetNameException() {
        // Arrange: Create a TextRenderInfo instance with a PdfString that has an invalid encoding.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // The encoding ".notdef" is not a valid Java charset name and is used to trigger the exception.
        String invalidEncoding = ".notdef";
        PdfString pdfStringWithInvalidEncoding = new PdfString("any-text", invalidEncoding);

        Matrix textMatrix = new Matrix();
        
        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithInvalidEncoding,
                graphicsState,
                textMatrix,
                Collections.emptySet()
        );

        // Act: Call the method under test. This action triggers the internal decoding of the PdfString.
        textRenderInfo.getBaseline();

        // Assert: The test is expected to throw an IllegalCharsetNameException.
        // This is handled declaratively by the @Test(expected = ...) annotation.
    }
}