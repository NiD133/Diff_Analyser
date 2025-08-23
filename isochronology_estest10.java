package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link ISOChronology} class, focusing on its equality behavior.
 */
public class ISOChronologyTest {

    /**
     * Verifies that two ISOChronology instances are not considered equal
     * if they are configured with different time zones.
     */
    @Test
    public void equals_returnsFalse_forChronologiesWithDifferentTimeZones() {
        // Arrange: Create a base chronology in UTC and a second one in a different time zone.
        ISOChronology utcChronology = ISOChronology.getInstanceUTC();
        DateTimeZone nonUtcZone = DateTimeZone.forOffsetMillis(-1322);
        
        // Act: Create a new chronology instance with the non-UTC zone.
        Chronology nonUtcChronology = utcChronology.withZone(nonUtcZone);
        
        // Assert: The two chronologies should not be equal.
        assertNotEquals("Chronologies with different time zones should not be equal.",
                        utcChronology, nonUtcChronology);
    }
}