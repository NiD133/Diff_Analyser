package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    @Test
    public void isLocalDateTimeGap_shouldReturnFalse_whenDateTimeIsNotInTransition() {
        // Arrange
        // WET (Western European Time) is a non-fixed zone with Daylight Saving Time,
        // so DateTimeZone.forID("WET") returns a CachedDateTimeZone instance.
        DateTimeZone wetZone = DateTimeZone.forID("WET");

        // We choose a date in mid-January, which is guaranteed not to be within
        // a DST transition period for any European time zone.
        LocalDateTime dateTimeInWinter = new LocalDateTime(2024, 1, 15, 12, 0);

        // Act
        // Check if the local date-time falls within a DST gap.
        boolean isInGap = wetZone.isLocalDateTimeGap(dateTimeInWinter);

        // Assert
        assertFalse("A date in mid-winter should not be in a DST gap.", isInGap);
    }
}