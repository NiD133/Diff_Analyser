package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the equals() method of {@link IslamicChronology}.
 */
public class IslamicChronologyEqualsTest {

    /**
     * Tests that two IslamicChronology instances are not equal if they have
     * different time zones and different leap year patterns.
     */
    @Test
    public void equals_returnsFalse_forDifferentTimeZoneAndLeapYearPattern() {
        // Arrange
        // Create a chronology with the default leap year pattern (16-based) in UTC.
        IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();

        // Create a second chronology with a non-UTC time zone and the "Indian" leap year pattern.
        DateTimeZone nonUtcZone = DateTimeZone.forOffsetMillis(1);
        IslamicChronology differentChronology = IslamicChronology.getInstance(
                nonUtcZone, IslamicChronology.LEAP_YEAR_INDIAN);

        // Act & Assert
        // The two chronologies have different configurations, so they should not be equal.
        assertNotEquals(utcChronology, differentChronology);
    }
}