package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that creating a date with a positive proleptic year
     * using {@link Symmetry454Chronology#dateYearDay(int, int)}
     * correctly assigns the Common Era (CE).
     */
    @Test
    public void dateYearDay_withPositiveYear_returnsDateInCEEra() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int prolepticYear = 19;
        int dayOfYear = 19;

        // Act
        Symmetry454Date date = chronology.dateYearDay(prolepticYear, dayOfYear);

        // Assert
        assertEquals("The era for a positive year should be Common Era (CE).", IsoEra.CE, date.getEra());
    }
}