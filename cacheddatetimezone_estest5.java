package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the behavior of the CachedDateTimeZone class.
 * This specific test case verifies the behavior of the previousTransition() method
 * when used with a time zone that has no transitions, such as UTC.
 */
public class CachedDateTimeZone_ESTestTest5 {

    /**
     * Tests that for a fixed time zone like UTC, which has no transitions,
     * previousTransition() returns the same instant that was passed in.
     * The underlying DateTimeZone is documented to behave this way, and this
     * test ensures the cached wrapper preserves that behavior.
     */
    @Test
    public void previousTransition_forFixedZone_returnsSameInstant() {
        // Arrange
        // UTC is a fixed time zone with no daylight saving or other transitions.
        DateTimeZone utcZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedUtcZone = CachedDateTimeZone.forZone(utcZone);
        long testInstant = -983L;

        // Act
        long resultInstant = cachedUtcZone.previousTransition(testInstant);

        // Assert
        assertEquals(
            "For a fixed zone with no transitions, previousTransition should return the input instant.",
            testInstant,
            resultInstant
        );
    }
}