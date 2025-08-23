package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link TextRenderInfo} class.
 * Note: The original class name "TextRenderInfo_ESTestTest13" was auto-generated
 * and has been renamed to reflect its purpose better.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getBaseline() returns a valid LineSegment when the graphics state
     * includes a vertical text offset, known as 'rise'. This is a common scenario
     * for rendering superscripts.
     */
    @Test
    public void getBaseline_shouldReturnNonNull_whenGraphicsStateHasRise() {
        // ARRANGE: Create a graphics state with a non-zero 'rise' value to simulate
        // text being rendered above the normal baseline (e.g., a superscript).
        final float textRise = 2.0f;
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.rise = textRise;
        // A font is required for TextRenderInfo's internal calculations.
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());

        PdfString sampleText = new PdfString("Test");
        Matrix initialTextMatrix = graphicsState.getCtm();

        TextRenderInfo renderInfo = new TextRenderInfo(
                sampleText,
                graphicsState,
                initialTextMatrix,
                Collections.emptyList() // Marked content is not relevant for this test.
        );

        // ACT: Call the method under test to get the text's baseline.
        LineSegment baseline = renderInfo.getBaseline();

        // ASSERT: Ensure a valid LineSegment was returned.
        // This confirms the calculation handles the 'rise' value correctly without errors.
        assertNotNull("The calculated baseline should not be null when a text rise is applied.", baseline);
    }
}