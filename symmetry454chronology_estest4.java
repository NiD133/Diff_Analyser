package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.IsoEra;
import org.junit.Test;

/**
 * Tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void prolepticYear_withCEEra_returnsSameAsYearOfEra() {
        // Arrange
        // The Symmetry454Chronology uses the same proleptic year as the year-of-era for the CE era.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int yearOfEra = -1390;
        int expectedProlepticYear = -1390;

        // Act
        int actualProlepticYear = chronology.prolepticYear(IsoEra.CE, yearOfEra);

        // Assert
        assertEquals(expectedProlepticYear, actualProlepticYear);
    }
}