package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link InternationalFixedChronology#zonedDateTime(TemporalAccessor)}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void zonedDateTime_whenConvertingFromIsoZonedDateTime_thenReturnsCorrectIfcDateTime() {
        // Arrange
        // The International Fixed Chronology (IFC) has 13 months of 28 days each.
        // We will test the conversion of an ISO date to its IFC equivalent.
        // The ISO date 2023-02-01 is the 32nd day of the year.
        // In IFC, this corresponds to the 4th day of the 2nd month (since Month 1 has 28 days, 32 - 28 = 4).
        InternationalFixedChronology ifcChronology = InternationalFixedChronology.INSTANCE;
        ZonedDateTime isoZonedDateTime = ZonedDateTime.of(2023, 2, 1, 15, 45, 30, 0, ZoneOffset.ofHours(-5));

        // Act
        ChronoZonedDateTime<InternationalFixedDate> ifcZonedDateTime = ifcChronology.zonedDateTime(isoZonedDateTime);

        // Assert
        assertNotNull("The resulting ZonedDateTime should not be null", ifcZonedDateTime);

        // Verify that the underlying point in time and zone information are preserved
        assertEquals("The underlying instant must be identical",
                isoZonedDateTime.toInstant(), ifcZonedDateTime.toInstant());
        assertEquals("The time zone must be preserved",
                isoZonedDateTime.getZone(), ifcZonedDateTime.getZone());
        assertEquals("The chronology should be InternationalFixed",
                ifcChronology, ifcZonedDateTime.getChronology());

        // Verify the date components are correctly converted to the International Fixed calendar
        assertEquals("Year should be 2023", 2023, ifcZonedDateTime.get(ChronoField.YEAR));
        assertEquals("Month should be 2", 2, ifcZonedDateTime.get(ChronoField.MONTH_OF_YEAR));
        assertEquals("Day of month should be 4", 4, ifcZonedDateTime.get(ChronoField.DAY_OF_MONTH));

        // Verify the time components are unchanged
        assertEquals("Hour should be 15", 15, ifcZonedDateTime.get(ChronoField.HOUR_OF_DAY));
        assertEquals("Minute should be 45", 45, ifcZonedDateTime.get(ChronoField.MINUTE_OF_HOUR));
        assertEquals("Second should be 30", 30, ifcZonedDateTime.get(ChronoField.SECOND_OF_MINUTE));
    }
}