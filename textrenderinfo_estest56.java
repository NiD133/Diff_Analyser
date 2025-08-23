package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotEquals;

public class TextRenderInfo_ESTestTest56 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getAscentLine() and getUnscaledBaseline() return distinct line segments.
     * <p>
     * The ascent line represents the top of the characters and should be positioned
     * above the baseline, which is where the characters "sit". This test confirms
     * they are not the same.
     */
    @Test
    public void getAscentLine_shouldReturnDifferentSegmentThanUnscaledBaseline() {
        // Arrange: Set up a TextRenderInfo object with a valid font.
        // A font is required because ascent metrics are read from it.
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfDictionary());

        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = font;

        PdfString text = new PdfString("Test");
        Matrix textMatrix = new Matrix(); // An identity matrix is sufficient.

        TextRenderInfo textRenderInfo = new TextRenderInfo(text, graphicsState, textMatrix, Collections.emptyList());

        // Act: Retrieve the baseline and ascent line for the rendered text.
        LineSegment unscaledBaseline = textRenderInfo.getUnscaledBaseline();
        LineSegment ascentLine = textRenderInfo.getAscentLine();

        // Assert: The ascent line and baseline should not be equal. The ascent line is
        // calculated using the font's ascent property, placing it at a different
        // vertical position than the baseline.
        assertNotEquals("The ascent line should be distinct from the unscaled baseline.",
                unscaledBaseline, ascentLine);
    }
}