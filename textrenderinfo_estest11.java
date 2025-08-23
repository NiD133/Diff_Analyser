package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * This class contains improved tests for the TextRenderInfo class.
 * The original test was an auto-generated one (TextRenderInfo_ESTestTest11)
 * that was functionally correct but difficult to comprehend.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getAscentLine() returns a non-null LineSegment
     * when the graphics state includes a non-default text 'rise'.
     *
     * The 'rise' parameter in a PDF's graphics state specifies a vertical
     * shift from the baseline, often used for superscripts or subscripts.
     * This test ensures that this vertical shift is handled correctly
     * without causing errors during the ascent line calculation.
     */
    @Test
    public void getAscentLine_withNonZeroTextRise_returnsNotNull() {
        // ARRANGE: Set up a GraphicsState with a font and a non-zero 'rise' value.
        // This simulates rendering text that is vertically shifted from the baseline.
        final float textRise = 1296.0F;
        
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.rise = textRise;
        
        // A font must be set in the graphics state for text operations.
        // We use a CMapAwareDocumentFont with a default PdfGState for this setup.
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        // Create the other necessary parameters for the TextRenderInfo constructor.
        PdfString text = new PdfString("");
        Matrix ctm = graphicsState.getCtm(); // Current Transformation Matrix
        List<MarkedContentInfo> markedContentInfo = Collections.emptyList();

        TextRenderInfo renderInfo = new TextRenderInfo(text, graphicsState, ctm, markedContentInfo);

        // ACT: Call the method under test to calculate the ascent line.
        LineSegment ascentLine = renderInfo.getAscentLine();

        // ASSERT: Verify that the calculation completes successfully and returns a valid object.
        // A null return value would indicate a potential issue in handling the text 'rise'.
        assertNotNull("The ascent line should be successfully calculated even when a text rise is applied.", ascentLine);
    }
}