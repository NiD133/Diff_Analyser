package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the {@link LocationTextExtractionStrategy} class.
 */
public class LocationTextExtractionStrategyTest {

    /**
     * Tests that the isChunkAtWordBoundary method returns true when a text chunk
     * is compared to itself. This is an edge case test.
     */
    @Test
    public void isChunkAtWordBoundary_whenChunkIsComparedToItself_returnsTrue() {
        // Arrange: Create a zero-length text chunk located at the origin.
        Vector origin = new Vector(0.0F, 0.0F, 0.0F);
        
        // The character space width is an arbitrary value from the original generated test.
        float charSpaceWidth = -2505.027F;
        
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp chunkLocation =
                new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(origin, origin, charSpaceWidth);

        // Sanity-check the properties of our zero-length chunk's location.
        assertEquals("Perpendicular distance for a chunk at the origin should be 0", 0, chunkLocation.distPerpendicular());
        assertEquals("Orientation magnitude for a zero-length chunk should be 0", 0, chunkLocation.orientationMagnitude());
        assertEquals("Parallel start distance for a chunk at the origin should be 0.0", 0.0F, chunkLocation.distParallelStart(), 0.01F);
        assertEquals("Parallel end distance for a chunk at the origin should be 0.0", 0.0F, chunkLocation.distParallelEnd(), 0.01F);

        LocationTextExtractionStrategy.TextChunk textChunk = new LocationTextExtractionStrategy.TextChunk("", chunkLocation);
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Act: Check if the chunk is at a word boundary with itself.
        boolean isAtBoundary = strategy.isChunkAtWordBoundary(textChunk, textChunk);

        // Assert: A chunk compared to itself should be considered at a word boundary.
        assertTrue("A chunk should be considered at a word boundary relative to itself", isAtBoundary);
    }
}