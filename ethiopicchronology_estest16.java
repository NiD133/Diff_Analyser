package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for {@link EthiopicChronology}.
 */
public class EthiopicChronologyTest {

    @Test
    public void withZone_whenZoneIsUnchanged_shouldReturnSameInstance() {
        // Joda-Time chronologies are immutable. This test verifies that if we request a chronology
        // with the same time zone it already has, the same instance is returned. This is an
        // important performance optimization.

        // Arrange: Get an instance of EthiopicChronology for the default time zone.
        // Passing null to getInstance() uses the default zone.
        EthiopicChronology defaultZoneChronology = EthiopicChronology.getInstance(null);

        // Act: Request a chronology with the same (default) time zone by passing null again.
        Chronology resultChronology = defaultZoneChronology.withZone(null);

        // Assert: The returned object should be the exact same instance, not just an equal one.
        assertSame("Expected withZone() to return the same instance for the same time zone",
                defaultZoneChronology, resultChronology);
    }
}