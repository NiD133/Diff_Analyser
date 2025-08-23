package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the inner class {@link LocationTextExtractionStrategy.TextChunk}.
 */
public class LocationTextExtractionStrategyTextChunkTest {

    /**
     * Tests that the distance between two TextChunks is zero when they occupy the exact same
     * start and end positions. This represents a complete overlap scenario.
     */
    @Test
    public void distanceFromEndOf_whenChunksAreCoLocated_returnsZero() {
        // Arrange: Create two TextChunks that are co-located (same start and end vectors).
        // These are zero-width chunks, which is a valid edge case.
        Vector sharedLocation = new Vector(0.0f, -2030.0f, 0.0f);

        // The text content and character space width are irrelevant for this distance calculation,
        // as the distance depends only on the start and end locations.
        LocationTextExtractionStrategy.TextChunk currentChunk = new LocationTextExtractionStrategy.TextChunk(
                "", sharedLocation, sharedLocation, 0.0f);
        LocationTextExtractionStrategy.TextChunk previousChunk = new LocationTextExtractionStrategy.TextChunk(
                "Lm", sharedLocation, sharedLocation, 2.0f);

        // Act: Calculate the distance from the end of the previous chunk to the start of the current one.
        float distance = currentChunk.distanceFromEndOf(previousChunk);

        // Assert: Since both chunks start and end at the same point, the distance should be zero.
        assertEquals(0.0f, distance, 0.01f);
    }
}