package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that getStandardOffset() returns the correct offset for a non-fixed zone
     * at a very distant point in the future.
     */
    @Test
    public void getStandardOffset_forWETZoneAtFarFutureInstant_returnsZero() {
        // Arrange
        // DateTimeZone.forID() wraps non-fixed zones like "WET" in a CachedDateTimeZone instance.
        // The WET (Western European Time) zone has a standard offset of 0 from UTC.
        DateTimeZone wetZone = DateTimeZone.forID("WET");

        // This test uses a very large instant value to verify the caching behavior
        // with timestamps that are far in the future. The value corresponds to a
        // date in the approximate year +31,688,764.
        final long FAR_FUTURE_INSTANT = 999999992896684031L;

        // Act
        // The getStandardOffset() method should return the zone's standard offset,
        // ignoring any daylight saving adjustments for the given instant.
        int standardOffset = wetZone.getStandardOffset(FAR_FUTURE_INSTANT);

        // Assert
        // The standard offset for WET should always be 0.
        assertEquals("Standard offset for WET should be 0", 0, standardOffset);
    }
}