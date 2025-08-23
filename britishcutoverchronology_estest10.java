package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void dateEpochDay_withDateBeforeEpoch_createsCorrectDate() {
        // Arrange: Define a date before the standard Java epoch (1970-01-01).
        // The epoch day -1813 corresponds to the ISO date 1965-01-15.
        // This date is after the British cutover of 1752, so it follows Gregorian rules.
        long epochDay = -1813L;
        LocalDate expectedIsoDate = LocalDate.of(1965, 1, 15);
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act: Create a BritishCutoverDate from the epoch day.
        BritishCutoverDate actualDate = chronology.dateEpochDay(epochDay);

        // Assert: The created date should be equivalent to the expected ISO date.
        assertEquals(expectedIsoDate, LocalDate.from(actualDate));
    }
}