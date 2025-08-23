package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that getUncachedZone() returns the original DateTimeZone instance
     * that the CachedDateTimeZone was created with.
     */
    @Test
    public void getUncachedZone_shouldReturnTheOriginalZoneInstance() {
        // Arrange: Create a specific, non-fixed time zone.
        // Using a specific zone like "Europe/London" makes the test more stable
        // and independent of the system's default time zone.
        DateTimeZone originalZone = DateTimeZone.forID("Europe/London");

        // Act: Create a cached wrapper and then retrieve the underlying zone.
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(originalZone);
        DateTimeZone unwrappedZone = cachedZone.getUncachedZone();

        // Assert: The unwrapped zone should be the exact same instance as the original.
        // We use assertSame to check for object identity, which is the contract
        // of getUncachedZone().
        assertSame("The unwrapped zone should be the same instance as the original", originalZone, unwrappedZone);
    }
}