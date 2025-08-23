package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertSame;

/**
 * Test suite for {@link TextRenderInfo}.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getFont() returns the same font instance
     * that was provided in the GraphicsState during construction.
     */
    @Test
    public void getFont_shouldReturnTheFontObjectFromGraphicsState() {
        // Arrange: Create a GraphicsState with a specific font and use it to initialize TextRenderInfo.
        GraphicsState graphicsState = new GraphicsState();
        DocumentFont expectedFont = new CMapAwareDocumentFont(null); // A concrete DocumentFont implementation.
        graphicsState.font = expectedFont;

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                new PdfString("test"),
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // Act: Retrieve the font from the TextRenderInfo object.
        DocumentFont actualFont = textRenderInfo.getFont();

        // Assert: The retrieved font should be the exact same instance as the one we set initially.
        assertSame("The returned font should be the same instance as the one in the GraphicsState.",
                expectedFont, actualFont);
    }
}