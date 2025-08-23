package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that getOffset() for a cached fixed-offset zone correctly returns
     * the underlying zone's fixed offset.
     */
    @Test
    public void getOffsetForFixedZoneShouldReturnTheFixedOffset() {
        // Arrange
        final int fixedOffsetMillis = -2614;
        final DateTimeZone fixedZone = DateTimeZone.forOffsetMillis(fixedOffsetMillis);
        final CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(fixedZone);

        // An arbitrary instant in time. For a fixed zone, any instant should yield the same offset.
        final long arbitraryInstant = 86_400_000L; // 24 hours in milliseconds

        // Act
        int actualOffset = cachedZone.getOffset(arbitraryInstant);

        // Assert
        assertEquals("The cached zone should return the same offset as the original fixed zone.",
                fixedOffsetMillis, actualOffset);
    }
}