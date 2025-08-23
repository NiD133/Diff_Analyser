package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 * This focuses on the conversion between {@link UtcInstant} and {@link TaiInstant}.
 */
public class UtcInstant_ESTestTest13 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that converting a TaiInstant to a UtcInstant and back again
     * results in the original TaiInstant value. This verifies that the
     * round-trip conversion is lossless.
     */
    @Test
    public void conversionToAndFromTaiInstantIsLossless() {
        // Arrange: Create an initial TAI instant.
        // The original test used 0 TAI seconds and 1000 nanoseconds.
        TaiInstant originalTaiInstant = TaiInstant.ofTaiSeconds(0L, 1000L);

        // Act: Convert the TAI instant to a UTC instant, and then convert it back.
        UtcInstant intermediateUtcInstant = UtcInstant.of(originalTaiInstant);
        TaiInstant roundTripTaiInstant = intermediateUtcInstant.toTaiInstant();

        // Assert: The final TAI instant should be identical to the original.
        assertEquals(originalTaiInstant, roundTripTaiInstant);
    }
}