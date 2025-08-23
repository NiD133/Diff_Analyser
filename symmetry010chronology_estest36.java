package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link Symmetry010Chronology#zonedDateTime(Instant, ZoneId)} method.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void zonedDateTime_fromInstantAndZone_shouldCreateCorrectZonedDateTime() {
        // Arrange: Set up a known instant and time zone.
        // The Symmetry010Chronology is a singleton, so we use its INSTANCE.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Instant testInstant = Instant.parse("2023-10-27T10:00:00Z");
        ZoneId utcZone = ZoneOffset.UTC;

        // Act: Call the method under test to convert the Instant to a ZonedDateTime
        // in the Symmetry010 calendar system.
        ChronoZonedDateTime<Symmetry010Date> result = chronology.zonedDateTime(testInstant, utcZone);

        // Assert: Verify that the conversion was successful and correct.
        // 1. The result should not be null.
        assertNotNull("The resulting ZonedDateTime should not be null.", result);

        // 2. The chronology of the result should be the Symmetry010 chronology.
        assertEquals("The chronology of the result should be Symmetry010.",
                chronology, result.getChronology());

        // 3. Converting the result back to an Instant should yield the original Instant.
        assertEquals("The instant represented by the result should match the input instant.",
                testInstant, result.toInstant());
    }
}