package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoZonedDateTime;

import static org.junit.Assert.assertEquals;

public class JulianChronology_ESTestTest3 extends JulianChronology_ESTest_scaffolding {

    /**
     * Tests the conversion of a standard {@link Instant} to a {@link ChronoZonedDateTime}
     * in the Julian calendar system.
     */
    @Test
    public void zonedDateTime_shouldCreateCorrectDateTime_whenConvertingFromInstant() {
        // Arrange: Define an instant just after the standard epoch and a timezone with the maximum offset.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        Instant instant = Instant.ofEpochMilli(3L); // Represents 1970-01-01T00:00:00.003Z in ISO
        ZoneId zone = ZoneOffset.MAX; // +18:00

        // Act: Convert the instant to a ZonedDateTime using the Julian chronology.
        ChronoZonedDateTime<JulianDate> result = julianChronology.zonedDateTime(instant, zone);

        // Assert: Verify that the resulting Julian date, time, and zone are correct.
        // The instant 1970-01-01T00:00:00.003Z at +18:00 is 1970-01-01T18:00:00.003.
        // The ISO date 1970-01-01 corresponds to the Julian date 1969-12-19.
        LocalDateTime expectedLocalDateTime = JulianDate.of(1969, 12, 19).atTime(18, 0, 0, 3_000_000);

        assertEquals("The local date-time should be correctly converted to the Julian calendar",
                expectedLocalDateTime, result.toLocalDateTime());
        assertEquals("The zone should be preserved", zone, result.getZone());
    }
}