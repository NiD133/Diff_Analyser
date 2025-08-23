package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link CachedDateTimeZone}.
 * This focuses on improving the understandability of a single test case.
 */
// The class name and inheritance are kept from the original Evosuite-generated test.
public class CachedDateTimeZone_ESTestTest1 extends CachedDateTimeZone_ESTest_scaffolding {

    /**
     * Tests that {@link CachedDateTimeZone#getName(long)} correctly delegates the call
     * to the underlying time zone and returns the appropriate name for a given instant.
     */
    @Test
    public void getNameForInstantDelegatesToUnderlyingZone() {
        // Arrange
        // Use a well-known, stable time zone for a clear and deterministic test.
        // UTC is an excellent choice as its name is constant.
        final DateTimeZone underlyingZone = DateTimeZone.UTC;
        final CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(underlyingZone);

        // An arbitrary instant in time, corresponding to 1976-09-18 08:00:00 UTC.
        // The specific date is not critical for UTC, as its name doesn't change.
        final long instant = 212544000000L;
        final String expectedName = "Coordinated Universal Time";

        // Act
        final String actualName = cachedZone.getName(instant);

        // Assert
        assertEquals("The cached zone should return the correct name from the underlying zone.",
                expectedName, actualName);
    }
}