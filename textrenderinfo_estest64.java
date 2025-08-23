package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link TextRenderInfo} class.
 */
// The original test class name "TextRenderInfo_ESTestTest64" was renamed for clarity.
// The scaffolding class is retained as it might contain necessary setup.
public class TextRenderInfoTest extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getFillColor() returns null when no fill color has been
     * set in the associated GraphicsState.
     */
    @Test
    public void getFillColor_shouldReturnNull_whenNotSetInGraphicsState() {
        // Arrange: Create a GraphicsState without explicitly setting a fill color.
        // The TextRenderInfo constructor requires a font, so we provide a minimal one.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        Matrix textMatrix = new Matrix();
        ArrayList<MarkedContentInfo> markedContent = new ArrayList<>();

        TextRenderInfo renderInfo = new TextRenderInfo(null, graphicsState, textMatrix, markedContent);

        // Act: Retrieve the fill color from the TextRenderInfo object.
        BaseColor fillColor = renderInfo.getFillColor();

        // Assert: The fill color should be null, as it defaults to the GraphicsState's value.
        assertNull("Expected the fill color to be null when not explicitly set.", fillColor);
    }
}