package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Symmetry454Chronology#dateYearDay(Era, int, int)}.
 */
public class Symmetry454Chronology_ESTestTest10 extends Symmetry454Chronology_ESTest_scaffolding {

    /**
     * Verifies that dateYearDay(Era, year, dayOfYear) creates a date
     * with the correct era.
     */
    @Test
    public void testDateYearDayWithEraPreservesTheEra() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Era expectedEra = IsoEra.CE;
        int yearOfEra = 29;
        int dayOfYear = 11;

        // Act
        Symmetry454Date resultDate = chronology.dateYearDay(expectedEra, yearOfEra, dayOfYear);

        // Assert
        assertEquals("The era of the created date should match the input era.", expectedEra, resultDate.getEra());
    }
}