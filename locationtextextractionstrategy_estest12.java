package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LocationTextExtractionStrategy.TextChunkLocationDefaultImp} class.
 */
public class LocationTextExtractionStrategy_TextChunkLocationDefaultImpTest {

    private static final float FLOAT_PRECISION = 0.01f;

    /**
     * Tests that the distance from a chunk's end to its own start is zero.
     * This is verified using an edge case: a zero-length chunk located at the origin.
     */
    @Test
    public void distanceFromEndOf_shouldReturnZero_whenComparingChunkToItself() {
        // Arrange: Create a zero-length text chunk location at the origin.
        // A zero-length chunk has identical start and end vectors.
        Vector origin = new Vector(0.0f, 0.0f, 0.0f);
        float charSpaceWidth = 0.0f;
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp chunkLocation =
                new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(origin, origin, charSpaceWidth);

        // Act: Calculate the distance from the end of the chunk to its own start.
        float distance = chunkLocation.distanceFromEndOf(chunkLocation);

        // Assert: The distance should be zero.
        assertEquals("The distance from a chunk to itself should be 0.0", 0.0f, distance, FLOAT_PRECISION);

        // It's also good practice to verify the initial state of our test object.
        assertEquals("Perpendicular distance for a chunk at the origin should be 0", 0, chunkLocation.distPerpendicular());
        assertEquals("Character space width should be as constructed", 0.0f, chunkLocation.getCharSpaceWidth(), FLOAT_PRECISION);
        assertEquals("Parallel end distance for a zero-length chunk should be 0.0", 0.0f, chunkLocation.distParallelEnd(), FLOAT_PRECISION);
        assertEquals("Orientation magnitude for a zero-length chunk should be 0", 0, chunkLocation.orientationMagnitude());
    }
}