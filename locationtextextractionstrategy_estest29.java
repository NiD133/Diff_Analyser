package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * This test class evaluates the behavior of the {@link TextChunkLocationDefaultImp} inner class.
 * The original test was auto-generated and has been refactored for clarity and maintainability.
 */
public class LocationTextExtractionStrategy_ESTestTest29 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that the sameLine() method returns false when comparing two text chunks
     * that have different orientations and perpendicular distances, confirming they do not
     * lie on the same line.
     */
    @Test(timeout = 4000)
    public void sameLine_shouldReturnFalse_whenChunksHaveDifferentOrientationAndPosition() {
        // Arrange
        // Define a text chunk that is effectively a single point. This results in a default
        // horizontal orientation and a specific perpendicular distance from the origin.
        Vector pointLocation = new Vector(0.0F, -1162.3555F, 1.0F);
        TextChunkLocationDefaultImp chunkOnFirstLine = new TextChunkLocationDefaultImp(pointLocation, pointLocation, -2442.038F);

        // Define a second text chunk that starts at the same point but ends elsewhere.
        // This gives it a different orientation and a different perpendicular distance.
        Vector startLocationForSecondChunk = new Vector(0.0F, -1162.3555F, 1.0F);
        Vector endLocationForSecondChunk = new Vector(905.5172F, 905.5172F, 905.5172F);
        TextChunkLocationDefaultImp chunkOnSecondLine = new TextChunkLocationDefaultImp(startLocationForSecondChunk, endLocationForSecondChunk, -1906.0F);

        // Act
        // Check if the second chunk is considered to be on the same line as the first.
        boolean areOnSameLine = chunkOnSecondLine.sameLine(chunkOnFirstLine);

        // Assert
        assertFalse("Chunks with different orientations and positions should not be on the same line.", areOnSameLine);
    }
}