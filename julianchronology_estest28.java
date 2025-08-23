package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.chrono.Era;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the prolepticYear method in JulianChronology.
 */
public class JulianChronology_ESTestTest28 {

    /**
     * Tests that prolepticYear() correctly handles the AD era.
     * According to the implementation, for the AD era, the proleptic year
     * is the same as the year-of-era.
     */
    @Test
    public void prolepticYear_forAdEraAndYearOfEraZero_shouldReturnZero() {
        // Arrange
        JulianChronology chronology = JulianChronology.INSTANCE;
        Era adEra = JulianEra.AD;
        int yearOfEra = 0;
        int expectedProlepticYear = 0;

        // Act
        int actualProlepticYear = chronology.prolepticYear(adEra, yearOfEra);

        // Assert
        assertEquals(
            "For the AD era, a year-of-era of 0 should result in a proleptic year of 0.",
            expectedProlepticYear,
            actualProlepticYear);
    }
}