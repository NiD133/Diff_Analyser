package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collection;
import java.util.Stack;

import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Tests that {@link TextRenderInfo#getAscentLine()} can handle a horizontal scaling factor of zero
     * without failing or returning a null value.
     * <p>
     * In PDF rendering, a horizontal scaling of zero is a valid, though unusual, scenario.
     * The calculation for the ascent line involves this scaling factor, and this test ensures
     * the implementation is robust enough to handle this edge case.
     */
    @Test
    public void getAscentLine_whenHorizontalScalingIsZero_returnsNonNull() {
        // Arrange: Set up a graphics state with the specific condition under test.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.horizontalScaling = 0.0F;

        // A font is required for the TextRenderInfo calculations.
        // We can use a CMapAwareDocumentFont initialized with a default dictionary.
        PdfGState fontDictionary = new PdfGState();
        DocumentFont font = new CMapAwareDocumentFont(fontDictionary);
        graphicsState.font = font;

        // Other required parameters for the TextRenderInfo constructor.
        PdfString text = new PdfString("any text");
        Matrix textMatrix = new Matrix(); // A default identity matrix is sufficient.
        Collection<MarkedContentInfo> markedContentStack = new Stack<>();

        TextRenderInfo renderInfo = new TextRenderInfo(text, graphicsState, textMatrix, markedContentStack);

        // Act: Call the method under test.
        LineSegment ascentLine = renderInfo.getAscentLine();

        // Assert: Verify that the method returns a valid object, not null.
        assertNotNull("The ascent line should not be null even when horizontal scaling is zero.", ascentLine);
    }
}