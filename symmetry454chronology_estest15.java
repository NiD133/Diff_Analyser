package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import org.junit.Test;

/**
 * Tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void date_withEra_shouldCreateDateWithCorrectEra() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Era expectedEra = IsoEra.CE;
        int yearOfEra = 4;
        int month = 4;
        int dayOfMonth = 4;

        // Act
        Symmetry454Date date = chronology.date(expectedEra, yearOfEra, month, dayOfMonth);

        // Assert
        assertEquals("The era of the created date should match the input era.", expectedEra, date.getEra());
    }
}