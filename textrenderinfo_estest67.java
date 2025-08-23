package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getDescentLine() returns a valid LineSegment object
     * when the TextRenderInfo is constructed with a properly initialized GraphicsState.
     * This ensures the method does not throw an exception or return null under normal conditions.
     */
    @Test
    public void getDescentLine_withValidFont_returnsNonNull() {
        // Arrange: Create a GraphicsState with a valid, non-null font, which is
        // essential for calculating text metrics.
        GraphicsState graphicsState = new GraphicsState();
        CMapAwareDocumentFont documentFont = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = documentFont;

        // Arrange: Set up the other required parameters for the TextRenderInfo constructor.
        Matrix textMatrix = new Matrix();
        PdfString text = new PdfString("test");
        TextRenderInfo textRenderInfo = new TextRenderInfo(text, graphicsState, textMatrix, Collections.emptyList());

        // Act: Call the method under test.
        LineSegment descentLine = textRenderInfo.getDescentLine();

        // Assert: The returned LineSegment should be a valid object, not null.
        assertNotNull("The descent line should not be null when a valid font is provided.", descentLine);
    }
}