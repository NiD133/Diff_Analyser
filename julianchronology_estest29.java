package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the proleptic year calculation in {@link JulianChronology}.
 */
public class JulianChronologyTest {

    /**
     * Tests that the proleptic year for a year in the BC (Before Christ) era
     * is calculated correctly.
     *
     * <p>According to the Julian calendar system's rules, the proleptic year
     * for the BC era is calculated as {@code 1 - yearOfEra}.
     */
    @Test
    public void prolepticYear_forBCEra_calculatesCorrectly() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        Era bcEra = JulianEra.BC;
        int yearOfEra = 981;
        // For the BC era, the proleptic year is 1 - yearOfEra (1 - 981 = -980)
        int expectedProlepticYear = -980;

        // Act
        int actualProlepticYear = julianChronology.prolepticYear(bcEra, yearOfEra);

        // Assert
        assertEquals(expectedProlepticYear, actualProlepticYear);
    }
}