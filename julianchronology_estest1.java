package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a date from a negative epoch day correctly results
     * in a date in the BC (Before Christ) era.
     */
    @Test
    public void dateEpochDay_withNegativeEpochDay_returnsDateInBCEra() {
        // Arrange: Define a known date in the BC era and find its corresponding epoch day.
        // The proleptic year 0 in the Julian calendar corresponds to the year 1 BC.
        JulianDate dateInBCEra = JulianDate.of(0, 1, 1); // Represents 1 BC, Jan 1st
        long epochDay = dateInBCEra.toEpochDay();

        // Act: Create a new JulianDate from this epoch day using the chronology.
        JulianDate resultDate = JulianChronology.INSTANCE.dateEpochDay(epochDay);

        // Assert: The resulting date should be in the BC era and equal to the original date.
        assertEquals("The era should be BC", JulianEra.BC, resultDate.getEra());
        assertEquals("The reconstructed date should match the original", dateInBCEra, resultDate);
    }
}