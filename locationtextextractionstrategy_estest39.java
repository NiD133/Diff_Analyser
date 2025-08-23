package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Test suite for the {@link LocationTextExtractionStrategy} class.
 */
public class LocationTextExtractionStrategyTest {

    /**
     * Tests that calling the endTextBlock() method completes without throwing an exception.
     * <p>
     * The method is part of the RenderListener interface and is implemented as a no-op
     * (no operation) in LocationTextExtractionStrategy. This test ensures that this empty
     * implementation does not cause any runtime errors.
     */
    @Test
    public void endTextBlock_shouldExecuteWithoutErrors() {
        // Arrange: Create an instance of the strategy using its default constructor.
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Act & Assert: Call the method and expect no exception.
        // A successful execution of this line means the test passes.
        strategy.endTextBlock();
    }
}