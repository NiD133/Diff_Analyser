package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link Symmetry010Chronology}.
 * This focuses on the conversion from standard Java Time objects.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests the creation of a Symmetry010ChronoZonedDateTime from an OffsetDateTime.
     *
     * <p>The test verifies that when converting a standard {@link OffsetDateTime}
     * to the Symmetry010 calendar system, the fundamental properties like the
     * instant in time, the local time, and the zone offset are preserved correctly.
     */
    @Test
    public void zonedDateTime_fromTemporalAccessor_shouldCreateCorrectlyFromOffsetDateTime() {
        // Arrange
        // Use the singleton instance as recommended by the class documentation.
        Symmetry010Chronology symmetryChronology = Symmetry010Chronology.INSTANCE;

        // Use a fixed, specific OffsetDateTime for a deterministic and repeatable test.
        // This avoids the flakiness of using `now()`.
        OffsetDateTime isoOffsetDateTime = OffsetDateTime.of(
                LocalDateTime.of(2024, 7, 26, 10, 30, 15, 500),
                ZoneOffset.ofHours(2)
        );

        // Act
        // The method under test: creating a zoned date-time from a generic TemporalAccessor.
        ChronoZonedDateTime<Symmetry010Date> result = symmetryChronology.zonedDateTime((TemporalAccessor) isoOffsetDateTime);

        // Assert
        assertNotNull("The resulting ZonedDateTime should not be null.", result);

        // Verify that the fundamental properties of the date-time are preserved.
        // The most important check is that the absolute instant in time is identical.
        assertEquals("The instant in time should be preserved after conversion.",
                isoOffsetDateTime.toInstant(), result.toInstant());

        // Also verify that the time and zone information are correctly carried over.
        assertEquals("The local time should match the input.",
                isoOffsetDateTime.toLocalTime(), result.toLocalTime());
        assertEquals("The zone offset should match the input.",
                isoOffsetDateTime.getOffset(), result.getOffset());
        assertEquals("The chronology of the result should be Symmetry010.",
                symmetryChronology, result.getChronology());
    }
}