package org.joda.time.tz;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link CachedDateTimeZone}.
 * This test focuses on verifying the behavior of time zones that are expected to be cached.
 */
public class CachedDateTimeZone_ESTestTest2 extends CachedDateTimeZone_ESTest_scaffolding {

    /**
     * Tests that getName() for a non-fixed time zone returns the correct standard time name
     * for an instant that falls during the winter.
     */
    @Test
    public void getNameForWinterInstantShouldReturnStandardTimeName() {
        // Arrange
        // The "WET" zone (Western European Time) is not fixed due to its daylight saving
        // variant, "WEST". Non-fixed zones are wrapped in a CachedDateTimeZone by default.
        DateTimeZone wetZone = DateTimeZone.forID("WET");

        // An instant during winter, when standard time (WET) is in effect.
        // Using a concrete date makes the test's intent clear.
        long winterInstant = new DateTime(2023, 1, 15, 12, 0, DateTimeZone.UTC).getMillis();

        String expectedName = "Western European Time";

        // Act
        String actualName = wetZone.getName(winterInstant);

        // Assert
        assertEquals(expectedName, actualName);
    }
}