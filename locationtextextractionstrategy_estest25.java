package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the comparison logic of {@link LocationTextExtractionStrategy.TextChunk}.
 */
public class LocationTextExtractionStrategy_ESTestTest25 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that when two TextChunks have the same orientation, the compareTo method
     * correctly sorts them based on their perpendicular distance. A chunk with a larger
     * perpendicular distance is considered "greater than" a chunk with a smaller one.
     */
    @Test
    public void compareTo_whenOrientationsAreEqual_sortsByPerpendicularDistance() {
        // --- Arrange ---
        // Create two text chunks with the same default orientation but different perpendicular distances.
        // When a chunk's start and end locations are identical, its orientation defaults to a
        // horizontal vector (1, 0, 0). In this case, the perpendicular distance from the origin
        // is primarily determined by the Y-coordinate.
        Vector locationHigh = new Vector(50, 200, 0); // Higher Y-coordinate
        Vector locationLow = new Vector(50, 100, 0);   // Lower Y-coordinate

        // `chunkHigh` is located "above" `chunkLow`, giving it a larger perpendicular distance.
        TextChunk chunkHigh = new TextChunk("High", locationHigh, locationHigh, 10);
        TextChunk chunkLow = new TextChunk("Low", locationLow, locationLow, 10);

        // --- Act ---
        // Compare the chunks in both directions to ensure the logic is consistent.
        int highVsLowResult = chunkHigh.compareTo(chunkLow);
        int lowVsHighResult = chunkLow.compareTo(chunkHigh);

        // --- Assert ---
        // The compareTo method sorts by orientation first, then perpendicular distance.
        // Since orientations are identical, the chunk with the larger perpendicular distance (chunkHigh)
        // is considered "greater".

        // A return value of 1 means the first object is greater than the second.
        assertEquals("A chunk with a larger perpendicular distance should be 'greater'", 1, highVsLowResult);

        // A return value of -1 means the first object is less than the second.
        assertEquals("A chunk with a smaller perpendicular distance should be 'lesser'", -1, lowVsHighResult);
    }
}