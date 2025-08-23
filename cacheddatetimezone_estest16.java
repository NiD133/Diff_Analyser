package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    // This instant, -4294967296L milliseconds from the epoch, corresponds to 1969-11-12T00:00:00Z.
    // This value is significant because it is -2^32, which aligns with the start of a cache period
    // in CachedDateTimeZone, making it an important boundary condition to test.
    private static final long HISTORIC_INSTANT_AT_CACHE_BOUNDARY = -4294967296L;

    /**
     * Tests that getNameKey() returns the correct historical time zone key for an instant
     * that falls on an internal cache boundary.
     *
     * <p>For the "WET" (Western European Time) zone, the time zone rules in late 1969
     * were different from today. For example, Portugal used CET (Central European Time) between 1966 and 1976.
     * This test verifies that the caching mechanism correctly retrieves the historical name key ("CET")
     * for an instant during that period.
     */
    @Test
    public void getNameKey_forHistoricInstantAtCacheBoundary_returnsCorrectKey() {
        // Arrange
        // The WET time zone is used by countries like the UK and Portugal.
        // DateTimeZone.forID() may return a CachedDateTimeZone instance.
        DateTimeZone westernEuropeanTime = DateTimeZone.forID("WET");
        String expectedNameKey = "CET"; // Central European Time

        // Act
        // Get the name key for the specific historic instant.
        String actualNameKey = westernEuropeanTime.getNameKey(HISTORIC_INSTANT_AT_CACHE_BOUNDARY);

        // Assert
        assertEquals("The name key for WET in late 1969 should be CET due to historical rules.",
                expectedNameKey, actualNameKey);
    }
}