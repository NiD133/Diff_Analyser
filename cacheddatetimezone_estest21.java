package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that the hashCode of a CachedDateTimeZone is the same as the hashCode
     * of the original zone it wraps.
     */
    @Test
    public void hashCode_shouldBeSameAsUncachedZone() {
        // Arrange: Create a standard, non-fixed time zone.
        // "America/New_York" is a good example of a complex time zone that would benefit from caching.
        DateTimeZone originalZone = DateTimeZone.forID("America/New_York");

        // Act: Wrap the original zone with a CachedDateTimeZone using the factory method.
        DateTimeZone cachedZone = CachedDateTimeZone.forZone(originalZone);

        // Assert: The hashCode of the cached zone should delegate to the original zone's hashCode.
        // This confirms the decorator correctly preserves the hash code contract.
        assertEquals(originalZone.hashCode(), cachedZone.hashCode());

        // Further assert that we are indeed testing a decorator, not the original object itself.
        assertNotSame("The factory method should return a new CachedDateTimeZone instance",
                      originalZone, cachedZone);
    }
}