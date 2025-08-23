package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the behavior of the CachedDateTimeZone, focusing on how it delegates
 * properties like `isFixed` to the underlying time zone it wraps.
 */
public class CachedDateTimeZoneTest {

    /**
     * Verifies that a CachedDateTimeZone wrapping a variable-offset time zone
     * (like one with daylight saving) correctly reports that it is not fixed.
     */
    @Test
    public void isFixed_shouldReturnFalse_whenWrappingVariableTimeZone() {
        // Arrange: Create a time zone that has daylight saving transitions, so its offset is not fixed.
        // "America/New_York" is a standard example of such a zone.
        DateTimeZone variableZone = DateTimeZone.forID("America/New_York");
        
        // Act: Wrap the variable zone in a CachedDateTimeZone.
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(variableZone);

        // Assert: The isFixed() method should delegate to the original zone and return false.
        assertFalse("A cached zone for a variable time zone should not be reported as fixed.", cachedZone.isFixed());
    }

    /**
     * Verifies that a CachedDateTimeZone wrapping a fixed-offset time zone
     * (like UTC) correctly reports that it is fixed.
     */
    @Test
    public void isFixed_shouldReturnTrue_whenWrappingFixedTimeZone() {
        // Arrange: Get the UTC time zone, which has a fixed offset of zero.
        DateTimeZone fixedZone = DateTimeZone.UTC;

        // Act: Wrap the fixed zone in a CachedDateTimeZone.
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(fixedZone);

        // Assert: The isFixed() method should delegate to the original zone and return true.
        assertTrue("A cached zone for a fixed time zone should be reported as fixed.", cachedZone.isFixed());
    }
}