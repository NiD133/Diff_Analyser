package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * This class contains tests for the {@link TextRenderInfo} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class TextRenderInfo_ESTestTest10 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that {@link TextRenderInfo#getDescentLine()} correctly returns a line segment
     * when the graphics state includes a non-zero 'rise' value. A non-zero rise
     * indicates a vertical shift from the baseline, which is common for superscripts.
     */
    @Test
    public void getDescentLine_whenGraphicsStateHasRise_returnsNotNull() {
        // Arrange: Set up a graphics state with a vertical text offset (rise).
        // This is the key condition for this test.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.rise = 1.0f; // A positive rise shifts the text upwards.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        // Create the other parameters required by the TextRenderInfo constructor.
        PdfString sampleText = new PdfString("any text");
        Matrix textMatrix = new Matrix(1.0f, 0.0f); // A simple identity-like matrix.

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                sampleText,
                graphicsState,
                textMatrix,
                Collections.<MarkedContentInfo>emptySet()
        );

        // Act: Calculate the descent line for the rendered text.
        LineSegment descentLine = textRenderInfo.getDescentLine();

        // Assert: The method should successfully compute a line segment.
        // This verifies that the 'rise' parameter is handled correctly without causing errors.
        assertNotNull("The descent line should be successfully calculated even when a text rise is applied.", descentLine);
    }
}