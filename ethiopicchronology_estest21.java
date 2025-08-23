package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the EthiopicChronology class.
 */
public class EthiopicChronologyTest {

    /**
     * Tests that calling withUTC() on a chronology with a non-UTC time zone
     * returns a new instance configured for the UTC time zone.
     */
    @Test
    public void withUTC_whenChronologyIsNotUTC_returnsNewInstanceWithUTCZone() {
        // Arrange: Create a chronology with a specific, non-UTC time zone.
        DateTimeZone nonUtcZone = DateTimeZone.forOffsetHours(2);
        EthiopicChronology originalChronology = EthiopicChronology.getInstance(nonUtcZone);

        // Act: Request the equivalent chronology in UTC.
        Chronology utcChronology = originalChronology.withUTC();

        // Assert: The method should return a different instance because the time zone has changed.
        assertNotSame(
            "A new Chronology instance should be returned for a different time zone.",
            originalChronology,
            utcChronology
        );

        // Further assert that the new instance has the correct (UTC) time zone.
        assertEquals(
            "The returned Chronology should have the UTC time zone.",
            DateTimeZone.UTC,
            utcChronology.getZone()
        );
    }
}