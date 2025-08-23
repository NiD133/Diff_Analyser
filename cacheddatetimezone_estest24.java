package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class verifies the behavior of {@link CachedDateTimeZone}.
 * The original test was auto-generated and has been rewritten for clarity,
 * correctness, and deterministic behavior.
 */
public class CachedDateTimeZone_ESTestTest24 {

    /**
     * Tests that getNameKey() on a cached zone returns the same value as the
     * underlying (uncached) zone for an instant just before the epoch.
     *
     * This verifies that the caching mechanism correctly delegates the call to the
     * original zone and returns the proper value for edge-case instants.
     */
    @Test
    public void getNameKey_forInstantBeforeEpoch_returnsSameValueAsUncachedZone() {
        // ARRANGE
        // Use a specific, non-fixed time zone to make the test deterministic
        // and independent of the system's default time zone.
        // "America/New_York" is a good example with historical transitions.
        DateTimeZone originalZone = DateTimeZone.forID("America/New_York");
        DateTimeZone cachedZone = CachedDateTimeZone.forZone(originalZone);

        // Use an instant just before the Unix epoch, which can be an edge case.
        long instant = -458L;

        // ACT
        // Get the expected value from the original (uncached) zone.
        String expectedNameKey = originalZone.getNameKey(instant);
        // Get the actual value from the cached zone under test.
        String actualNameKey = cachedZone.getNameKey(instant);

        // ASSERT
        // The cached zone should return the exact same name key as the original zone,
        // confirming the caching layer correctly delegates the call.
        assertEquals(expectedNameKey, actualNameKey);
    }
}