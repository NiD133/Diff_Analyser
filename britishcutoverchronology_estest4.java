package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link BritishCutoverChronology#prolepticYear(Era, int)} method.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that for the 'Anno Domini' (AD) era, the proleptic year is identical
     * to the year-of-era.
     *
     * <p>The {@code prolepticYear} method should return the {@code yearOfEra} value
     * directly when the era is {@code JulianEra.AD}. This test confirms that behavior.
     */
    @Test
    public void prolepticYear_forAdEra_returnsSameAsYearOfEra() {
        // Arrange
        // Use the singleton instance as recommended by the class documentation.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        int yearOfEra = 13;
        int expectedProlepticYear = 13;

        // Act
        int actualProlepticYear = chronology.prolepticYear(JulianEra.AD, yearOfEra);

        // Assert
        assertEquals(
            "For the AD era, the proleptic year should be the same as the year-of-era.",
            expectedProlepticYear,
            actualProlepticYear
        );
    }
}