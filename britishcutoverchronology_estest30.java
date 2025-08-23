package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the prolepticYear method in {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that the proleptic year is calculated correctly for the AD era.
     * <p>
     * According to the rules of the chronology, for the 'Anno Domini' (AD) era,
     * the proleptic year should be the same as the year-of-era.
     */
    @Test
    public void prolepticYear_forAdEra_shouldReturnSameAsYearOfEra() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        JulianEra adEra = JulianEra.AD;
        int yearOfEra = 0;

        // Act
        int prolepticYear = chronology.prolepticYear(adEra, yearOfEra);

        // Assert
        assertEquals("For the AD era, proleptic year should equal year-of-era.", yearOfEra, prolepticYear);
    }
}