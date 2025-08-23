package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link LocationTextExtractionStrategy} class.
 */
public class LocationTextExtractionStrategyTest {

    /**
     * Verifies that the renderText method throws an IllegalCharsetNameException
     * when the provided TextRenderInfo contains a PdfString with an invalid encoding name.
     */
    @Test
    public void renderText_whenGivenTextWithInvalidEncoding_throwsIllegalCharsetNameException() {
        // Arrange
        final String invalidEncodingName = ".notdef";

        // Create a TextRenderInfo object containing a PdfString with an unsupported encoding.
        // The specific values for the font, matrix, and text content are not critical for this test,
        // as the exception occurs during character set lookup before text processing.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());

        PdfString textWithInvalidEncoding = new PdfString("any text", invalidEncodingName);
        Matrix textMatrix = new Matrix();

        TextRenderInfo renderInfo = new TextRenderInfo(
                textWithInvalidEncoding,
                graphicsState,
                textMatrix,
                Collections.emptyList()
        );

        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Act & Assert
        try {
            strategy.renderText(renderInfo);
            fail("Expected an IllegalCharsetNameException to be thrown due to invalid encoding.");
        } catch (IllegalCharsetNameException e) {
            // Verify that the exception was thrown for the correct reason.
            assertEquals("The exception should report the invalid charset name.",
                    invalidEncodingName, e.getCharsetName());
        }
    }
}