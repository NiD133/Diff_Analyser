package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the TextRenderInfo class.
 * Note: The original test was auto-generated and has been refactored for clarity.
 */
public class TextRenderInfo_ESTestTest16 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that getUnscaledWidth() correctly calculates the total width of a string
     * by factoring in the character spacing.
     *
     * <p>The test assumes a mock font where individual character widths are zero.
     * Therefore, the total unscaled width is expected to be the number of characters
     * multiplied by the character spacing value.</p>
     */
    @Test
    public void getUnscaledWidth_withCharacterSpacing_returnsCorrectTotalWidth() {
        // ARRANGE
        final float CHARACTER_SPACING = 7.0f;
        final String TEXT_CONTENT = "Test Text"; // 9 characters
        final float EXPECTED_WIDTH = CHARACTER_SPACING * TEXT_CONTENT.length(); // 7.0f * 9 = 63.0f

        // Set up a graphics state with a mock font and the specified character spacing.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState()); // A mock font with default (zero) widths.
        graphicsState.characterSpacing = CHARACTER_SPACING;

        PdfString textAsPdfString = new PdfString(TEXT_CONTENT);
        Matrix identityMatrix = new Matrix();

        TextRenderInfo renderInfo = new TextRenderInfo(
                textAsPdfString,
                graphicsState,
                identityMatrix,
                Collections.emptyList()
        );

        // ACT
        float actualWidth = renderInfo.getUnscaledWidth();

        // ASSERT
        assertEquals("The unscaled width should be the sum of character spacings",
                EXPECTED_WIDTH, actualWidth, 0.01f);
    }
}