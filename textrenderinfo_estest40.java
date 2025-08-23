package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;

/**
 * This test class contains tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getDescentLine() throws an IllegalCharsetNameException
     * when the underlying PdfString was created with an invalid character encoding.
     *
     * The text rendering process requires decoding the PdfString, which fails
     * if the specified encoding name is not a valid charset.
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void getDescentLine_whenPdfStringHasInvalidEncoding_throwsIllegalCharsetNameException() {
        // --- Arrange ---
        // Create a PdfString with an encoding that is not a valid Java charset name.
        // ".notdef" is a special name in PDF fonts but is not a valid charset.
        String invalidEncoding = ".notdef";
        PdfString pdfStringWithInvalidEncoding = new PdfString("any text", invalidEncoding);

        // Set up the minimum required dependencies to construct a TextRenderInfo instance.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        Matrix textMatrix = graphicsState.ctm;

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithInvalidEncoding,
                graphicsState,
                textMatrix,
                Collections.emptyList()
        );

        // --- Act ---
        // This call is expected to trigger the decoding of the PdfString,
        // which will fail due to the invalid encoding.
        textRenderInfo.getDescentLine();

        // --- Assert ---
        // The test will pass if the expected IllegalCharsetNameException is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}