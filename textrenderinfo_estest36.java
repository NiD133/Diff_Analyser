package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;

/**
 * This test class contains tests for the {@link TextRenderInfo} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class TextRenderInfo_ESTestTest36 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that {@link TextRenderInfo#getText()} throws an
     * {@link IllegalCharsetNameException} when the underlying {@link PdfString}
     * was constructed with an invalid character set name.
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void getTextShouldThrowExceptionForInvalidCharsetName() {
        // Arrange: Set up a TextRenderInfo object with a PdfString that has an invalid encoding.
        final String DUMMY_TEXT_CONTENT = ".notdef";
        // This path-like string is not a valid Java charset name.
        final String INVALID_CHARSET_NAME = "com/itextpdf/text/pdf/fonts/";

        PdfString pdfStringWithInvalidEncoding = new PdfString(DUMMY_TEXT_CONTENT, INVALID_CHARSET_NAME);

        GraphicsState graphicsState = new GraphicsState();
        // A font is required by the GraphicsState for the TextRenderInfo constructor.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        Matrix textMatrix = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithInvalidEncoding,
                graphicsState,
                textMatrix,
                Collections.emptyList() // An empty list of marked content is sufficient for this test.
        );

        // Act: Attempt to get the text. This will trigger decoding using the invalid charset.
        textRenderInfo.getText();

        // Assert: The test expects an IllegalCharsetNameException, which is handled by the
        // @Test(expected = ...) annotation. If the exception is thrown, the test passes.
    }
}