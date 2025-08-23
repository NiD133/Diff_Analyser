package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that for a fixed-offset time zone (which has no transitions),
     * nextTransition() returns the same instant that was passed in.
     */
    @Test
    public void nextTransition_onFixedZone_returnsSameInstant() {
        // --- Arrange ---
        // A fixed-offset time zone has no daylight saving or historical rule changes,
        // so it has no transitions.
        DateTimeZone fixedZone = DateTimeZone.forOffsetMillis(-3600 * 1000); // UTC-1
        CachedDateTimeZone cachedFixedZone = CachedDateTimeZone.forZone(fixedZone);

        long testInstant = 123456789L;

        // --- Act ---
        // Ask for the next transition after the given instant.
        long result = cachedFixedZone.nextTransition(testInstant);

        // --- Assert ---
        // For a zone with no transitions, the method should return the original instant.
        assertEquals("For a fixed zone, nextTransition should return the input instant",
                testInstant, result);
    }
}