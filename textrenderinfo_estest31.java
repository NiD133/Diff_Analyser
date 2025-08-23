package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.UnsupportedCharsetException;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The test class structure (e.g., runner, scaffolding) is preserved from the original.
public class TextRenderInfo_ESTestTest31 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that methods requiring string decoding, like getUnscaledBaseline(),
     * throw an UnsupportedCharsetException when the underlying PdfString was created
     * with an encoding not supported by the standard Java Charset library.
     */
    @Test(timeout = 4000)
    public void getUnscaledBaseline_shouldThrowUnsupportedCharsetException_whenPdfStringHasUnsupportedEncoding() {
        // Arrange: Set up the necessary objects to create a TextRenderInfo instance.
        GraphicsState graphicsState = new GraphicsState();
        // A font is a required part of the graphics state for text operations.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        Matrix textMatrix = new Matrix();
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        // "Identity-H" is a PDF-specific encoding for horizontal writing, which is not a standard Java charset.
        // This is expected to cause an exception when the string content needs to be decoded.
        String unsupportedEncoding = "Identity-H";
        PdfString pdfStringWithUnsupportedEncoding = new PdfString("test content", unsupportedEncoding);

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithUnsupportedEncoding,
                graphicsState,
                textMatrix,
                markedContentStack
        );

        // Act & Assert: Verify that calling a method that decodes the string throws the correct exception.
        try {
            textRenderInfo.getUnscaledBaseline();
            fail("Expected an UnsupportedCharsetException because the encoding '" + unsupportedEncoding + "' is not supported.");
        } catch (UnsupportedCharsetException e) {
            // The exception is expected. Verify its property for correctness.
            assertEquals("The exception should report the name of the unsupported charset.",
                         unsupportedEncoding, e.getCharsetName());
        }
    }
}