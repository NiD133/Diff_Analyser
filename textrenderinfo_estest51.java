package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TextRenderInfo_ESTestTest51 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that methods attempting to decode a PdfString will throw
     * an UnsupportedCharsetException if the string was created with an
     * encoding that is not a valid Java charset.
     */
    @Test(timeout = 4000)
    public void getAscentLine_whenPdfStringHasUnsupportedEncoding_throwsUnsupportedCharsetException() {
        // ARRANGE
        // Define an encoding that is not a valid Java charset name.
        // The iText library will attempt to use Java's charset decoders for unknown encodings.
        final String unsupportedEncoding = "Times-BoldItalic";

        // Create a PdfString with this unsupported encoding. The actual text content is not critical.
        PdfString pdfStringWithUnsupportedEncoding = new PdfString("some-text", unsupportedEncoding);

        // Set up the minimal required GraphicsState and other parameters for TextRenderInfo.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        Matrix ctm = graphicsState.getCtm();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithUnsupportedEncoding,
                graphicsState,
                ctm,
                Collections.emptyList()
        );

        // ACT & ASSERT
        try {
            // The getAscentLine() method internally needs to decode the PdfString to calculate
            // its metrics, which triggers the exception.
            textRenderInfo.getAscentLine();
            fail("Expected an UnsupportedCharsetException to be thrown due to the invalid encoding.");
        } catch (UnsupportedCharsetException e) {
            // Verify that the exception was thrown for the correct, unsupported charset.
            assertEquals(unsupportedEncoding, e.getCharsetName());
        }
    }
}