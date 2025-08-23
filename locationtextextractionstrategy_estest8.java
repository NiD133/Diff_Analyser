package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The test class name is kept as in the original file, but the test method is refactored.
public class LocationTextExtractionStrategy_ESTestTest8 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that the location properties (orientation, perpendicular distance, etc.)
     * are calculated correctly for a zero-length text chunk. A zero-length chunk
     * is a special case where the start and end locations are identical.
     */
    @Test
    public void calculatesPropertiesCorrectlyForZeroLengthChunk() {
        // Arrange: A zero-length chunk is defined by identical start and end vectors.
        // The implementation defaults to a horizontal orientation vector (1, 0, 0) in this case.
        Vector pointLocation = new Vector(0.0f, -2030.0f, 0.0f);
        float charSpaceWidth = 0.0f;

        // Act: Create the TextChunkLocation with the same start and end point.
        TextChunkLocationDefaultImp chunkLocation = new TextChunkLocationDefaultImp(pointLocation, pointLocation, charSpaceWidth);

        // Assert: Verify the calculated geometric properties.
        // The orientation for a default horizontal vector (1,0,0) is atan2(0,1)*1000 = 0.
        assertEquals("Orientation magnitude should be 0 for default horizontal orientation",
                0, chunkLocation.orientationMagnitude());

        // The perpendicular distance is calculated from the origin relative to the orientation vector.
        // For a point at y=-2030 and a horizontal line, this results in 2030.
        assertEquals("Perpendicular distance should reflect the y-coordinate",
                2030, chunkLocation.distPerpendicular());

        // The parallel distance is the dot product of the location with the orientation vector.
        // For (0, -2030, 0) . (1, 0, 0), the result is 0.
        assertEquals("Parallel start distance should be 0", 0.0f, chunkLocation.distParallelStart(), 0.01f);
        assertEquals("Parallel end distance should be 0", 0.0f, chunkLocation.distParallelEnd(), 0.01f);

        assertEquals("Character space width should be as provided in constructor",
                charSpaceWidth, chunkLocation.getCharSpaceWidth(), 0.01f);
    }
}