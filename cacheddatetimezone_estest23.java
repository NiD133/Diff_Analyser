package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the {@link CachedDateTimeZone} class.
 */
public class CachedDateTimeZoneTest {

    /**
     * This instant is a very large millisecond value, chosen because it is expected
     * to be outside the valid calculation range of Joda-Time, leading to an exception.
     * It corresponds to a date tens of millions of years in the future.
     */
    private static final long INSTANT_FAR_IN_THE_FUTURE = 100_000_000_000_000_000L;

    @Test(expected = IllegalArgumentException.class)
    public void getOffsetForInstantFarInFutureThrowsException() {
        // Arrange: Create a CachedDateTimeZone for a well-known, non-fixed time zone.
        // Using a specific ID like "Europe/London" makes the test deterministic,
        // unlike DateTimeZone.getDefault(), which depends on the system environment.
        DateTimeZone londonZone = DateTimeZone.forID("Europe/London");
        DateTimeZone cachedZone = CachedDateTimeZone.forZone(londonZone);

        // Act: Request the offset for an instant far in the future.
        // Assert: An IllegalArgumentException is expected, as declared in the @Test annotation.
        // This happens because the instant is too large for the time zone transition
        // calculations to handle without overflowing.
        cachedZone.getOffset(INSTANT_FAR_IN_THE_FUTURE);
    }
}