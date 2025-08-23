package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the hashCode() method of {@link ISOChronology}.
 */
public class ISOChronologyHashCodeTest {

    @Test
    public void hashCode_shouldBeBasedOnTimeZone() {
        // The hashCode() of an ISOChronology instance is determined by its time zone.
        // This test verifies that two instances with the same time zone have the same
        // hash code, and two instances with different time zones have different hash codes.

        // Arrange: Create chronologies with specific time zones.
        DateTimeZone londonZone = DateTimeZone.forID("Europe/London");
        ISOChronology chronologyInLondon1 = ISOChronology.getInstance(londonZone);
        ISOChronology chronologyInLondon2 = ISOChronology.getInstance(londonZone);

        DateTimeZone parisZone = DateTimeZone.forID("Europe/Paris");
        ISOChronology chronologyInParis = ISOChronology.getInstance(parisZone);

        // Act & Assert

        // 1. Test for consistency:
        // Chronologies for the same time zone are equal and must have the same hash code.
        assertEquals(
            "Hash code should be consistent for the same time zone",
            chronologyInLondon1.hashCode(),
            chronologyInLondon2.hashCode()
        );

        // 2. Test for variance:
        // Chronologies for different time zones are not equal and should have different hash codes.
        assertNotEquals(
            "Hash code should be different for different time zones",
            chronologyInLondon1.hashCode(),
            chronologyInParis.hashCode()
        );
    }
}