package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link CachedDateTimeZone} class.
 *
 * Note: The original test class name "CachedDateTimeZone_ESTestTest28" and its
 * inheritance from a scaffolding class were artifacts of a test generation tool.
 * The class has been simplified to focus on the test's core logic.
 */
public class CachedDateTimeZoneTest {

    /**
     * Verifies that getStandardOffset() correctly returns the standard offset
     * for the wrapped time zone. This test uses UTC, which has a constant
     * standard offset of zero.
     */
    @Test
    public void getStandardOffsetShouldReturnCorrectOffsetForUtcZone() {
        // Arrange
        // Use a fixed time zone (UTC) instead of the system default to ensure the test is deterministic.
        // The standard offset for UTC is always 0.
        final DateTimeZone utcZone = DateTimeZone.UTC;
        final CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(utcZone);
        
        final long instantNearEpoch = 6L;
        final int expectedOffset = 0;

        // Act
        final int actualOffset = cachedZone.getStandardOffset(instantNearEpoch);

        // Assert
        assertEquals("The standard offset for UTC should always be 0.", expectedOffset, actualOffset);
    }
}