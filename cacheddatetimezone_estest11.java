package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that getStandardOffset() for a cached, fixed-offset zone
     * returns the correct, unchanging offset.
     *
     * <p>For a time zone with a fixed offset (no daylight saving), the standard offset
     * should always be equal to the zone's total offset.
     */
    @Test
    public void getStandardOffset_forFixedZone_returnsUnderlyingZoneOffset() {
        // Arrange
        final int fixedOffsetMillis = -2614;
        final long epochInstant = 0L;

        // Create a non-cached, fixed-offset time zone.
        DateTimeZone underlyingZone = DateTimeZone.forOffsetMillis(fixedOffsetMillis);

        // Create the cached wrapper for the zone.
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(underlyingZone);

        // Act
        // Request the standard offset from the cached zone.
        int actualStandardOffset = cachedZone.getStandardOffset(epochInstant);

        // Assert
        // The returned standard offset should match the original fixed offset.
        assertEquals(fixedOffsetMillis, actualStandardOffset);
    }
}