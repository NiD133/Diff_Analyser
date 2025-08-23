package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    @Test
    public void withZone_givenDifferentZone_returnsNewInstance() {
        // Arrange
        // Get the singleton instance of IslamicChronology in UTC.
        final IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();
        final DateTimeZone differentZone = DateTimeZone.forOffsetHoursMinutes(1, 1);

        // Act
        // Request a new Chronology instance with a different time zone.
        final Chronology zonedChronology = utcChronology.withZone(differentZone);

        // Assert
        // The returned object should not be the same instance as the original UTC one.
        assertNotNull(zonedChronology);
        assertNotSame("A new Chronology instance should be returned for a different zone.", utcChronology, zonedChronology);
        
        // Also, verify that the new instance has the correct time zone.
        assertEquals("The new instance should have the specified time zone.", differentZone, zonedChronology.getZone());
    }
}