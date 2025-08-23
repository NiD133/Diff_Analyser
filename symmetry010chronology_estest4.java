package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that the prolepticYear() method correctly converts a year in the Common Era (CE).
     * For the CE era, the proleptic year should be identical to the year-of-era.
     */
    @Test
    public void prolepticYear_withCommonEra_returnsSameYear() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int yearOfEra = 3994;

        // Act
        int prolepticYear = chronology.prolepticYear(IsoEra.CE, yearOfEra);

        // Assert
        assertEquals("For the Common Era (CE), the proleptic year should equal the year-of-era.",
                yearOfEra, prolepticYear);
    }
}