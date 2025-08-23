package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the prolepticYear method in {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronology_ESTestTest5 { // Note: Class name kept from original

    /**
     * Tests that the proleptic year is the same as the year-of-era for the Common Era (CE).
     * The International Fixed Chronology has only one era, so this conversion is expected to be
     * an identity function for valid eras.
     */
    @Test
    public void prolepticYear_forCEEra_returnsSameAsYearOfEra() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedEra era = InternationalFixedEra.CE;
        int yearOfEra = 734;

        // Act
        int actualProlepticYear = chronology.prolepticYear(era, yearOfEra);

        // Assert
        assertEquals(yearOfEra, actualProlepticYear);
    }
}