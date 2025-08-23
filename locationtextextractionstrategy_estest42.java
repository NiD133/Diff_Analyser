package com.itextpdf.text.pdf.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test class contains the improved version of the original test case.
 * The original scaffolding and runner-specific annotations have been removed for clarity,
 * focusing on a standard JUnit 4 and Mockito setup.
 */
public class LocationTextExtractionStrategyImprovedTest {

    /**
     * Verifies that getResultantText returns an empty string when the strategy
     * processes a TextRenderInfo object that contains no text.
     *
     * This scenario can occur in a real PDF if, for example, a font's decoding
     * process fails or if an empty text object is present.
     */
    @Test
    public void getResultantText_whenRenderInfoHasNoText_returnsEmptyString() {
        // Arrange
        // Create a mock TextRenderInfo that simulates a text rendering operation
        // that results in an empty string. This isolates the strategy from the
        // complexities of font decoding and other dependencies.
        TextRenderInfo emptyTextRenderInfo = mock(TextRenderInfo.class);
        when(emptyTextRenderInfo.getText()).thenReturn("");

        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Act
        // The strategy is asked to process the (empty) text rendering information.
        // It should correctly handle this by not adding any text chunks.
        strategy.renderText(emptyTextRenderInfo);
        String result = strategy.getResultantText();

        // Assert
        // Since the rendered text was empty, the strategy should produce an empty result.
        assertEquals("Expected an empty string when no text is rendered.", "", result);
    }
}