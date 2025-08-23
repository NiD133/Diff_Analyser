package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.Era;
import org.junit.Test;

/**
 * Tests for the prolepticYear method in {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void prolepticYear_withBCEra_isCalculatedAsOneMinusYearOfEra() {
        // The proleptic year for the BC era is defined as 1 minus the year-of-era.
        // This test verifies that 1778 BC corresponds to the proleptic year -1777.

        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        Era bcEra = JulianEra.BC;
        int yearOfEra = 1778;
        int expectedProlepticYear = 1 - yearOfEra; // 1 - 1778 = -1777

        // Act
        int actualProlepticYear = chronology.prolepticYear(bcEra, yearOfEra);

        // Assert
        assertEquals("Proleptic year for BC era should be 1 - yearOfEra", expectedProlepticYear, actualProlepticYear);
    }
}