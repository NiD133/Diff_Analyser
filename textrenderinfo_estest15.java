package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link TextRenderInfo} class.
 */
public class TextRenderInfoTest {

    /**
     * Verifies that getUnscaledWidth() returns 0.0 for a TextRenderInfo
     * object that was initialized with an empty string.
     */
    @Test
    public void getUnscaledWidth_forEmptyString_shouldReturnZero() {
        // ARRANGE: Create a TextRenderInfo instance for an empty text string.
        // A font object is required by the constructor, so we provide a minimal one.
        PdfString emptyText = new PdfString("");
        
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());

        // The specific transformation matrix does not affect the unscaled width calculation.
        Matrix identityMatrix = new Matrix();

        TextRenderInfo textRenderInfo = new TextRenderInfo(
                emptyText,
                graphicsState,
                identityMatrix,
                Collections.emptyList()
        );

        // ACT: Calculate the unscaled width.
        float unscaledWidth = textRenderInfo.getUnscaledWidth();

        // ASSERT: The width of an empty string should be zero.
        assertEquals("The unscaled width of an empty string should be 0.0F.", 0.0F, unscaledWidth, 0.001F);
    }
}