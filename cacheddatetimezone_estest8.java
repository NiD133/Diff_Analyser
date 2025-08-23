package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CachedDateTimeZone} class.
 * This version focuses on the isFixed() method.
 */
public class CachedDateTimeZoneTest {

    /**
     * Verifies that isFixed() returns true when the CachedDateTimeZone
     * wraps a fixed time zone like UTC.
     *
     * The isFixed() method should simply delegate to the underlying zone.
     */
    @Test
    public void isFixed_shouldReturnTrue_whenWrappingFixedZone() {
        // Arrange: Create a CachedDateTimeZone wrapping a known fixed zone (UTC).
        // Using a specific zone like UTC makes the test deterministic, unlike the
        // original test which relied on the system's default time zone.
        DateTimeZone underlyingFixedZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(underlyingFixedZone);

        // Act: Call the method under test.
        boolean isFixed = cachedZone.isFixed();

        // Assert: The cached zone should report being fixed, just like its underlying zone.
        assertTrue("A CachedDateTimeZone wrapping a fixed zone should also be fixed.", isFixed);
    }

    /**
     * Verifies that isFixed() returns false when the CachedDateTimeZone
     * wraps a non-fixed (variable) time zone.
     *
     * This is a complementary test case to ensure both paths of the logic are covered.
     */
    @Test
    public void isFixed_shouldReturnFalse_whenWrappingVariableZone() {
        // Arrange: Create a CachedDateTimeZone wrapping a known non-fixed zone.
        DateTimeZone underlyingVariableZone = DateTimeZone.forID("America/New_York");
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(underlyingVariableZone);

        // Act: Call the method under test.
        boolean isFixed = cachedZone.isFixed();

        // Assert: The cached zone should report being not-fixed, just like its underlying zone.
        assertFalse("A CachedDateTimeZone wrapping a variable zone should not be fixed.", isFixed);
    }
}