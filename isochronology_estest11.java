package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ISOChronology}.
 */
public class ISOChronologyTest {

    /**
     * Tests that calling withZone(null) on an instance already in the default
     * time zone returns the same cached instance.
     *
     * The Joda-Time documentation specifies that passing a null DateTimeZone to
     * withZone() should result in using the default time zone. This test verifies
     * the optimization that if the chronology is already in the requested zone,
     * the same instance is returned.
     */
    @Test
    public void withZone_givenNullZoneOnDefaultInstance_returnsSameInstance() {
        // Arrange: Get an ISOChronology instance for the default time zone.
        ISOChronology defaultZoneChronology = ISOChronology.getInstance();

        // Act: Request the chronology for a null zone, which should resolve to the default zone.
        Chronology resultChronology = defaultZoneChronology.withZone(null);

        // Assert: The returned object should be the exact same instance, not just an equal one.
        // This confirms the expected caching/optimization behavior.
        assertSame(
            "Expected withZone(null) to return the same instance for a default-zone chronology",
            defaultZoneChronology,
            resultChronology
        );
    }
}