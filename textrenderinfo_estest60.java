package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getAscentLine() returns a non-null LineSegment
     * when the TextRenderInfo is constructed with a valid GraphicsState
     * that includes a font.
     * <p>
     * This is a basic sanity check to ensure the method doesn't crash
     * with minimal, valid input, as font metrics are required for the calculation.
     */
    @Test
    public void getAscentLine_withValidFont_returnsNonNull() {
        // Arrange: Create a GraphicsState with a valid font, which is a prerequisite
        // for calculating line segments.
        GraphicsState graphicsState = new GraphicsState();
        DocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        // Arrange: Set up the other required parameters for the TextRenderInfo constructor.
        PdfString emptyText = new PdfString();
        Matrix identityMatrix = new Matrix();
        Collection<MarkedContentInfo> emptyMarkedContent = new ArrayList<>();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                emptyText,
                graphicsState,
                identityMatrix,
                emptyMarkedContent
        );

        // Act: Call the method under test.
        LineSegment ascentLine = textRenderInfo.getAscentLine();

        // Assert: The returned line segment should not be null.
        assertNotNull("The ascent line should be successfully calculated when a font is available.", ascentLine);
    }
}