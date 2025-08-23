package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Symmetry010Chronology}.
 * This class focuses on the date creation methods.
 */
public class Symmetry010Chronology_ESTestTest13 extends Symmetry010Chronology_ESTest_scaffolding {

    /**
     * Tests that the dateYearDay(Era, year, dayOfYear) method correctly constructs a date
     * with the specified properties.
     */
    @Test
    public void dateYearDayWithEra_shouldCreateDateWithCorrectProperties() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Era expectedEra = IsoEra.CE;
        int yearOfEra = 14;
        int dayOfYear = 30;

        // Act
        Symmetry010Date resultDate = chronology.dateYearDay(expectedEra, yearOfEra, dayOfYear);

        // Assert
        assertEquals("The era should match the input era.", expectedEra, resultDate.getEra());
        assertEquals("The year of era should match the input year.", yearOfEra, resultDate.get(ChronoField.YEAR_OF_ERA));
        assertEquals("The day of year should match the input day-of-year.", dayOfYear, resultDate.get(ChronoField.DAY_OF_YEAR));
    }
}