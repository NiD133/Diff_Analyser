package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link LocationTextExtractionStrategy.TextChunkLocationDefaultImp} class.
 */
public class TextChunkLocationDefaultImpTest {

    /**
     * Verifies that the constructor of TextChunkLocationDefaultImp correctly calculates
     * location properties for a zero-length text chunk, where the start and end locations
     * are identical.
     * <p>
     * For a zero-length chunk, the implementation defaults to a horizontal orientation vector (1, 0, 0).
     * This test confirms that all derived properties (orientation, distances) are calculated correctly
     * based on this default.
     */
    @Test
    public void constructor_shouldCalculateCorrectProperties_forZeroLengthChunk() {
        // Arrange
        final float Y_COORDINATE = 226.68376F;
        final float CHAR_SPACE_WIDTH = -703.80695F;
        final Vector locationVector = new Vector(0.0F, Y_COORDINATE, 0.0F);

        // Act
        // Create a chunk location where the start and end points are the same.
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp chunkLocation =
                new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(locationVector, locationVector, CHAR_SPACE_WIDTH);

        // Assert
        // For a zero-length chunk, orientation defaults to a horizontal vector (1,0,0),
        // which corresponds to an orientation magnitude of 0.
        assertEquals("Orientation magnitude should be 0 for default horizontal orientation",
                0, chunkLocation.orientationMagnitude());

        // The perpendicular distance is calculated from the cross product of the location vector
        // (relative to the origin) and the orientation vector. The integer cast of the result is asserted.
        assertEquals("Perpendicular distance should be the integer part of the negated Y-coordinate",
                -226, chunkLocation.distPerpendicular());

        // The parallel distance is the dot product of the location and orientation vectors.
        // For a location on the Y-axis and a horizontal orientation, this is 0.
        assertEquals("Parallel start distance should be 0", 0.0F, chunkLocation.distParallelStart(), 0.01F);
        assertEquals("Parallel end distance should be 0", 0.0F, chunkLocation.distParallelEnd(), 0.01F);

        // The character space width should be stored as provided.
        assertEquals("Character space width should be preserved",
                CHAR_SPACE_WIDTH, chunkLocation.getCharSpaceWidth(), 0.01F);

        // Verify the getters for start/end locations return the original vector.
        assertSame("Start location should be the provided vector", locationVector, chunkLocation.getStartLocation());
        assertSame("End location should be the provided vector", locationVector, chunkLocation.getEndLocation());
    }
}