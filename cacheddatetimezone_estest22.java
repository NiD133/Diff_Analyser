package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Test suite for the {@link CachedDateTimeZone} class.
 */
public class CachedDateTimeZoneTest {

    /**
     * Joda-Time's calculations have practical limits. An instant this far in the future
     * (approximately the year 3,168,809 AD) is expected to cause an overflow when
     * the library attempts to calculate the corresponding year, leading to an exception.
     */
    private static final long INSTANT_TOO_FAR_IN_FUTURE = 100_000_000_000_000_000L;

    /**
     * Verifies that getStandardOffset throws an IllegalArgumentException when given an
     * instant that is too far in the future to be processed. This ensures that the
     * caching layer correctly propagates exceptions from the underlying time zone
     * for out-of-range values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getStandardOffset_whenInstantIsFarInFuture_throwsIllegalArgumentException() {
        // Arrange: Create a cached time zone instance for a real-world, non-fixed zone.
        // Using a specific zone like "America/New_York" makes the test deterministic,
        // unlike DateTimeZone.getDefault() which depends on the system environment.
        DateTimeZone underlyingZone = DateTimeZone.forID("America/New_York");
        DateTimeZone cachedZone = CachedDateTimeZone.forZone(underlyingZone);

        // Act: Attempt to get the standard offset for an instant far beyond the supported range.
        // This call is expected to fail and throw an exception.
        cachedZone.getStandardOffset(INSTANT_TOO_FAR_IN_FUTURE);

        // Assert: The @Test(expected=...) annotation verifies that an
        // IllegalArgumentException was thrown, fulfilling the test's expectation.
    }
}