package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SimpleTextExtractionStrategy} class.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that a newly created SimpleTextExtractionStrategy instance
     * returns an empty string before any text has been processed.
     */
    @Test
    public void getResultantText_onNewInstance_returnsEmptyString() {
        // Arrange: Create a new instance of the text extraction strategy.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Act: Get the resultant text without processing any input.
        String resultantText = strategy.getResultantText();

        // Assert: The returned text should be empty.
        assertEquals("The initial text from a new strategy should be empty.", "", resultantText);
    }
}