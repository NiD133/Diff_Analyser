package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoZonedDateTime;
import org.junit.Test;

/**
 * This test class has been refactored for understandability.
 * The original was an auto-generated test with a vague name and weak assertions.
 */
public class Symmetry454Chronology_ESTestTest2 {

    /**
     * Tests that converting an Instant to a Symmetry454 ZonedDateTime produces the correct date and time.
     * The Symmetry454 calendar is aligned with the standard ISO calendar at the Unix epoch,
     * so the date and time values should match.
     */
    @Test
    public void zonedDateTime_fromInstant_createsCorrectDateTime() {
        // Arrange: Define the chronology, a specific instant in time, and a time zone.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Instant instant = Instant.ofEpochSecond(3L); // Represents 1970-01-01T00:00:03Z
        ZoneId utcZone = ZoneOffset.UTC;

        // Act: Convert the Instant to a ZonedDateTime using the Symmetry454 chronology.
        ChronoZonedDateTime<Symmetry454Date> result = chronology.zonedDateTime(instant, utcZone);

        // Assert: Verify that the resulting date, time, and zone are correct.
        Symmetry454Date expectedDate = Symmetry454Date.of(1970, 1, 1);
        LocalTime expectedTime = LocalTime.of(0, 0, 3);

        assertNotNull("The resulting ZonedDateTime should not be null", result);
        assertEquals("The date part should be correct", expectedDate, result.toLocalDate());
        assertEquals("The time part should be correct", expectedTime, result.toLocalTime());
        assertEquals("The time zone should be correct", utcZone, result.getZone());
        assertEquals("The chronology should be correct", chronology, result.getChronology());
    }
}