package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocation;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LocationTextExtractionStrategy_ESTestTest5 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that the sameLine() method returns false for two text chunks
     * that have the same orientation but are at different perpendicular distances.
     * In PDF text extraction, this corresponds to text on two different, parallel lines.
     */
    @Test
    public void sameLine_shouldReturnFalse_whenChunksHaveDifferentPerpendicularDistances() {
        // Arrange
        // Create two text chunk locations. For simplicity, their start and end points are the same,
        // which gives them a default horizontal orientation.
        // The key difference is their Y-coordinate, which results in different perpendicular distances
        // from the orientation vector's line through the origin.

        // A chunk located at y=100.
        Vector position1 = new Vector(50, 100, 0);
        TextChunkLocation chunkOnLine1 = new TextChunkLocationDefaultImp(position1, position1, 0);

        // A second chunk located at y=200, parallel to the first.
        Vector position2 = new Vector(50, 200, 0);
        TextChunkLocation chunkOnLine2 = new TextChunkLocationDefaultImp(position2, position2, 0);

        // Sanity check the setup: both chunks should have the same horizontal orientation (magnitude 0)
        // but different perpendicular distances. For a horizontal orientation, the perpendicular
        // distance is derived from the negative Y-coordinate.
        assertEquals(0, chunkOnLine1.orientationMagnitude());
        assertEquals(0, chunkOnLine2.orientationMagnitude());
        assertEquals(-100, chunkOnLine1.distPerpendicular());
        assertEquals(-200, chunkOnLine2.distPerpendicular());

        // Act
        // Check if the two chunks are considered to be on the same line.
        boolean areOnSameLine = chunkOnLine1.sameLine(chunkOnLine2);

        // Assert
        // They should not be on the same line because their perpendicular distances differ.
        assertFalse("Chunks with different perpendicular distances should not be on the same line", areOnSameLine);
    }
}