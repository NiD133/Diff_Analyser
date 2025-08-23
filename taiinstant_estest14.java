package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    /**
     * Tests that converting a {@link UtcInstant} to a {@link TaiInstant} and back
     * results in the original {@link UtcInstant}. This verifies that the
     * conversion round-trip is lossless.
     */
    @Test
    public void utcInstantConvertsToTaiInstantAndBackWithoutLoss() {
        // Arrange: Create a specific UtcInstant for a known point in time.
        // Using a fixed value makes the test deterministic and easy to debug.
        // The original test used Modified Julian Day 0 (which is 1858-11-17).
        long modifiedJulianDay = 0L;
        long nanoOfDay = 12_345_678_901_234L; // An arbitrary, non-zero time of day.
        UtcInstant originalUtcInstant = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, nanoOfDay);

        // Act: Perform the round-trip conversion.
        // 1. Convert from UtcInstant to TaiInstant.
        TaiInstant taiInstant = TaiInstant.of(originalUtcInstant);
        // 2. Convert back from TaiInstant to UtcInstant.
        UtcInstant roundTripUtcInstant = taiInstant.toUtcInstant();

        // Assert: The result of the round-trip conversion should equal the original instant.
        assertEquals("Round-trip conversion should yield the original UtcInstant",
                originalUtcInstant, roundTripUtcInstant);
    }
}