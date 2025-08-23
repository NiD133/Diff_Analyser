package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import org.junit.Test;

/**
 * Tests for {@link FileTimes} focusing on NTFS time conversions.
 * This class contains tests related to converting between NTFS time and java.util.Date.
 */
public class FileTimesTest { // Renamed from FileTimes_ESTestTest6 for clarity

    /**
     * Tests that converting the minimum possible NTFS time (Long.MIN_VALUE) to a Date
     * and back again results in the original value. This verifies the round-trip
     * conversion for a critical boundary condition.
     */
    @Test
    public void testNtfsTimeDateConversionRoundTripWithLongMinValue() {
        // Arrange
        // An NTFS file time is a 64-bit value. We test the boundary condition Long.MIN_VALUE.
        final long originalNtfsTime = Long.MIN_VALUE;

        // The expected millisecond epoch value for an NTFS time of Long.MIN_VALUE.
        // This value is pre-calculated to verify the intermediate conversion step.
        // Calculation: floor((Long.MIN_VALUE + UNIX_TO_NTFS_OFFSET) / HUNDRED_NANOS_PER_MILLISECOND)
        final long expectedDateMillis = -933981677285478L;

        // Act
        // 1. Convert the NTFS time to a Date using the public API.
        final Date convertedDate = FileTimes.ntfsTimeToDate(originalNtfsTime);
        // 2. Convert the Date back to an NTFS time.
        final long roundTripNtfsTime = FileTimes.toNtfsTime(convertedDate);

        // Assert
        // Verify that the intermediate Date object has the correct millisecond value.
        assertEquals("The intermediate Date's millisecond value is incorrect.",
            expectedDateMillis, convertedDate.getTime());
            
        // Verify that the value after the round-trip conversion is the same as the original.
        assertEquals("The round-trip NTFS time conversion failed.",
            originalNtfsTime, roundTripNtfsTime);
    }
}