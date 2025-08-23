package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;

/**
 * This test verifies the behavior of the TextRenderInfo class when handling
 * text with an unsupported character encoding.
 */
public class TextRenderInfoRefactoredTest {

    /**
     * Tests that getUnscaledWidth() throws an UnsupportedCharsetException
     * when the underlying PdfString was created with an encoding that is not
     * a valid or supported charset name.
     *
     * The method getUnscaledWidth() internally needs to decode the string to
     * calculate its width, which fails if the encoding is invalid.
     */
    @Test(expected = UnsupportedCharsetException.class)
    public void getUnscaledWidth_whenPdfStringHasUnsupportedEncoding_throwsUnsupportedCharsetException() {
        // ARRANGE: Set up a TextRenderInfo object containing a PdfString that has an
        // invalid encoding. "Courier-Bold" is a font name, not a valid Java charset.
        final String unsupportedEncoding = "Courier-Bold";
        PdfString textWithUnsupportedEncoding = new PdfString("some text", unsupportedEncoding);

        GraphicsState graphicsState = new GraphicsState();
        // A font must be set in the graphics state for TextRenderInfo construction.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        Matrix textMatrix = new Matrix();

        TextRenderInfo renderInfo = new TextRenderInfo(
                textWithUnsupportedEncoding,
                graphicsState,
                textMatrix,
                Collections.emptyList() // An empty list of marked content is sufficient for this test.
        );

        // ACT: Attempt to get the unscaled width. This action is expected to trigger
        // the decoding of the string, which will fail.
        renderInfo.getUnscaledWidth();

        // ASSERT: The test passes if an UnsupportedCharsetException is thrown,
        // which is handled by the @Test(expected = ...) annotation.
    }
}