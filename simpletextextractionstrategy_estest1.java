package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SimpleTextExtractionStrategy} class.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that appendTextChunk correctly inserts text between rendering operations
     * without adding extra spaces or newlines.
     *
     * This scenario simulates manually adding text after an initial piece of text has been
     * rendered. A subsequent render call at the exact same position should not introduce
     * any extra separators (like spaces or newlines), and the manually appended text
     * should be preserved correctly in the final output.
     */
    @Test
    public void appendTextChunk_shouldPreserveTextBetweenIdenticalRenders() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String manuallyAppendedText = "some custom text";

        // Create a mock TextRenderInfo. The actual text content is empty, but it provides
        // the necessary geometry and font information to the strategy. A font is required
        // for the strategy to calculate text boundaries.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfAction());
        TextRenderInfo textRenderInfo = new TextRenderInfo(
                new PdfString(""), // The rendered text is empty
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // Act
        // 1. The first render call initializes the strategy's internal state (e.g., last text position).
        strategy.renderText(textRenderInfo);

        // 2. Manually append a text chunk. This is the core action under test.
        strategy.appendTextChunk(manuallyAppendedText);

        // 3. The second render call uses the same text info. The strategy should detect
        //    that this text is at the same position as the previous one and therefore
        //    not add a space or newline before rendering its (empty) content.
        strategy.renderText(textRenderInfo);

        // Assert
        String expectedText = manuallyAppendedText;
        String actualText = strategy.getResultantText();
        assertEquals("The manually appended text should be the only content.", expectedText, actualText);
    }
}