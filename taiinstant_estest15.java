package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the TaiInstant class, focusing on conversions.
 */
public class TaiInstantTest {

    /**
     * Tests that converting a UtcInstant to a TaiInstant and back again
     * results in the original UtcInstant, ensuring the conversion is lossless.
     */
    @Test
    public void conversionToTaiAndBackToUtcIsLossless() {
        // Arrange: Create a UtcInstant. The specific date is arbitrary; the key is to
        // verify that the entire value, including the nano-of-day, is preserved.
        long modifiedJulianDay = -2L;
        long nanoOfDay = 1L;
        UtcInstant originalUtcInstant = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, nanoOfDay);

        // Act: Convert from UtcInstant to TaiInstant, and then back to UtcInstant.
        TaiInstant taiInstant = TaiInstant.of(originalUtcInstant);
        UtcInstant resultUtcInstant = taiInstant.toUtcInstant();

        // Assert: The final UtcInstant should be identical to the original.
        // This is a stronger guarantee than the original test's assertions.
        assertEquals(originalUtcInstant, resultUtcInstant);

        // We can also verify the intermediate state for clarity.
        // The nano-of-day from UtcInstant should correspond to the nano-of-second
        // in TaiInstant when the time is at the very start of the day.
        assertEquals(nanoOfDay, taiInstant.getNano());
    }
}