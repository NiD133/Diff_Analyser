package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that isFixed() returns false when the CachedDateTimeZone wraps a
     * non-fixed (variable) time zone.
     */
    @Test
    public void isFixed_shouldReturnFalse_whenWrappingNonFixedZone() {
        // Arrange: Create a CachedDateTimeZone that wraps a non-fixed time zone.
        // "WET" (Western European Time) is not fixed because it observes daylight saving time.
        DateTimeZone nonFixedZone = DateTimeZone.forID("WET");
        DateTimeZone cachedZone = CachedDateTimeZone.forZone(nonFixedZone);

        // Act & Assert: Verify that the cached zone correctly reports that it is not fixed.
        // The isFixed() method is expected to delegate to the underlying zone.
        assertFalse("A cached zone wrapping a non-fixed zone should not be reported as fixed.", cachedZone.isFixed());
    }
}