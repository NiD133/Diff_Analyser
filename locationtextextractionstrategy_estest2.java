package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocation;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTextExtractionStrategy_ESTestTest2_Improved extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that the compareTo method correctly prioritizes sorting by orientation.
     * <p>
     * The comparison logic for TextChunkLocation is:
     * 1. Sort by orientation magnitude.
     * 2. If orientations are the same, sort by perpendicular distance.
     * 3. If both are the same, sort by parallel distance.
     * <p>
     * This test verifies the primary sorting criterion: orientation.
     */
    @Test
    public void compareTo_whenOrientationsDiffer_shouldSortByOrientationMagnitude() {
        // ARRANGE
        // Define two text chunk locations with different orientations.

        // Create a chunk with the same start and end point. This defaults to a
        // horizontal orientation, which has an orientation magnitude of 0.
        Vector startAndEndLocation1 = new Vector(-555.05f, -555.05f, -555.05f);
        TextChunkLocation chunkLocation1 = new TextChunkLocationDefaultImp(startAndEndLocation1, startAndEndLocation1, 10f);

        // Create a second chunk with a distinct start and end point, resulting in a
        // different, non-zero orientation magnitude.
        Vector startLocation2 = startAndEndLocation1;
        Vector endLocation2 = new Vector(0f, -3318.408f, 1f);
        TextChunkLocation chunkLocation2 = new TextChunkLocationDefaultImp(startLocation2, endLocation2, 10f);

        // Sanity check the calculated orientation magnitudes, which are the basis for this test.
        assertEquals("Chunk 1 should have a default horizontal orientation", 0, chunkLocation1.orientationMagnitude());
        assertEquals("Chunk 2 should have a calculated orientation", -1372, chunkLocation2.orientationMagnitude());

        // ACT
        // Compare the two locations.
        int comparisonResult = chunkLocation1.compareTo(chunkLocation2);

        // ASSERT
        // The result should be 1, because chunkLocation1's orientation magnitude (0) is
        // greater than chunkLocation2's (-1372).
        assertEquals(1, comparisonResult);
    }
}