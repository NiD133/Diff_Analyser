package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import org.junit.Test;

/**
 * Unit tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void date_withEraYearMonthDay_shouldCreateDateWithCorrectEra() {
        // This test verifies that the date factory method `date(Era, int, int, int)`
        // correctly assigns the provided era to the created Symmetry010Date object.

        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Era expectedEra = IsoEra.CE;
        int yearOfEra = 10;
        int month = 10;
        int dayOfMonth = 10;

        // Act
        Symmetry010Date createdDate = chronology.date(expectedEra, yearOfEra, month, dayOfMonth);

        // Assert
        assertEquals("The era of the created date should match the input era.",
                expectedEra, createdDate.getEra());
    }
}