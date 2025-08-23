package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry010Chronology}.
 * This focuses on the prolepticYear method's handling of invalid era types.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that prolepticYear() throws a ClassCastException when an era
     * that is not an IsoEra is provided. The Symmetry010Chronology only
     * supports IsoEra (CE and BCE).
     */
    @Test
    public void prolepticYear_shouldThrowException_forNonIsoEra() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Era invalidEra = JapaneseEra.MEIJI;
        int yearOfEra = 1; // The specific year value is irrelevant for this test.

        // Act & Assert
        try {
            chronology.prolepticYear(invalidEra, yearOfEra);
            fail("Expected ClassCastException was not thrown for an invalid era type.");
        } catch (ClassCastException e) {
            // The method should reject eras that are not IsoEra.
            assertEquals("Invalid era: Meiji", e.getMessage());
        }
    }
}