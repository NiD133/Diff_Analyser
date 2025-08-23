package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the toString() method of {@link IslamicChronology}.
 */
@DisplayName("IslamicChronology.toString()")
class IslamicChronologyToStringTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    @Test
    @DisplayName("should return the chronology name and zone ID for a specific time zone")
    void toString_withSpecificZone_returnsNameAndZoneId() {
        // Act & Assert
        assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance(LONDON).toString());
        assertEquals("IslamicChronology[Asia/Tokyo]", IslamicChronology.getInstance(TOKYO).toString());
    }

    @Test
    @DisplayName("should return the chronology name and 'UTC' for a UTC instance")
    void toString_withUTCInstance_returnsNameAndUTC() {
        // Act & Assert
        assertEquals("IslamicChronology[UTC]", IslamicChronology.getInstanceUTC().toString());
    }

    @Test
    @DisplayName("should use the default time zone when no zone is specified")
    void toString_withDefaultZone_usesDefaultZoneId() {
        DateTimeZone originalDefaultZone = DateTimeZone.getDefault();
        try {
            // Arrange: Set a known default time zone for this test
            DateTimeZone.setDefault(LONDON);

            // Act & Assert
            assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance().toString());
        } finally {
            // Teardown: Restore the original default time zone to avoid side effects
            DateTimeZone.setDefault(originalDefaultZone);
        }
    }
}