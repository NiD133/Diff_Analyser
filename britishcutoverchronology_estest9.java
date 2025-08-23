package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that dateNow(ZoneId) returns the current date for the specified time zone.
     */
    @Test
    public void dateNow_withZoneId_returnsCurrentDate() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        ZoneId systemZone = ZoneId.systemDefault();
        LocalDate expectedDate = LocalDate.now(systemZone);

        // Act
        // Obtain the current date in the BritishCutover calendar system using the system clock.
        BritishCutoverDate actualDate = chronology.dateNow(systemZone);

        // Assert
        // The returned date should correspond to today's date in the ISO calendar.
        // Note: This assertion has a small chance of failing if the test runs at the exact moment
        // the date changes (midnight), as the two calls to 'now()' might return different dates.
        assertEquals(expectedDate, actualDate.toLocalDate());
    }
}