package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocation;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocationTextExtractionStrategy_ESTestTest3 {

    /**
     * Tests the behavior of {@link TextChunkLocationDefaultImp#isAtWordBoundary(TextChunkLocation)}
     * for a specific edge case where two text chunks have different underlying orientation vectors
     * but are still considered to be on the same line.
     *
     * This scenario occurs when one chunk is a point (zero length), causing its orientation
     * vector to default to horizontal (1,0,0), while the other has a vertical orientation (0,0,1).
     * Due to a collision in the `orientationMagnitude` calculation (both result in 0), the `sameLine`
     * method returns true.
     *
     * The test verifies that despite a large calculated overlap between these chunks,
     * they are not considered to be at a word boundary.
     */
    @Test
    public void isAtWordBoundary_ReturnsFalse_ForOverlappingChunksWithMismatchedOrientations() {
        // --- ARRANGE ---

        // Define a "previous" chunk represented by a long vertical line segment.
        // Its orientation vector will be (0, 0, 1).
        Vector previousChunkStart = new Vector(0, 0, 1);
        Vector previousChunkEnd = new Vector(0, 0, 1164.3555f);
        float previousChunkCharSpaceWidth = 7.0f;
        TextChunkLocation previousChunkLocation = new TextChunkLocationDefaultImp(
                previousChunkStart,
                previousChunkEnd,
                previousChunkCharSpaceWidth
        );

        // Define a "current" chunk as a zero-length point located at the start of the previous chunk.
        // Because its length is zero, its orientation vector defaults to (1, 0, 0).
        Vector currentChunkStartAndEnd = new Vector(0, 0, 1);
        float currentCharSpaceWidth = 4.0f;
        TextChunkLocation currentChunkLocation = new TextChunkLocationDefaultImp(
                currentChunkStartAndEnd,
                currentChunkStartAndEnd,
                currentCharSpaceWidth
        );

        // Sanity check: Verify that the two chunks are considered to be on the same line,
        // which is the precondition for this edge case.
        assertTrue("Chunks with mismatched orientations should be considered on the same line for this test case",
                currentChunkLocation.sameLine(previousChunkLocation));
        assertEquals(0, currentChunkLocation.orientationMagnitude());
        assertEquals(0, previousChunkLocation.orientationMagnitude());


        // --- ACT ---

        // Check if the current chunk is at a word boundary relative to the previous one.
        // The distance calculation uses their different orientation vectors, resulting in a large
        // negative value, indicating significant overlap.
        boolean isAtBoundary = currentChunkLocation.isAtWordBoundary(previousChunkLocation);


        // --- ASSERT ---

        // The test asserts that this specific type of large overlap is NOT considered a word boundary.
        assertFalse("A large overlap with mismatched orientations should not be a word boundary", isAtBoundary);
    }
}