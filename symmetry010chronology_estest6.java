package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link Symmetry010Chronology} class, focusing on local date-time creation.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that a standard ISO OffsetDateTime can be correctly converted into a
     * Symmetry010Chronology-based ChronoLocalDateTime.
     *
     * The test verifies that the conversion is successful, the resulting object uses the
     * correct chronology, and the time-of-day information is preserved.
     */
    @Test
    public void localDateTime_shouldConvertFromTemporalAccessor() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        OffsetDateTime isoDateTime = OffsetDateTime.of(2023, 10, 27, 14, 45, 30, 0, ZoneOffset.UTC);
        LocalTime expectedTime = isoDateTime.toLocalTime();

        // Act
        ChronoLocalDateTime<Symmetry010Date> sym010DateTime = chronology.localDateTime(isoDateTime);

        // Assert
        assertNotNull("The conversion should produce a non-null result.", sym010DateTime);
        assertEquals("The chronology of the result should be Symmetry010.", chronology, sym010DateTime.getChronology());
        assertEquals("The time part of the date-time should be preserved.", expectedTime, sym010DateTime.toLocalTime());
    }
}