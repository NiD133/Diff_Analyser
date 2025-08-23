package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link UtcInstant} class, focusing on conversions.
 */
public class UtcInstantTest {

    /**
     * Tests that converting a UtcInstant to a java.time.Instant and back
     * results in an equivalent UtcInstant. This verifies that the conversion
     * process is lossless under normal conditions.
     */
    @Test
    public void roundTripConversion_fromUtcInstant_toInstant_andBack_isLossless() {
        // Arrange: Create an initial UtcInstant from a Modified Julian Day and nano-of-day.
        long modifiedJulianDay = -127L;
        long nanoOfDay = 41317L;
        UtcInstant originalUtcInstant = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, nanoOfDay);

        // Act: Convert the UtcInstant to a standard java.time.Instant and then convert it back.
        Instant javaInstant = originalUtcInstant.toInstant();
        UtcInstant roundTrippedUtcInstant = UtcInstant.of(javaInstant);

        // Assert: The round-tripped instant should be equal to the original.
        assertEquals(originalUtcInstant, roundTrippedUtcInstant);
    }
}