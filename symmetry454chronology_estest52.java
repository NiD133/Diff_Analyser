package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoZonedDateTime;
import org.junit.Test;

/**
 * Tests for {@link Symmetry454Chronology}.
 * This class focuses on the conversion of temporal objects from other calendar systems.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void zonedDateTime_shouldCorrectlyConvertOffsetDateTimeBeforeEpoch() {
        // --- Arrange ---
        // The Symmetry454Chronology instance to test.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Define an input based on an instant shortly before the Unix epoch.
        // The value -1343L corresponds to 1969-12-31T23:59:58.657Z in the ISO calendar.
        Instant instantBeforeEpoch = Instant.ofEpochMilli(-1343L);
        OffsetDateTime isoDateTime = OffsetDateTime.ofInstant(instantBeforeEpoch, ZoneOffset.UTC);

        // --- Act ---
        // Convert the ISO OffsetDateTime to the Symmetry454 calendar system.
        ChronoZonedDateTime<Symmetry454Date> result = chronology.zonedDateTime(isoDateTime);

        // --- Assert ---
        assertNotNull("The conversion result should not be null.", result);

        // A common Symmetry454 year has 364 days, while a common ISO year has 365.
        // Therefore, the last day of the ISO year 1969 (1969-12-31) corresponds to
        // the first day of the Symmetry454 year 1970.
        Symmetry454Date expectedDate = Symmetry454Date.of(1970, 1, 1);
        LocalTime expectedTime = LocalTime.of(23, 59, 58, 657_000_000);

        assertEquals("The converted date should match the expected Symmetry454 date.", expectedDate, result.toLocalDate());
        assertEquals("The converted time should be preserved.", expectedTime, result.toLocalTime());
        assertEquals("The zone offset should be preserved.", ZoneOffset.UTC, result.getZone());
    }
}