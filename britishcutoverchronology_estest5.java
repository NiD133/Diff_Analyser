package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link BritishCutoverChronology#localDateTime(TemporalAccessor)}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests the conversion of a modern ZonedDateTime (post-cutover) to a
     * BritishCutoverChronology local date-time.
     */
    @Test
    public void localDateTime_fromModernZonedDateTime_convertsCorrectly() {
        // Arrange: Set up the chronology and a fixed, modern ZonedDateTime.
        // A modern date is well after the 1752 cutover, so it should behave like the standard ISO calendar.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        ZonedDateTime modernZonedDateTime = ZonedDateTime.of(
                2024, 5, 20, 10, 15, 30, 0, ZoneId.of("Europe/London"));

        // Act: Call the method under test to perform the conversion.
        ChronoLocalDateTime<BritishCutoverDate> result = chronology.localDateTime(modernZonedDateTime);

        // Assert: Verify that the conversion was successful and correct.
        assertNotNull("The resulting ChronoLocalDateTime should not be null.", result);

        // For a modern date, the resulting local date-time should be equivalent to the
        // ISO local date-time from the original ZonedDateTime.
        LocalDateTime expectedLocalDateTime = modernZonedDateTime.toLocalDateTime();
        assertEquals("The converted local date-time should match the original's local part.",
                expectedLocalDateTime, result.toLocalDateTime());
    }
}