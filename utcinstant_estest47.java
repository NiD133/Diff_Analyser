package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the string representation of {@link UtcInstant}.
 */
public class UtcInstantToStringTest {

    @Test
    public void toString_atModifiedJulianDayEpoch_returnsCorrectIsoFormattedString() {
        // The documentation for UtcInstant specifies that a Modified Julian Day (MJD) of 0
        // corresponds to the epoch date 1858-11-17. This test verifies the toString()
        // representation for the exact start of that epoch day.

        // Arrange
        long modifiedJulianDay = 0L;
        long nanoOfDay = 0L;
        UtcInstant mjdEpochStart = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, nanoOfDay);
        String expectedIsoString = "1858-11-17T00:00:00Z";

        // Act
        String actualIsoString = mjdEpochStart.toString();

        // Assert
        assertEquals(expectedIsoString, actualIsoString);
    }
}