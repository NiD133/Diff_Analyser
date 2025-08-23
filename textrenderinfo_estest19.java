package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getPdfString() correctly returns the null value
     * that was passed to the TextRenderInfo constructor.
     */
    @Test
    public void getPdfStringShouldReturnNullWhenConstructedWithNull() {
        // Arrange: Set up the necessary dependencies for TextRenderInfo.
        // The key input for this test is a null PdfString.
        GraphicsState graphicsState = new GraphicsState();
        
        // The font is a required dependency and cannot be null.
        DocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        Matrix textMatrix = new Matrix();
        Collection<MarkedContentInfo> markedContent = Collections.emptyList();
        
        // Act: Construct the TextRenderInfo with a null PdfString and then retrieve it.
        TextRenderInfo textRenderInfo = new TextRenderInfo(null, graphicsState, textMatrix, markedContent);
        PdfString actualPdfString = textRenderInfo.getPdfString();

        // Assert: The retrieved PdfString should be null, matching the input.
        assertNull("Expected getPdfString() to return null when the object was constructed with a null PdfString.", actualPdfString);
    }
}