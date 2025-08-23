package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Unit tests for the {@link LocationTextExtractionStrategy} class.
 */
public class LocationTextExtractionStrategyTest {

    /**
     * Verifies that the renderText method correctly throws a NullPointerException
     * when it is called with a null TextRenderInfo object. This ensures the method
     * handles invalid input gracefully by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void renderText_whenGivenNull_throwsNullPointerException() {
        // Arrange: Create an instance of the strategy.
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Act & Assert: Calling renderText with null should trigger the expected exception.
        strategy.renderText(null);
    }
}