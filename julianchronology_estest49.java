package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JulianChronology_ESTestTest49 extends JulianChronology_ESTest_scaffolding {

    /**
     * Tests that converting an ISO {@link OffsetDateTime} to a Julian {@link ChronoZonedDateTime}
     * produces the correct date, time, and zone.
     */
    @Test
    public void zonedDateTime_fromOffsetDateTime_shouldConvertCorrectly() {
        // Arrange: Define a specific ISO OffsetDateTime.
        // The Julian calendar is 13 days behind the Gregorian/ISO calendar for dates in the 20th/21st centuries.
        // ISO date: 2024-03-11 -> Expected Julian date: 2024-02-27
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        ZoneOffset offset = ZoneOffset.ofHours(2);
        OffsetDateTime isoOffsetDateTime = OffsetDateTime.of(2024, 3, 11, 15, 45, 30, 0, offset);

        // Act: Convert the ISO OffsetDateTime to a Julian ChronoZonedDateTime.
        ChronoZonedDateTime<JulianDate> julianZonedDateTime = julianChronology.zonedDateTime(isoOffsetDateTime);

        // Assert: Verify that the resulting Julian date, time, and zone are correct.
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2024, 2, 27, 15, 45, 30);

        assertNotNull("The resulting ZonedDateTime should not be null", julianZonedDateTime);
        assertEquals("The local date-time should be converted correctly to the Julian calendar",
                expectedLocalDateTime, julianZonedDateTime.toLocalDateTime());
        assertEquals("The zone offset should be preserved",
                offset, julianZonedDateTime.getOffset());
        assertEquals("The zone ID should be the same as the offset",
                offset, julianZonedDateTime.getZone());
    }
}