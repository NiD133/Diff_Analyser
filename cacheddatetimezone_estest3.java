package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that the hashCode of a CachedDateTimeZone is identical to the
     * hashCode of the original zone it wraps. The hashCode is based on the
     * time zone's ID, which should be the same for both objects.
     */
    @Test
    public void hashCode_shouldBeSameAsUncachedZone() {
        // Arrange: Use a well-known, non-fixed time zone to ensure caching is relevant.
        DateTimeZone originalZone = DateTimeZone.forID("Europe/London");
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(originalZone);

        // Act: Calculate the hash codes for both the original and cached zones.
        int originalHashCode = originalZone.hashCode();
        int cachedHashCode = cachedZone.hashCode();

        // Assert: The hash codes must be equal, even though they are different objects.
        assertNotSame("The cached zone should be a different instance from the original", originalZone, cachedZone);
        assertEquals("The hash code of the cached zone should match the original zone's hash code",
                originalHashCode, cachedHashCode);
    }
}