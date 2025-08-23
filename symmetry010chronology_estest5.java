package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the prolepticYear() method of {@link Symmetry010Chronology}.
 */
// The original class name and inheritance are kept to match the project's structure.
public class Symmetry010Chronology_ESTestTest5 extends Symmetry010Chronology_ESTest_scaffolding {

    /**
     * Tests that prolepticYear() returns the same year value for the Common Era (CE).
     * The proleptic year should be identical to the year-of-era for any year in the CE era.
     */
    @Test
    public void prolepticYear_withCommonEra_returnsSameYear() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Era commonEra = IsoEra.CE;
        int yearOfEra = -537;
        int expectedProlepticYear = -537;

        // Act
        int actualProlepticYear = chronology.prolepticYear(commonEra, yearOfEra);

        // Assert
        assertEquals(
            "For the CE era, the proleptic year should be the same as the year-of-era.",
            expectedProlepticYear,
            actualProlepticYear
        );
    }
}