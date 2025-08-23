package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void prolepticYear_whenYearOfEraIsBelowMinimum_throwsException() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Era era = IsoEra.CE;
        // The valid range for yearOfEra is -1,000,000 to 1,000,000.
        // This test uses a value just below the minimum boundary.
        int yearBelowMinimum = -1_000_001;

        // Act & Assert
        try {
            chronology.prolepticYear(era, yearBelowMinimum);
            fail("Expected a DateTimeException because the year of era is out of range.");
        } catch (DateTimeException ex) {
            String expectedMessage = "Invalid value for YearOfEra (valid values -1000000 - 1000000): " + yearBelowMinimum;
            assertEquals(expectedMessage, ex.getMessage());
        }
    }
}