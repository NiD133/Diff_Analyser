package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that the getText() method throws an UnsupportedCharsetException
     * when the underlying PdfString was created with an encoding that is not
     * a valid or supported character set.
     */
    @Test
    public void getText_whenPdfStringHasUnsupportedEncoding_throwsUnsupportedCharsetException() {
        // Arrange
        // 1. Define an encoding name that is not a valid Java charset.
        // The PDF specification allows font encoding names that don't map to standard charsets.
        final String unsupportedEncoding = "Courier-Oblique";

        // 2. Set up the necessary objects to create a TextRenderInfo instance.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        PdfString pdfStringWithInvalidEncoding = new PdfString("some-text", unsupportedEncoding);

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                pdfStringWithInvalidEncoding,
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // Act & Assert
        try {
            textRenderInfo.getText();
            fail("Expected UnsupportedCharsetException was not thrown.");
        } catch (UnsupportedCharsetException e) {
            // The exception message from java.nio.charset.Charset is expected to be the invalid encoding name.
            assertEquals("The exception message should contain the unsupported encoding name.",
                    unsupportedEncoding, e.getMessage());
        }
    }
}