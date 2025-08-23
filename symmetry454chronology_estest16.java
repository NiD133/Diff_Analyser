package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void date_forPositiveYear_returnsDateInCEEra() {
        // Arrange: Set up the chronology and the date components.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int prolepticYear = 3;
        int month = 2;
        int dayOfMonth = 3;

        // Act: Create a date using the chronology.
        Symmetry454Date date = chronology.date(prolepticYear, month, dayOfMonth);

        // Assert: Verify that the date's era is the Common Era (CE).
        assertEquals("The era for a positive year should be Common Era (CE).", IsoEra.CE, date.getEra());
    }
}