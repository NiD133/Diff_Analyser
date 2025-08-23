package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNull;

public class TextRenderInfo_ESTestTest68 extends TextRenderInfo_ESTest_scaffolding {

    /**
     * Verifies that getStrokeColor() returns null when the stroke color has not been
     * explicitly set in the GraphicsState.
     */
    @Test
    public void getStrokeColor_shouldReturnNull_whenNotSetInGraphicsState() {
        // ARRANGE
        // A default GraphicsState does not have a stroke color defined.
        GraphicsState graphicsState = new GraphicsState();

        // The TextRenderInfo constructor requires a font to be set in the GraphicsState
        // to avoid a NullPointerException when it internally calls font.getFontMatrix().
        // We create a dummy font for this purpose.
        CMapAwareDocumentFont dummyFont = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = dummyFont;

        // Create the TextRenderInfo instance with the default graphics state.
        TextRenderInfo renderInfo = new TextRenderInfo(
                new PdfString(""),
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // ACT
        BaseColor strokeColor = renderInfo.getStrokeColor();

        // ASSERT
        assertNull("The stroke color should be null by default.", strokeColor);
    }
}