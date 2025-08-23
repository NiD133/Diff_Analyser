package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link BritishCutoverChronology#zonedDateTime(TemporalAccessor)}.
 */
public class BritishCutoverChronologyZonedDateTimeTest {

    /**
     * Tests the conversion of a modern ISO ZonedDateTime to a BritishCutoverChronology ZonedDateTime.
     * For dates after the 1752 cutover, the date and time values should be identical.
     */
    @Test
    public void zonedDateTime_withModernDate_convertsFromIsoZonedDateTimeCorrectly() {
        // Arrange
        BritishCutoverChronology britishChronology = BritishCutoverChronology.INSTANCE;
        ZonedDateTime isoZonedDateTime = ZonedDateTime.of(2024, 8, 15, 10, 30, 0, 0, ZoneId.of("Europe/London"));

        // Act
        ChronoZonedDateTime<BritishCutoverDate> britishZonedDateTime = britishChronology.zonedDateTime(isoZonedDateTime);

        // Assert
        assertNotNull("The converted ZonedDateTime should not be null.", britishZonedDateTime);
        assertEquals("The chronology of the result should be BritishCutoverChronology.",
                britishChronology, britishZonedDateTime.getChronology());
        assertEquals("For modern dates, the local date-time should be identical to the ISO source.",
                isoZonedDateTime.toLocalDateTime(), britishZonedDateTime.toLocalDateTime());
        assertEquals("The time zone should be preserved during conversion.",
                isoZonedDateTime.getZone(), britishZonedDateTime.getZone());
        assertEquals("The zone offset should be preserved during conversion.",
                isoZonedDateTime.getOffset(), britishZonedDateTime.getOffset());
    }
}