package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class TextRenderInfo_ESTestTest18 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Tests that getSingleSpaceWidth() correctly calculates the width
     * based on the absolute value of the font size, even when the font size is negative.
     */
    @Test
    public void getSingleSpaceWidth_withNegativeFontSize_returnsAbsoluteValue() {
        // Arrange
        // Define a negative font size to test if the method correctly handles it.
        final float negativeFontSize = -744.2958F;
        final float expectedWidth = Math.abs(negativeFontSize);

        // Set up the graphics state with a mock font and the negative font size.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.fontSize = negativeFontSize;

        // Create a TextRenderInfo instance. The specific PDF string, matrix, and
        // marked content are not relevant for this particular calculation.
        TextRenderInfo textRenderInfo = new TextRenderInfo(
                new PdfString(""),
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // Act
        float actualWidth = textRenderInfo.getSingleSpaceWidth();

        // Assert
        // The single space width should be the absolute value of the font size.
        assertEquals("The single space width should be the absolute value of the font size.",
                expectedWidth, actualWidth, 0.01F);
    }
}