package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TaiInstant}.
 * This focuses on the conversion between {@link TaiInstant} and {@link UtcInstant}.
 */
public class TaiInstant_ESTestTest18 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that converting a TaiInstant to a UtcInstant and back again
     * results in the original TaiInstant. This verifies a lossless round-trip conversion.
     */
    @Test
    public void of_fromUtcInstant_roundTripConversionIsLossless() {
        // Arrange: Create an initial TaiInstant at the TAI epoch.
        TaiInstant originalTaiInstant = TaiInstant.ofTaiSeconds(0L, 0L);

        // Act: Convert to UtcInstant and then back to TaiInstant.
        UtcInstant intermediateUtcInstant = originalTaiInstant.toUtcInstant();
        TaiInstant roundTripTaiInstant = TaiInstant.of(intermediateUtcInstant);

        // Assert: The final TaiInstant should be equal to the original.
        assertEquals(originalTaiInstant, roundTripTaiInstant);
    }
}