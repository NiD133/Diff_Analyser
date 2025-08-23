package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy.TextChunkLocationDefaultImp;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LocationTextExtractionStrategy.TextChunkLocationDefaultImp} class.
 */
public class TextChunkLocationDefaultImpTest {

    /**
     * Tests that the constructor correctly calculates location properties for a zero-length chunk,
     * where the start and end locations are the same.
     *
     * In this scenario, the implementation should default to a standard horizontal orientation
     * vector (1, 0, 0) and calculate all derived properties based on that assumption.
     */
    @Test
    public void constructorShouldCalculateCorrectPropertiesForZeroLengthChunk() {
        // ARRANGE: Define the input data and expected outcomes.
        final float x = 1518.243f;
        final float y = 1518.243f;
        final float z = -2451.2717f;
        final float charSpaceWidth = -1477.5446f; // An arbitrary value for testing.

        // For a zero-length chunk, the start and end locations are the same point.
        Vector locationPoint = new Vector(x, y, z);

        // Expected values are derived from the constructor's logic for a zero-length chunk:
        // - Orientation vector defaults to (1, 0, 0).
        // - Parallel distance is the dot product with (1, 0, 0), which equals the x-coordinate.
        final float expectedDistParallel = x;
        // - Perpendicular distance is derived from the cross product, resulting in the negative y-coordinate.
        final int expectedDistPerpendicular = (int) -y; // Becomes -1518 after casting to int.
        // - Orientation magnitude for a horizontal vector (atan2(0,1)) is 0.
        final int expectedOrientationMagnitude = 0;

        // ACT: Create the TextChunkLocation instance.
        TextChunkLocationDefaultImp chunkLocation = new TextChunkLocationDefaultImp(locationPoint, locationPoint, charSpaceWidth);

        // ASSERT: Verify that all calculated properties match the expected values.
        assertEquals("Parallel start distance should equal the x-coordinate.", expectedDistParallel, chunkLocation.distParallelStart(), 0.01f);
        assertEquals("Parallel end distance should equal the x-coordinate.", expectedDistParallel, chunkLocation.distParallelEnd(), 0.01f);
        assertEquals("Perpendicular distance should be based on the y-coordinate.", expectedDistPerpendicular, chunkLocation.distPerpendicular());
        assertEquals("Orientation magnitude should be zero for default horizontal orientation.", expectedOrientationMagnitude, chunkLocation.orientationMagnitude());
        assertEquals("Character space width should be stored correctly.", charSpaceWidth, chunkLocation.getCharSpaceWidth(), 0.01f);
        assertEquals("Start location should be the original vector.", locationPoint, chunkLocation.getStartLocation());
    }
}