package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LocationTextExtractionStrategy_ESTestTest31 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that rendering text with an empty string does not add any content
     * to the resultant text, even when called multiple times.
     */
    @Test
    public void getResultantText_whenRenderingEmptyText_returnsEmptyString() {
        // Arrange: Set up the necessary objects for the test.
        // We create a TextRenderInfo object that represents an empty string.
        // The specific values for graphics state, matrix, and font are not critical for this test,
        // as we are focused on the behavior with empty text content.
        PdfString emptyText = new PdfString("");
        GraphicsState graphicsState = new GraphicsState();
        
        // A font must be set in the GraphicsState for TextRenderInfo construction.
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());
        
        Matrix textMatrix = new Matrix();
        List<MarkedContentInfo> markedContentStack = Collections.emptyList();

        TextRenderInfo emptyTextRenderInfo = new TextRenderInfo(emptyText, graphicsState, textMatrix, markedContentStack);
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Act: Perform the action under test.
        // Render the empty text twice to ensure no side effects like adding newlines or spaces.
        strategy.renderText(emptyTextRenderInfo);
        strategy.renderText(emptyTextRenderInfo);

        String actualText = strategy.getResultantText();

        // Assert: Verify the outcome.
        assertEquals("The resultant text should be empty after rendering empty strings.", "", actualText);
    }
}