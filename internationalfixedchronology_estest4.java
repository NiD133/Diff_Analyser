package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoZonedDateTime;
import org.junit.Test;

/**
 * Tests for {@link InternationalFixedChronology}.
 * This class focuses on the creation of zoned date-times.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests the conversion of a UTC Instant to a zoned date-time in the International Fixed calendar.
     */
    @Test
    public void zonedDateTimeFromInstantShouldConvertToCorrectDateAndTime() {
        // Arrange: Define the chronology, a specific instant in time, and a timezone.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        
        // This instant corresponds to 1969-12-31T23:34:48.000107016Z in UTC.
        Instant instant = Instant.ofEpochSecond(-1512L, 107016L);
        
        // Use the maximum positive offset, which shifts the local time into the next day.
        ZoneId zone = ZoneOffset.MAX; // +18:00

        // Act: Convert the Instant to a ZonedDateTime in the International Fixed chronology.
        ChronoZonedDateTime<InternationalFixedDate> ifcZonedDateTime = chronology.zonedDateTime(instant, zone);

        // Assert: Verify that the resulting date, time, and zone are correct.
        // The instant, when viewed in the +18:00 zone, becomes 1970-01-01T17:34:48.000107016.
        // The International Fixed calendar aligns with the ISO calendar on this date.
        assertNotNull("The resulting ZonedDateTime should not be null.", ifcZonedDateTime);

        InternationalFixedDate expectedDate = InternationalFixedDate.of(1970, 1, 1);
        assertEquals("The date part should be the first day of 1970.", expectedDate, ifcZonedDateTime.toLocalDate());
        
        assertEquals("Hour should be correct.", 17, ifcZonedDateTime.toLocalTime().getHour());
        assertEquals("Minute should be correct.", 34, ifcZonedDateTime.toLocalTime().getMinute());
        assertEquals("Second should be correct.", 48, ifcZonedDateTime.toLocalTime().getSecond());
        assertEquals("Nanosecond should be correct.", 107016, ifcZonedDateTime.toLocalTime().getNano());
        
        assertEquals("The zone should be preserved.", zone, ifcZonedDateTime.getZone());
        assertEquals("The offset should match the zone.", ZoneOffset.MAX, ifcZonedDateTime.getOffset());
    }
}