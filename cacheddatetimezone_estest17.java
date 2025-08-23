package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that a CachedDateTimeZone instance is not considered equal to the
     * original DateTimeZone instance it wraps.
     *
     * The equals() implementation in CachedDateTimeZone requires the other object
     * to also be a CachedDateTimeZone for equality to be possible.
     */
    @Test
    public void equals_returnsFalse_whenComparedWithUncachedZone() {
        // Arrange: Create a standard, non-fixed time zone and its cached wrapper.
        // Using a specific ID like "Europe/London" makes the test deterministic
        // and independent of the system's default time zone.
        DateTimeZone originalZone = DateTimeZone.forID("Europe/London");
        DateTimeZone cachedZone = CachedDateTimeZone.forZone(originalZone);

        // Sanity check to ensure we are not comparing the same object instance.
        assertNotEquals("The cached zone should be a different object than the original",
                originalZone, cachedZone);

        // Act & Assert: Verify that the cached zone is not equal to the original zone.
        assertFalse("A CachedDateTimeZone instance should not be equal to the raw DateTimeZone it wraps.",
                cachedZone.equals(originalZone));
    }
}