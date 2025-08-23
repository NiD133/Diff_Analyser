package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests the calculation of the proleptic year for the BCE era with a year-of-era of 0.
     *
     * This test verifies a specific behavior of Symmetry454Chronology where
     * prolepticYear(BCE, 0) results in a proleptic year of 0. This is a key
     * data point for understanding the chronology's year mapping.
     */
    @Test
    public void prolepticYear_forBceEraAndYearOfEraZero_shouldReturnZero() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        final int yearOfEra = 0;

        // Verify the precondition that era value 0 corresponds to BCE.
        Era bceEra = chronology.eraOf(0);
        assertEquals("Precondition failed: eraOf(0) should be BCE.", IsoEra.BCE, bceEra);

        // Act
        int prolepticYear = chronology.prolepticYear(bceEra, yearOfEra);

        // Assert
        assertEquals("The proleptic year for BCE era with year-of-era 0 should be 0.", 0, prolepticYear);
    }
}