package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TextRenderInfo_ESTestTest47 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that getBaseline() throws an UnsupportedCharsetException when the underlying
     * PdfString is initialized with an encoding that is not a valid Java charset name.
     * The getBaseline() method triggers text decoding, which exposes the encoding issue.
     */
    @Test(timeout = 4000)
    public void getBaseline_whenPdfStringHasUnsupportedEncoding_throwsUnsupportedCharsetException() {
        // Arrange: Set up a GraphicsState and a PdfString with an invalid encoding.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // "Times-Italic" is a valid PDF encoding name but not a standard Java Charset name.
        // This will cause an exception when iText tries to decode the string.
        String invalidEncoding = "Times-Italic";
        PdfString pdfStringWithInvalidEncoding = new PdfString("some text", invalidEncoding);

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithInvalidEncoding,
                graphicsState,
                graphicsState.ctm,
                Collections.emptySet()
        );

        // Act & Assert: Verify that calling getBaseline() throws the expected exception.
        try {
            textRenderInfo.getBaseline();
            fail("Expected an UnsupportedCharsetException to be thrown due to the invalid encoding.");
        } catch (UnsupportedCharsetException e) {
            // The exception should correctly identify the problematic charset name.
            assertEquals(invalidEncoding, e.getCharsetName());
        }
    }
}