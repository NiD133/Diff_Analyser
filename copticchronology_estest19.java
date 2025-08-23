package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the CopticChronology class, focusing on its time zone handling.
 */
public class CopticChronologyTest {

    /**
     * Tests that calling withZone() with the same time zone returns the same instance.
     * Chronology objects are immutable, so this is an important caching optimization.
     */
    @Test
    public void withZone_whenZoneIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create a CopticChronology instance for the UTC time zone.
        CopticChronology initialChronology = CopticChronology.getInstanceUTC();

        // Act: Request the chronology for the same time zone it already has.
        Chronology resultChronology = initialChronology.withZone(DateTimeZone.UTC);

        // Assert: The returned object should be the exact same instance, not just an equal one.
        assertSame("Expected the same instance when the time zone is not changed",
                   initialChronology, resultChronology);
    }
}