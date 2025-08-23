package org.joda.time.chrono;

import static org.junit.Assert.assertNotEquals;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests for the equals() method in the IslamicChronology class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that two IslamicChronology instances are not considered equal
     * if they are configured with different time zones, even if they share
     * the same leap year pattern.
     */
    @Test
    public void equals_shouldReturnFalse_whenChronologiesHaveDifferentTimeZones() {
        // Arrange: Create two chronology instances with different time zones.
        // The default leap year pattern is used for both.
        IslamicChronology chronologyInUtc = IslamicChronology.getInstanceUTC();
        
        DateTimeZone nonUtcZone = DateTimeZone.forOffsetMillis(1);
        IslamicChronology chronologyInNonUtc = IslamicChronology.getInstance(nonUtcZone);

        // Act & Assert: The equals() method should return false because the time zones differ.
        assertNotEquals(chronologyInUtc, chronologyInNonUtc);
    }
}