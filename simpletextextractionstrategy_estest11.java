package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SimpleTextExtractionStrategy} class.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that calling beginTextBlock() does not add any text to the output.
     * This method is expected to be a no-op concerning the resultant text.
     */
    @Test
    public void beginTextBlock_shouldNotAddTextToResult() {
        // Arrange: Create a new instance of the text extraction strategy.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Act: Signal the beginning of a new text block.
        strategy.beginTextBlock();

        // Assert: Verify that the resultant text is still empty.
        String resultantText = strategy.getResultantText();
        assertEquals("The resultant text should be empty after starting a text block.", "", resultantText);
    }
}