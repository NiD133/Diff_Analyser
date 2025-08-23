package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that converting a UtcInstant to a TaiInstant and back
     * results in an equivalent UtcInstant.
     */
    @Test
    public void testRoundTripConversionViaTaiInstant() {
        // Arrange: Create a UtcInstant at the start of the Modified Julian Day epoch.
        // MJD 0 corresponds to 1858-11-17T00:00:00Z.
        UtcInstant originalUtcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);

        // Act: Convert the UtcInstant to a TaiInstant and then convert it back.
        TaiInstant taiInstant = originalUtcInstant.toTaiInstant();
        UtcInstant roundTripUtcInstant = UtcInstant.of(taiInstant);

        // Assert: The resulting instant should be equal to the original.
        assertEquals(originalUtcInstant, roundTripUtcInstant);
    }
}