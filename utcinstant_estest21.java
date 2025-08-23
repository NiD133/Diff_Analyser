package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the conversion logic between {@link UtcInstant} and {@link TaiInstant}.
 */
public class UtcInstantConversionTest {

    /**
     * Tests that converting a UtcInstant to a TaiInstant and back
     * results in an equivalent UtcInstant.
     *
     * This test verifies the consistency of the conversion logic, ensuring that
     * the round-trip operation is lossless.
     */
    @Test
    public void conversionToTaiInstantAndBackIsLossless() {
        // Arrange: Create an arbitrary UtcInstant.
        // The specific values are not significant, only that they are valid.
        long modifiedJulianDay = -127L;
        long nanoOfDay = 41317L;
        UtcInstant originalUtcInstant = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, nanoOfDay);

        // Act: Convert the UtcInstant to a TaiInstant and then convert it back.
        TaiInstant intermediateTaiInstant = originalUtcInstant.toTaiInstant();
        UtcInstant roundTripUtcInstant = UtcInstant.of(intermediateTaiInstant);

        // Assert: The UtcInstant after the round-trip conversion should be equal to the original.
        assertEquals(originalUtcInstant, roundTripUtcInstant);
    }
}