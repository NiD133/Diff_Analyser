package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.IsoEra;
import org.junit.Test;

/**
 * Tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void dateYearDay_withNegativeProlepticYear_returnsBceEra() {
        // In calendars that support eras, a proleptic year is extended backward from the
        // calendar's epoch. For ISO-based systems, year 1 is 1 CE, year 0 is 1 BCE,
        // year -1 is 2 BCE, and so on. This test verifies that a negative proleptic year
        // correctly maps to the BCE (Before Common Era) era.

        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int negativeProlepticYear = -762;
        int dayOfYear = 4;

        // Act
        Symmetry010Date date = chronology.dateYearDay(negativeProlepticYear, dayOfYear);

        // Assert
        assertEquals("A proleptic year before 1 should result in the BCE era",
                IsoEra.BCE, date.getEra());
    }
}