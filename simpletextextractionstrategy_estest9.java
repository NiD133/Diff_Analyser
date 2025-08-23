package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

// Note: Other imports from the original test may be required depending on the full class structure.
// This example includes only those directly used in the refactored test.

public class SimpleTextExtractionStrategy_ESTestTest9 extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Creates a TextRenderInfo object configured with an invalid font.
     * <p>
     * A {@link PdfAction} is used as a font dictionary, which is not a valid
     * font structure. This causes the font's internal decode method to return
     * an empty string, effectively making any {@code renderText} call using
     * this object a no-op in terms of text extraction.
     *
     * @return A {@link TextRenderInfo} instance that will not produce any text.
     */
    private TextRenderInfo createRenderInfoWithInvalidFont() {
        // A PdfAction is a dictionary, but not a valid font dictionary.
        PdfDictionary nonFontDictionary = new PdfAction();
        CMapAwareDocumentFont invalidFont = new CMapAwareDocumentFont(nonFontDictionary);

        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = invalidFont;

        // The text content and other parameters are arbitrary since the invalid font
        // will prevent any text from being decoded and extracted.
        PdfString dummyText = new PdfString("some text");
        Matrix dummyMatrix = new Matrix();

        return new TextRenderInfo(dummyText, graphicsState, dummyMatrix, Collections.emptyList());
    }

    @Test
    public void appendTextChunk_shouldAddTextToResult_whileRenderTextWithInvalidFontAddsNothing() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        TextRenderInfo renderInfoWithInvalidFont = createRenderInfoWithInvalidFont();
        String textToAppend = "This text should be appended directly.";

        // Act
        // This call should not add any text because the font in renderInfo is invalid.
        strategy.renderText(renderInfoWithInvalidFont);

        // This call should add the specified text to the internal buffer.
        strategy.appendTextChunk(textToAppend);

        // This second call should also add no text, confirming the behavior is consistent.
        strategy.renderText(renderInfoWithInvalidFont);

        // Assert
        // The final result should contain only the text from the appendTextChunk call.
        String actualText = strategy.getResultantText();
        assertEquals(textToAppend, actualText);
    }
}