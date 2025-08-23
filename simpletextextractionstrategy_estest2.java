package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SimpleTextExtractionStrategy} class.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that text appended via appendTextChunk is correctly returned by getResultantText.
     * This tests the basic text accumulation and retrieval functionality.
     */
    @Test
    public void getResultantText_afterAppendingTextChunk_shouldReturnAppendedText() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String expectedText = "A sample chunk of text.";

        // Act
        // The appendTextChunk method accepts any CharSequence, so we can pass the String directly.
        strategy.appendTextChunk(expectedText);
        String actualText = strategy.getResultantText();

        // Assert
        assertEquals("The retrieved text should match the appended text.", expectedText, actualText);
    }
}