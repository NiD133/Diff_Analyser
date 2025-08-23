package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the CopticChronology class, focusing on time zone handling.
 */
public class CopticChronologyTest {

    /**
     * Tests that the withUTC() method returns a new Chronology instance
     * configured with the UTC time zone, while leaving the original instance unchanged.
     */
    @Test
    public void withUTC_shouldReturnChronologyInUTCZone() {
        // Arrange: Create a CopticChronology instance with a non-UTC time zone.
        final DateTimeZone parisZone = DateTimeZone.forID("Europe/Paris");
        final CopticChronology chronologyInParis = CopticChronology.getInstance(parisZone);

        // Act: Request the UTC-equivalent chronology.
        Chronology chronologyInUTC = chronologyInParis.withUTC();

        // Assert:
        // 1. The new chronology's time zone must be UTC.
        assertEquals("The new chronology should be in the UTC time zone",
                DateTimeZone.UTC, chronologyInUTC.getZone());

        // 2. The original chronology's time zone should remain unchanged (verifying immutability).
        assertEquals("The original chronology's time zone should not be modified",
                parisZone, chronologyInParis.getZone());

        // 3. The returned instance should be a different object from the original.
        assertNotSame("withUTC() should return a new or cached instance, not the same instance",
                chronologyInParis, chronologyInUTC);
    }
}