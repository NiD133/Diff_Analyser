package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    @Test
    public void getNameKey_forCachedUTCZone_returnsUTC() {
        // Arrange
        // Use a specific, non-default time zone (UTC) to ensure the test is deterministic.
        // The behavior of a test should not depend on the environment's default time zone.
        DateTimeZone utcZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedUtcZone = CachedDateTimeZone.forZone(utcZone);

        // The specific instant does not matter for a fixed zone like UTC,
        // as the name key is always the same.
        long instant = -1048L;
        String expectedNameKey = "UTC";

        // Act
        String actualNameKey = cachedUtcZone.getNameKey(instant);

        // Assert
        assertEquals("The name key for the cached UTC zone should always be 'UTC'",
                     expectedNameKey, actualNameKey);
    }
}