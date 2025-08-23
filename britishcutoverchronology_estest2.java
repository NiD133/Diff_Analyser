package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void zonedDateTime_fromInstantAfterCutover_isCorrect() {
        // This test verifies that zonedDateTime(Instant, ZoneId) correctly converts an Instant
        // that occurs *after* the British calendar cutover of 1752. In this period, the
        // BritishCutoverChronology should behave identically to the standard ISO (Gregorian) calendar.

        // Arrange
        // Use the singleton instance as recommended by the class documentation.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // This instant (1970-01-01T00:16:43.999998279Z) is well after the 1752 cutover.
        // The original test used MockInstant.ofEpochSecond(1004L, -1721L), which normalizes to this value.
        Instant testInstant = Instant.ofEpochSecond(1003, 999_998_279L);
        ZoneOffset utcZone = ZoneOffset.UTC;

        // The expected ZonedDateTime, calculated using the standard ISO chronology.
        ZonedDateTime expectedDateTime = ZonedDateTime.ofInstant(testInstant, utcZone);

        // Act
        ChronoZonedDateTime<BritishCutoverDate> actualDateTime = chronology.zonedDateTime(testInstant, utcZone);

        // Assert
        // The resulting ChronoZonedDateTime should be equal to the standard ZonedDateTime.
        // The equals() method provides a robust check of the instant and zone.
        assertEquals(expectedDateTime, actualDateTime);
    }
}