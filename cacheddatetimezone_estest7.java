package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Verifies that for a fixed time zone, which has no transitions,
     * nextTransition() returns the same instant that was passed in.
     * The CachedDateTimeZone should preserve this behavior from the underlying zone.
     */
    @Test
    public void nextTransition_forFixedZone_returnsSameInstant() {
        // Arrange
        // Create a fixed-offset zone. By definition, it has no transitions.
        final DateTimeZone fixedOffsetZone = DateTimeZone.forOffsetMillis(-3600_000); // UTC-1
        final CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(fixedOffsetZone);
        final long testInstant = 123456789L;

        // Act
        final long nextTransitionInstant = cachedZone.nextTransition(testInstant);

        // Assert
        // For a fixed zone, the next transition is defined as the instant itself.
        assertEquals(testInstant, nextTransitionInstant);
    }
}