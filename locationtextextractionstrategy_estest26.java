package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LocationTextExtractionStrategy_ESTestTest26 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that TextChunk comparison correctly prioritizes orientation.
     *
     * The compareTo method should first sort chunks by their orientation magnitude.
     * This test creates two chunks with different orientations and verifies that
     * the one with the smaller orientation magnitude is ordered first.
     */
    @Test
    public void compareTo_whenOrientationsDiffer_shouldSortByOrientation() {
        // Arrange
        // The TextChunk#compareTo method sorts based on:
        // 1. Orientation
        // 2. Perpendicular distance
        // 3. Parallel distance
        // This test verifies the primary sorting key: orientation.

        Vector commonStartLocation = new Vector(50, 100, 0);

        // Create a chunk that is effectively a single point. Its orientation defaults
        // to horizontal (a vector of [1, 0, 0]), resulting in an orientation magnitude of 0.
        Vector horizontalEndLocation = commonStartLocation;
        TextChunk horizontalChunk = new TextChunk("horizontal", commonStartLocation, horizontalEndLocation, 0);

        // Create a second chunk with a downward orientation (a vector of [0, -1, 0]).
        // Its orientation magnitude will be atan2(-1, 0) * 1000 = -1570.
        Vector downwardEndLocation = new Vector(50, 50, 0);
        TextChunk downwardChunk = new TextChunk("downward", commonStartLocation, downwardEndLocation, 2);

        // Act
        // Compare the downward-oriented chunk to the horizontal one.
        // Since -1570 is less than 0, the result should be -1.
        int comparisonResult = downwardChunk.compareTo(horizontalChunk);

        // Assert
        assertEquals("A chunk with a smaller orientation magnitude should be ordered first.", -1, comparisonResult);
        
        // This assertion simply verifies the test data was set up correctly.
        assertEquals(2.0F, downwardChunk.getCharSpaceWidth(), 0.01F);
    }
}