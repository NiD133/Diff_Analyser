package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SimpleTextExtractionStrategy}.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Tests that renderText() throws a NullPointerException when its internal buffer
     * has been populated via appendTextChunk() and it then processes a TextRenderInfo
     * object configured with a font that lacks an underlying BaseFont.
     *
     * This test covers a specific edge case where the strategy is in an intermediate state
     * (has text but no previous render position) and encounters invalid font data, leading
     * to an unexpected failure during rendering calculations.
     */
    @Test
    public void renderText_whenStateIsPrimedByAppendTextChunkAndFontIsInvalid_throwsNullPointerException() {
        // Arrange
        // 1. Create a strategy and add text to it directly, bypassing the normal renderText flow.
        // This primes the strategy's buffer but leaves its positional tracking (lastStart/lastEnd) null.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.appendTextChunk("some pre-existing text");

        // 2. Set up a TextRenderInfo with a font object that is not a real font dictionary.
        // A CMapAwareDocumentFont without a valid BaseFont will cause issues during rendering calculations.
        // A PdfAction is used here as a stand-in for a dictionary that lacks the required font information.
        PdfDictionary mockFontDictionary = PdfAction.createLaunch("", "", "PDF", "");
        CMapAwareDocumentFont fontWithoutBaseFont = new CMapAwareDocumentFont(mockFontDictionary);

        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = fontWithoutBaseFont;

        TextRenderInfo renderInfo = new TextRenderInfo(
                new PdfDate(), // The actual text content for this render operation.
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // Act & Assert
        try {
            strategy.renderText(renderInfo);
            fail("Expected a NullPointerException to be thrown due to the invalid state.");
        } catch (NullPointerException e) {
            // The exception is expected. This confirms the method fails as anticipated
            // under these specific, unusual conditions.
            assertNotNull("The caught exception should not be null.", e);
        }
    }
}