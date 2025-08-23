package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that the getFont() method correctly returns the font instance
     * that was provided via the GraphicsState during the object's construction.
     */
    @Test
    public void getFont_shouldReturnTheFontSetInTheGraphicsState() {
        // Arrange: Create a GraphicsState with a specific font.
        GraphicsState graphicsState = new GraphicsState();
        DocumentFont expectedFont = new CMapAwareDocumentFont(new PdfGState());
        graphicsState.font = expectedFont;

        // Arrange: Create a TextRenderInfo instance using this GraphicsState.
        TextRenderInfo textRenderInfo = new TextRenderInfo(
                new PdfString("test text"),
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // Act: Retrieve the font from the TextRenderInfo instance.
        DocumentFont actualFont = textRenderInfo.getFont();

        // Assert: The retrieved font should be the exact same instance as the one
        // originally set in the GraphicsState.
        assertSame("The font returned by getFont() should be the same instance provided during construction.",
                expectedFont, actualFont);
    }
}