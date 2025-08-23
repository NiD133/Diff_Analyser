package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;

/**
 * Contains unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that methods attempting to process text content (e.g., getUnscaledBaseline)
     * throw an IllegalCharsetNameException if the underlying PdfString was constructed
     * with a syntactically invalid character set name.
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void getTextBasedMetricsWithIllegalCharsetNameThrowsException() {
        // Arrange: Create a PdfString with an encoding name that is syntactically invalid,
        // as it contains characters not permitted by the Charset specification.
        final String ILLEGAL_CHARSET_NAME = "i?iZea`|";
        PdfString pdfStringWithIllegalEncoding = new PdfString("some-text", ILLEGAL_CHARSET_NAME);

        // Set up the minimum required dependencies for TextRenderInfo. The GraphicsState
        // and its font are necessary because text decoding is delegated through them.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // The constructor also requires a transformation matrix and a collection of marked content.
        Matrix textMatrix = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithIllegalEncoding,
                graphicsState,
                textMatrix,
                Collections.emptyList()
        );

        // Act: Call a method that requires decoding the string. This will trigger the
        // underlying call to Java's charset functionality, which will fail.
        textRenderInfo.getUnscaledBaseline();

        // Assert: The @Test(expected) annotation handles the verification that an
        // IllegalCharsetNameException was thrown.
    }
}