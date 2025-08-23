package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;
import org.junit.Test;

/**
 * Tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that creating a date from epoch day 0 results in the correct
     * representation of the epoch date, 1970-01-01.
     */
    @Test
    public void dateEpochDay_whenEpochDayIsZero_returnsDateFor1970_01_01() {
        // Arrange: The Symmetry010 chronology and the epoch day for 1970-01-01.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        long epochDay = 0L;

        // Act: Create a Symmetry010Date from the epoch day.
        Symmetry010Date date = chronology.dateEpochDay(epochDay);

        // Assert: The resulting date should correspond to 1970-01-01.
        // The source documentation states this calendar is aligned with the ISO calendar.
        assertEquals("Era should be Common Era", IsoEra.CE, date.getEra());
        assertEquals("Year should be 1970", 1970, date.get(ChronoField.YEAR));
        assertEquals("Month should be 1", 1, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals("Day should be 1", 1, date.get(ChronoField.DAY_OF_MONTH));
    }
}