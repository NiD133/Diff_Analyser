package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test focuses on the inner class {@link LocationTextExtractionStrategy.TextChunkLocationDefaultImp}.
 */
public class LocationTextExtractionStrategy_ESTestTest11 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests the constructor of TextChunkLocationDefaultImp for a zero-length chunk,
     * where the start and end locations are identical.
     *
     * It verifies that:
     * 1. The character space width is stored and retrieved correctly.
     * 2. The orientation defaults to a standard horizontal vector.
     * 3. Distances (parallel and perpendicular) are calculated correctly based on this default orientation.
     */
    @Test(timeout = 4000)
    public void constructorForZeroLengthChunkCalculatesPropertiesCorrectly() {
        // ARRANGE: Define inputs for a zero-length text chunk.
        // A single point in space represents a chunk where start and end locations are the same.
        Vector startAndEndLocation = new Vector(1.0E-4F, 480.96F, 2.0F);
        float expectedCharSpaceWidth = 16.0301F;

        // ACT: Create the location object.
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp location =
                new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(
                        startAndEndLocation,
                        startAndEndLocation,
                        expectedCharSpaceWidth
                );

        // ASSERT: Verify the properties calculated by the constructor.

        // 1. Test the simple getter for character space width.
        assertEquals("Character space width should match the constructor argument",
                expectedCharSpaceWidth, location.getCharSpaceWidth(), 0.01F);

        // 2. For a zero-length chunk, the orientation vector defaults to (1, 0, 0).
        // The magnitude is calculated as (int)(atan2(0, 1) * 1000), which is 0.
        assertEquals("Orientation magnitude should be 0 for default horizontal orientation",
                0, location.orientationMagnitude());

        // 3. The perpendicular distance is calculated from the origin relative to the orientation vector.
        // For the given point, this results in the integer part of -480.96.
        assertEquals("Perpendicular distance should be calculated correctly",
                -480, location.distPerpendicular());

        // 4. The parallel distance is the dot product of the location with the orientation vector (1, 0, 0),
        // which is simply the x-coordinate of the location.
        assertEquals("Parallel start distance should equal the x-coordinate",
                1.0E-4F, location.distParallelStart(), 0.01F);
        assertEquals("Parallel end distance should equal the x-coordinate",
                1.0E-4F, location.distParallelEnd(), 0.01F);
    }
}