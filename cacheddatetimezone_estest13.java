package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Verifies that getOffset() for a cached UTC zone correctly returns a zero offset.
     *
     * <p>This test uses a fixed time zone (UTC) to ensure it is deterministic and
     * independent of the system's default time zone.
     */
    @Test
    public void getOffsetForCachedUtcZoneShouldReturnZero() {
        // Arrange
        // Use a fixed time zone like UTC to ensure the test is stable and predictable.
        DateTimeZone utcZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedUtcZone = CachedDateTimeZone.forZone(utcZone);

        // An arbitrary instant in milliseconds just before the epoch.
        long instantNearEpoch = -418L;
        int expectedOffset = 0; // The offset for UTC is always zero.

        // Act
        int actualOffset = cachedUtcZone.getOffset(instantNearEpoch);

        // Assert
        assertEquals("The offset for a cached UTC zone should be zero.", expectedOffset, actualOffset);
    }
}