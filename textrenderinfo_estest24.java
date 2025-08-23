package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link TextRenderInfo}.
 */
public class TextRenderInfoTest {

    @Test
    public void getFillColor_shouldReturnColorFromGraphicsState() {
        // Arrange: Set up the graphics state with a specific fill color.
        GraphicsState graphicsState = new GraphicsState();
        BaseColor expectedFillColor = BaseColor.GRAY;
        graphicsState.fillColor = expectedFillColor;

        // The TextRenderInfo constructor requires a font to be set in the graphics state.
        // We can use a default font for this test.
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = font;

        // Provide other minimal, valid arguments for the constructor.
        PdfString dummyText = new PdfString("any text");
        Matrix textMatrix = new Matrix();

        // Act: Create the TextRenderInfo and get the fill color.
        TextRenderInfo textRenderInfo = new TextRenderInfo(dummyText, graphicsState, textMatrix, Collections.emptyList());
        BaseColor actualFillColor = textRenderInfo.getFillColor();

        // Assert: Verify that the returned color matches the one set in the graphics state.
        assertEquals(expectedFillColor, actualFillColor);
    }
}