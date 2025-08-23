package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void date_forPositiveYear_returnsDateWithCEEra() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int year = 12;
        int month = 12;
        int day = 12;

        // Act
        Symmetry010Date date = chronology.date(year, month, day);

        // Assert
        assertEquals("The era for a positive year should be Common Era (CE)", IsoEra.CE, date.getEra());
    }
}