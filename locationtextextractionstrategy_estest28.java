package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocation;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocationTextExtractionStrategy_ESTestTest28 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that isAtWordBoundary() returns true for two chunks on the same line
     * where the "current" chunk starts significantly before the "previous" chunk ends.
     * This represents an overlapping or out-of-order scenario, which should be
     * treated as a word boundary.
     */
    @Test
    public void isAtWordBoundary_shouldReturnTrue_whenChunksOverlap() {
        // Arrange
        // Define two text chunk locations on the same horizontal line (y=2.0).
        Vector startVector = new Vector(2.0F, 2.0F, 175.0F);

        // The "previous" chunk is a long segment from x=2 to x=175.
        Vector previousChunkEndVector = new Vector(175.0F, 2.0F, 175.0F);
        TextChunkLocation previousChunk = new TextChunkLocationDefaultImp(startVector, previousChunkEndVector, 0.0F);

        // The "current" chunk is a zero-length point located at the start of the previous chunk (x=2).
        // A small negative character space width is used to test the boundary condition.
        TextChunkLocation currentChunk = new TextChunkLocationDefaultImp(startVector, startVector, -5.186149E-6F);

        // Act
        // Check if the current chunk is at a word boundary relative to the previous one.
        boolean isAtBoundary = currentChunk.isAtWordBoundary(previousChunk);

        // Assert
        // The distance from the end of the previous chunk (x=175) to the start of the
        // current chunk (x=2) is 2 - 175 = -173. This large negative distance (overlap)
        // should be considered a word boundary.
        assertTrue("Chunks with significant overlap should be at a word boundary", isAtBoundary);

        // Verify the internal state of the created chunk locations to ensure the setup is correct.
        assertEquals("Parallel start distance of previous chunk should be its x-coordinate", 2.0F, previousChunk.distParallelStart(), 0.01F);
        assertEquals("Parallel end distance of previous chunk should be its x-coordinate", 175.0F, previousChunk.distParallelEnd(), 0.01F);
        assertEquals("Orientation of a horizontal chunk should be 0", 0, currentChunk.orientationMagnitude());
        assertEquals("Perpendicular distance should be based on the y-coordinate", -2, currentChunk.distPerpendicular());
    }
}