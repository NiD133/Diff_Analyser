package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunk;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocation;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the inner classes of {@link LocationTextExtractionStrategy},
 * focusing on location and text chunk logic.
 */
public class LocationTextExtractionStrategyTest {

    /**
     * Tests that a zero-length text chunk (where start and end locations are identical)
     * correctly calculates its location properties. The implementation should default to a
     * standard horizontal orientation (a vector of [1, 0, 0]) for these calculations.
     */
    @Test
    public void testLocationProperties_ForZeroLengthChunk_DefaultsToHorizontalOrientation() {
        // Arrange: Define a text chunk at a single point, making it a "zero-length" chunk.
        Vector startAndEndLocation = new Vector(-2327.809F, -1220.9F, 0.0F);
        float charSpaceWidth = 2.0f;

        // A zero-length chunk's orientation vector defaults to [1, 0, 0] (horizontal).
        // All distance calculations are based on this default orientation.
        TextChunkLocationDefaultImp locationImpl = new TextChunkLocationDefaultImp(
                startAndEndLocation,
                startAndEndLocation,
                charSpaceWidth
        );
        TextChunk textChunk = new TextChunk("any text", locationImpl);

        // Act: Retrieve the location object from the text chunk.
        TextChunkLocation retrievedLocation = textChunk.getLocation();

        // Assert: Verify that the location properties were calculated correctly based on the
        // default horizontal orientation.
        assertNotNull(retrievedLocation);

        // The orientation magnitude for a horizontal vector [1, 0, 0] is 0.
        // (atan2(0, 1) * 1000) = 0
        assertEquals("Orientation magnitude should be 0 for default horizontal vector",
                0, retrievedLocation.orientationMagnitude());

        // The perpendicular distance is calculated from the cross product of the location vector
        // and the orientation vector. For a horizontal orientation, this effectively becomes the
        // integer part of the y-coordinate. int(([-2327, -1220, -1] x [1, 0, 0]).z) = int(1220.9) = 1220.
        assertEquals("Perpendicular distance should be based on the y-coordinate",
                1220, retrievedLocation.distPerpendicular());

        // The parallel distance is the dot product of the location and orientation vectors.
        // For a horizontal orientation, this is simply the x-coordinate.
        assertEquals("Parallel start distance should match the x-coordinate",
                -2327.809F, retrievedLocation.distParallelStart(), 0.01F);
        assertEquals("Parallel end distance should also match the x-coordinate",
                -2327.809F, retrievedLocation.distParallelEnd(), 0.01F);

        assertEquals("Character space width should be preserved",
                charSpaceWidth, retrievedLocation.getCharSpaceWidth(), 0.01F);
    }
}