package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that {@link TextRenderInfo#getAscentLine()} returns a non-null
     * {@link LineSegment} when the instance is properly configured with a font.
     * <p>
     * The ascent line calculation depends on font metrics, so ensuring a font
     * is present in the {@link GraphicsState} is the key prerequisite for this test.
     */
    @Test
    public void getAscentLine_whenFontIsAvailable_shouldReturnNonNullSegment() {
        // Arrange: Create a GraphicsState and assign a font to it. This is essential
        // for calculating text metrics like the ascent line.
        GraphicsState graphicsState = new GraphicsState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        // Create the other parameters needed for the TextRenderInfo constructor.
        PdfString text = new PdfString(" ");
        Matrix textMatrix = graphicsState.getCtm();
        Collection<MarkedContentInfo> markedContentInfo = new ArrayList<>();

        TextRenderInfo renderInfo = new TextRenderInfo(text, graphicsState, textMatrix, markedContentInfo);

        // Act: Call the method under test.
        LineSegment ascentLine = renderInfo.getAscentLine();

        // Assert: The returned LineSegment should be a valid object, not null.
        assertNotNull("The ascent line should not be null when a font is available in the graphics state.", ascentLine);
    }
}