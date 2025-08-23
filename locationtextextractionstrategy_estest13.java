package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the {@link LocationTextExtractionStrategy.TextChunkLocationDefaultImp} class.
 */
public class LocationTextExtractionStrategy_ESTestTest13 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that the constructor correctly handles a zero-length text chunk (where start and end locations are identical).
     *
     * <p>A zero-length chunk has no intrinsic orientation. The implementation should default to a
     * standard horizontal orientation and calculate all geometric properties based on that assumption.
     * This test verifies those calculated properties.
     */
    @Test
    public void constructor_withZeroLengthChunk_calculatesPropertiesUsingDefaultHorizontalOrientation() {
        // Arrange
        // Define coordinates for a single point in space.
        final float x = 3501.2126F;
        final float y = 3501.2126F;
        final float z = 3501.2126F;
        final float charSpaceWidth = -408.0F;

        // A zero-length chunk is represented by identical start and end locations.
        Vector locationPoint = new Vector(x, y, z);

        // Define expected values based on the implementation's default behavior.
        // For a zero-length chunk, the orientation vector defaults to horizontal (1, 0, 0).
        final int expectedOrientationMagnitude = 0; // Corresponds to atan2(0, 1), i.e., 0 degrees.
        final int expectedDistPerpendicular = (int) -y; // The perpendicular distance is the negative Y-coordinate.
        final float expectedDistParallel = x; // The parallel distance is the X-coordinate.

        // Act
        // The constructor is the system under test.
        LocationTextExtractionStrategy.TextChunkLocationDefaultImp chunkLocation =
                new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(locationPoint, locationPoint, charSpaceWidth);

        // Assert
        // Verify that all calculated properties match the expected values for a default horizontal orientation.
        assertEquals("Orientation magnitude should be 0 for default horizontal orientation",
                expectedOrientationMagnitude, chunkLocation.orientationMagnitude());

        assertEquals("Perpendicular distance should be based on the Y-coordinate",
                expectedDistPerpendicular, chunkLocation.distPerpendicular());

        assertEquals("Parallel start distance should be based on the X-coordinate",
                expectedDistParallel, chunkLocation.distParallelStart(), 0.01F);

        assertEquals("Parallel end distance should equal parallel start for a zero-length chunk",
                expectedDistParallel, chunkLocation.distParallelEnd(), 0.01F);

        assertEquals("Character space width should be stored correctly",
                charSpaceWidth, chunkLocation.getCharSpaceWidth(), 0.01F);
    }
}