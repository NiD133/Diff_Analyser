package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.time.Instant;

/**
 * This test class is a placeholder for the improved test method.
 * The original test was part of an auto-generated suite (TaiInstant_ESTestTest13).
 */
public class TaiInstant_ESTestTest13 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that converting a UtcInstant to a TaiInstant and then back to a UtcInstant
     * results in the original value. This verifies that the conversion is lossless for a
     * standard, well-known point in time.
     */
    @Test
    public void testUtcToTaiToUtcRoundTripIsLossless() {
        // Arrange: Create a UtcInstant from a well-known point in time, the Java epoch.
        // This is more readable than using an obscure value like Modified Julian Day 0.
        UtcInstant originalUtcInstant = UtcInstant.of(Instant.EPOCH);

        // Act: Perform the round-trip conversion from UtcInstant -> TaiInstant -> UtcInstant.
        TaiInstant taiInstant = TaiInstant.of(originalUtcInstant);
        UtcInstant roundTripUtcInstant = taiInstant.toUtcInstant();

        // Assert: The final UtcInstant should be identical to the original.
        // This single, strong assertion is more comprehensive than checking individual fields.
        assertEquals(originalUtcInstant, roundTripUtcInstant);
    }
}