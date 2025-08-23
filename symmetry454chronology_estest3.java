package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void prolepticYear_forBceEra_isSameAsYearOfEra() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int yearOfEra = 1996;
        int expectedProlepticYear = 1996;

        // Act
        int actualProlepticYear = chronology.prolepticYear(IsoEra.BCE, yearOfEra);

        // Assert
        // The documentation for Symmetry454Chronology states that "The proleptic year is the
        // same as the year-of-era for the current era." This test verifies that behavior for
        // the BCE era, which differs from the standard ISO chronology (where proleptic year
        // for BCE is calculated as 1 - yearOfEra).
        assertEquals(expectedProlepticYear, actualProlepticYear);
    }
}