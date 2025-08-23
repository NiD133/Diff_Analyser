package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Unit tests for the {@link LocationTextExtractionStrategy} class.
 */
public class LocationTextExtractionStrategyTest {

    /**
     * Verifies that calling {@link LocationTextExtractionStrategy#isChunkAtWordBoundary(LocationTextExtractionStrategy.TextChunk, LocationTextExtractionStrategy.TextChunk)}
     * with null arguments throws a {@link NullPointerException}.
     *
     * The method is expected to attempt to access methods on the first null parameter,
     * which will trigger the exception.
     */
    @Test(expected = NullPointerException.class)
    public void isChunkAtWordBoundary_whenCalledWithNullChunks_throwsNullPointerException() {
        // Arrange
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        // The assertion is handled by the 'expected' attribute of the @Test annotation.
        strategy.isChunkAtWordBoundary(null, null);
    }
}