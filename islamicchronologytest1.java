package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the factory methods of {@link IslamicChronology}.
 * This test focuses on the getInstanceUTC() factory method.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the getInstanceUTC() factory method returns a non-null instance of
     * IslamicChronology configured with the UTC time zone.
     */
    @Test
    public void getInstanceUTC_shouldReturnChronologyInstanceWithUTCZone() {
        // Arrange: No setup is needed as we are testing a static factory method.

        // Act: Get the singleton instance of the IslamicChronology in UTC.
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();

        // Assert: Verify that the returned chronology has the correct properties.
        // 1. The time zone must be UTC.
        assertEquals("The chronology's time zone should be UTC", DateTimeZone.UTC, chronology.getZone());

        // 2. The returned object should be an instance of IslamicChronology.
        assertSame("The returned object should be of type IslamicChronology",
                IslamicChronology.class, chronology.getClass());
    }
}